package com.www.ledger.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.common.config.code.CodeDict;
import com.www.common.data.enums.ResponseEnum;
import com.www.common.data.response.Response;
import com.www.common.utils.DateUtils;
import com.www.common.utils.MoneyUtils;
import com.www.common.utils.UidGeneratorUtils;
import com.www.ledger.data.dto.OrderDTO;
import com.www.ledger.data.entity.OrderInfoEntity;
import com.www.ledger.data.enums.CodeTypeEnum;
import com.www.ledger.data.mapper.OrderInfoMapper;
import com.www.ledger.service.entity.IOrderInfoService;
import com.www.ledger.service.order.IOrderCheckService;
import com.www.ledger.service.order.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * <p>@Description 订单信息service实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:06 </p>
 */
@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private IOrderInfoService orderInfoService;
    @Autowired
    private IOrderCheckService orderCheckService;

    /**
     * <p>@Description 保存订单信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/16 22:41 </p>
     * @param orderDTO 订单信息
     * @return Response<java.lang.String>
     */
    @Override
    public Response<String> saveOrderInfo(OrderDTO orderDTO) {
        Response<String> response = new Response<>();
        //保存订单信息前校验
        OrderInfoEntity orderEntity = orderCheckService.checkBeforeSaveOrderInfo(orderDTO,response);
        if(orderEntity == null){
            return response;
        }
        orderEntity.setOrderId(orderDTO.getOrderId())
                .setShopId(orderDTO.getShopId())
                .setUserId(orderDTO.getUserId())
                .setOrderDate(orderDTO.getOrderDate())
                .setSupplyId(orderDTO.getSupplyId())
                .setGoodsId(orderDTO.getGoodsId())
                .setGoodsName(orderDTO.getGoodsName())
                //订单状态为空，则设置为【待确认】
                .setOrderState(StringUtils.isNotBlank(orderDTO.getOrderState()) ? orderDTO.getOrderState()
                        : CodeDict.getValue(CodeTypeEnum.OrderState_Unconfirme.getType(),CodeTypeEnum.OrderState_Unconfirme.getKey()))
                .setSaleAmount(MoneyUtils.nullToZero(orderDTO.getSaleAmount()))
                .setPaymentAmount(MoneyUtils.nullToZero(orderDTO.getPaymentAmount()))
                .setCostAmount(MoneyUtils.nullToZero(orderDTO.getCostAmount()))
                .setPayoutAmount(MoneyUtils.nullToZero(orderDTO.getPayoutAmount()))
                .setRemark(orderDTO.getRemark())
                .setUpdateTime(DateUtils.getCurrentDateTime())
                .setCreateTime(orderEntity.getOiId() == null ? DateUtils.getCurrentDateTime() : orderEntity.getCreateTime());
        //订单ID为空则自动生成
        orderEntity.setOrderId(StringUtils.isNotBlank(orderEntity.getOrderId()) ? orderEntity.getOrderId()
                : Long.toString(UidGeneratorUtils.getSnowFlakeID()));
        //总成本计算
        orderEntity.setTotalCost(MoneyUtils.nullToZero(orderDTO.getCostAmount()).add(MoneyUtils.nullToZero(orderDTO.getPayoutAmount())));
        //毛利润计算
        orderEntity.setGrossProfit((orderEntity.getPaymentAmount()
                .subtract(orderEntity.getCostAmount()).subtract(orderEntity.getPayoutAmount())).setScale(2, RoundingMode.HALF_UP));
        //毛利率计算
        orderEntity.setGrossProfitRate(orderEntity.getCostAmount().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                : (orderEntity.getGrossProfit().divide(orderEntity.getCostAmount(),4,RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"))).setScale(2,RoundingMode.HALF_UP));
        if(orderInfoService.saveOrUpdate(orderEntity)){
            response.setResponse(ResponseEnum.SUCCESS,"保存成功");
        }else {
            response.setResponse(ResponseEnum.FAIL,"保存失败");
        }
        return response;
    }

    /**
     * <p>@Description 删除订单 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 09:59 </p>
     * @param userId 用户ID
     * @param oiId   订单ID
     * @return
     */
    @Override
    public Response<String> deleteOrderInfo(String userId, Long oiId) {
        if(orderInfoService.deleteOrderInfo(userId,oiId)){
            return new Response<>(ResponseEnum.SUCCESS,"删除成功");
        }
        return new Response<>(ResponseEnum.FAIL,"删除失败");
    }

    /**
     * <p>@Description 查询订单信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/16 22:43 </p>
     * @param orderDTO 订单查询条件
     * @param pageNum  分页数量
     * @param pageSize 页面
     * @return Response<java.util.List < com.www.ledger.data.dto.OrderDTO>>
     */
    @Override
    public Response<List<OrderDTO>> findOrdeList(OrderDTO orderDTO, int pageNum, long pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <= 0 ? 5 : pageSize;
        Page<OrderDTO> page = new Page<>(pageNum,pageSize);
        page = orderInfoMapper.findOrdeList(page,orderDTO);
        List<OrderDTO> shopList =  page.getRecords();
        if(CollectionUtils.isNotEmpty(shopList)){
            shopList.forEach(d -> {
                d.setOrderStateName(CodeDict.getCodeValueName(CodeTypeEnum.OrderState_Success.getType(), d.getOrderState()));
            });
        }
        Response<List<OrderDTO>> response = new Response<>(ResponseEnum.SUCCESS,shopList);
        response.setPageNum(pageNum);
        response.setPageSize(pageSize);
        response.setTotalNum(page.getTotal());
        return response;
    }
}
