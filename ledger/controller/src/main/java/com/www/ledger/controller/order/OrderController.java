package com.www.ledger.controller.order;

import com.www.common.config.security.meta.JwtAuthorizationTokenFilter;
import com.www.common.data.response.Response;
import com.www.ledger.data.dto.OrderDTO;
import com.www.ledger.data.vo.order.OrderListRequest;
import com.www.ledger.data.vo.order.OrderListResponse;
import com.www.ledger.data.vo.order.OrderNewRequest;
import com.www.ledger.service.order.IOrderService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>@Description 订单信息controller </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/11 19:20 </p>
 */
@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    /**
     * <p>@Description 保存订单信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/16 22:41 </p>
     * @param orderRequest 订单信息
     * @return Response<java.lang.String>
     */
    @PostMapping("new")
    public Response<String> saveOrderInfo(@Validated OrderNewRequest orderRequest){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(JwtAuthorizationTokenFilter.getUserId()).setOrderId(orderRequest.getOrdId())
                .setShopId(orderRequest.getShopId()).setOrderDateStr(orderRequest.getOrdDat())
                .setSupplyId(orderRequest.getSupId()).setGoodsId(orderRequest.getGdsId())
                .setGoodsName(orderRequest.getGdsName()).setOrderState(orderRequest.getOrdSta())
                .setSaleAmount(orderRequest.getSalAmt()).setPaymentAmount(orderRequest.getPayAmt())
                .setCostAmount(orderRequest.getCosAmt()).setPayoutAmount(orderRequest.getOthAmt())
                .setRemark(orderRequest.getRemark()).setOiId(orderRequest.getOiId());
        return orderService.saveOrderInfo(orderDTO);
    }
    /**
     * <p>@Description 删除订单 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 09:58 </p>
     * @param oiId 订单ID
     * @return
     */
    @PostMapping("dlt/{oiId}")
    public Response<String> deleteOrder(@PathVariable("oiId") Long oiId){
        return orderService.deleteOrderInfo(JwtAuthorizationTokenFilter.getUserId(),oiId);
    }
    /**
     * <p>@Description 查询订单信息列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/16 22:43 </p>
     * @param orderRequest 订单查询条件
     * @return Response<java.util.List < com.www.ledger.data.dto.OrderDTO>>
     */
    @GetMapping("list")
    public Response<List<OrderListResponse>> findOrdeList(@Validated OrderListRequest orderRequest){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(JwtAuthorizationTokenFilter.getUserId()).setOrderDateStr(orderRequest.getOrdDat())
                .setShopId(orderRequest.getShopId()).setSupplyId(orderRequest.getSupId()).setOrderId(orderRequest.getOrdId())
                .setGoodsId(orderRequest.getGdsId()).setOrderState(orderRequest.getOrdSta());
        Response<List<OrderDTO>> dtoResponse = orderService.findOrdeList(orderDTO,orderRequest.getPageNum(),orderRequest.getPageSize());
        List<OrderListResponse> respList = Optional.ofNullable(dtoResponse.getData()).filter(e -> CollectionUtils.isNotEmpty(dtoResponse.getData()))
                .map(list -> {
                    List<OrderListResponse> tempList = new ArrayList<>();
                    list.forEach(dto -> {
                        OrderListResponse order = new OrderListResponse();
                        order.setOrdId(dto.getOrderId()).setShopId(dto.getShopId())
                             .setShopNm(dto.getShopName()).setOrdDat(dto.getOrderDateStr())
                             .setSupId(dto.getSupplyId()).setGdsId(dto.getGoodsId())
                             .setGdsName(dto.getGoodsName()).setShopNm(dto.getShopName())
                             .setUrl(dto.getGoodsUrl()).setOrdSta(dto.getOrderState())
                             .setOrdStaNm(dto.getOrderStateName()).setSalAmt(dto.getSaleAmount())
                             .setPayAmt(dto.getPaymentAmount()).setCosAmt(dto.getCostAmount())
                             .setOthAmt(dto.getPayoutAmount()).setTalCos(dto.getTotalCost())
                             .setGroPro(dto.getGrossProfit()).setGroProRat(dto.getGrossProfitRate())
                             .setRemark(dto.getRemark()).setOiId(dto.getOiId());
                        tempList.add(order);
                    });
                    return tempList;
                }).orElse(null);
        return new Response<>(dtoResponse,respList);
    }
}
