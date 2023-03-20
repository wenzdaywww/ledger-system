package com.www.ledger.service.entity.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.www.ledger.data.entity.OrderInfoEntity;
import com.www.ledger.data.mapper.OrderInfoMapper;
import com.www.ledger.service.entity.IOrderInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>@Description 订单信息Service实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfoEntity> implements IOrderInfoService {
    @Autowired
    private OrderInfoMapper orderInfoMapper;

    /**
     * <p>@Description 删除用户的订单信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 21:00 </p>
     * @param userId 用户ID
     * @param oiId   订单主键id
     * @return 订单信息
     */
    @Override
    public boolean deleteOrderInfo(String userId, Long oiId) {
        if(StringUtils.isBlank(userId) || oiId == null){
            return false;
        }
        QueryWrapper<OrderInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderInfoEntity::getOiId,oiId)
                .eq(OrderInfoEntity::getUserId,userId);
        return orderInfoMapper.delete(wrapper) != 0;
    }
    /**
     * <p>@Description 查询用户的订单信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 21:00 </p>
     * @param userId 用户ID
     * @param oiId   订单主键id
     * @return 订单信息
     */
    @Override
    public OrderInfoEntity findOrderInfo(String userId, Long oiId) {
        if(StringUtils.isBlank(userId) || oiId == null){
            return null;
        }
        QueryWrapper<OrderInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderInfoEntity::getUserId,userId)
                .eq(OrderInfoEntity::getOiId,oiId);
        return orderInfoMapper.selectOne(wrapper);
    }
}
