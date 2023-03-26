package com.www.ledger.controller.order;

import com.www.common.config.security.meta.JwtAuthorizationTokenFilter;
import com.www.common.data.response.Result;
import com.www.ledger.data.dto.OrderDTO;
import com.www.ledger.data.vo.order.OrderIptOutVO;
import com.www.ledger.data.vo.order.OrderListInVO;
import com.www.ledger.data.vo.order.OrderListOutVO;
import com.www.ledger.data.vo.order.OrderNewInVO;
import com.www.ledger.service.order.IOrderService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>@Description 订单信息controller </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/11 19:20 </p>
 */
@Validated
@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    /**
     * <p>@Description 导入订单文件 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/22 19:17 </p>
     * @param file 订单文件信息
     * @param shopId 店铺ID
     * @param pwd 文件密码
     * @return 导入失败的则下载订单数据信息
     */
    @PostMapping("ipt")
    public ResponseEntity<Result<OrderIptOutVO>> importOrder(MultipartFile file, @NotNull(message = "店铺不能为空") Long shopId, String pwd) throws Exception{
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(JwtAuthorizationTokenFilter.getUserId()).setShopId(shopId);
        Result<String[]> result = orderService.importOrder(file,orderDTO,pwd);
        OrderIptOutVO iptOutVO = new OrderIptOutVO();
        iptOutVO.setUrl(result.getData()[0]).setMsg(result.getData()[1]);
        return new ResponseEntity<>(new Result<>(iptOutVO),HttpStatus.OK);
    }
    /**
     * <p>@Description 保存订单信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/16 22:41 </p>
     * @param newInVO 订单信息
     * @return Response<java.lang.String>
     */
    @PostMapping("new")
    public Result<String> saveOrderInfo(@Validated OrderNewInVO newInVO){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(JwtAuthorizationTokenFilter.getUserId()).setOrderId(newInVO.getOrdId())
                .setShopId(newInVO.getShopId()).setOrderDateStr(newInVO.getOrdDat())
                .setSupplyId(newInVO.getSupId()).setGoodsId(newInVO.getGdsId())
                .setGoodsName(newInVO.getGdsName()).setOrderState(newInVO.getOrdSta())
                .setSaleAmount(newInVO.getSalAmt()).setPaymentAmount(newInVO.getPayAmt())
                .setCostAmount(newInVO.getCosAmt()).setPayoutAmount(newInVO.getOthAmt())
                .setRemark(newInVO.getRemark()).setOiId(newInVO.getOiId());
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
    public Result<String> deleteOrder(@PathVariable("oiId") Long oiId){
        return orderService.deleteOrderInfo(JwtAuthorizationTokenFilter.getUserId(),oiId);
    }
    /**
     * <p>@Description 查询订单信息列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/16 22:43 </p>
     * @param orderInVO 订单查询条件
     * @return Response<java.util.List < com.www.ledger.data.dto.OrderDTO>>
     */
    @GetMapping("list")
    public Result<List<OrderListOutVO>> findOrdeList(@Validated OrderListInVO orderInVO){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(JwtAuthorizationTokenFilter.getUserId())
                .setShopId(orderInVO.getShopId()).setSupplyId(orderInVO.getSupId()).setOrderId(orderInVO.getOrdId())
                .setGoodsId(orderInVO.getGdsId()).setOrderState(orderInVO.getOrdSta())
                .setStartDateStr(orderInVO.getStrDat()).setEndDateStr(orderInVO.getEndDat());
        Result<List<OrderDTO>> listResult = orderService.findOrdeList(orderDTO,orderInVO.getPageNum(),orderInVO.getPageSize());
        List<OrderListOutVO> respList = Optional.ofNullable(listResult.getData()).filter(e -> CollectionUtils.isNotEmpty(listResult.getData()))
                .map(list -> {
                    List<OrderListOutVO> tempList = new ArrayList<>();
                    list.forEach(dto -> {
                        OrderListOutVO order = new OrderListOutVO();
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
        return new Result<>(listResult,respList);
    }
}
