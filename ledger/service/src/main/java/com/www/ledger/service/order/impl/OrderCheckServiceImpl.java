package com.www.ledger.service.order.impl;

import com.www.common.config.code.CodeDict;
import com.www.common.config.exception.BusinessException;
import com.www.common.data.enums.DateFormatEnum;
import com.www.common.utils.DateUtils;
import com.www.ledger.data.dao.IOrderInfoDAO;
import com.www.ledger.data.dao.IUserShopDAO;
import com.www.ledger.data.dto.OrderDTO;
import com.www.ledger.data.entity.OrderInfoEntity;
import com.www.ledger.data.entity.UserShopEntity;
import com.www.ledger.data.enums.CodeTypeEnum;
import com.www.ledger.service.order.IOrderCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>@Description 订单信息校验service </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/16 23:03 </p>
 */
@Service
public class OrderCheckServiceImpl implements IOrderCheckService {
    @Autowired
    private IOrderInfoDAO orderInfoDAO;
    @Autowired
    private IUserShopDAO userShopDAO;

    /**
     * <p>@Description 保存订单信息前处理校验 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/16 23:04 </p>
     * @param orderDTO    订单信息
     * @return boolean 不为空校验通过，为空校验不通过
     */
    @Override
    public OrderInfoEntity checkBeforeSaveOrderInfo(OrderDTO orderDTO) {
        //校验订单状态值
        if(CodeDict.isIllegalValue(CodeTypeEnum.OrderState_Sended.getType(), orderDTO.getOrderState())){
            throw new BusinessException("订单状态错误");
        }
        Date orderDate = DateUtils.parse(orderDTO.getOrderDateStr(), DateFormatEnum.YYYYMMDD1);
        if(orderDate == null){
            throw new BusinessException("订单日期错误");
        }
        orderDTO.setOrderDate(orderDate);
        //判断用户店铺是否存在且有效
        UserShopEntity shopEntity = userShopDAO.findUserShop(orderDTO.getUserId(),orderDTO.getShopId());
        //订单ID不为空，判断该订单是否属于该用户
        if(orderDTO.getOiId() != null){
            OrderInfoEntity orderEntity = orderInfoDAO.findOrderInfo(orderDTO.getUserId(),orderDTO.getOiId());
            if(orderEntity == null){
                throw new BusinessException("订单信息不存在");
            }else {
                return orderEntity;
            }
        }
        return new OrderInfoEntity();
    }
}
