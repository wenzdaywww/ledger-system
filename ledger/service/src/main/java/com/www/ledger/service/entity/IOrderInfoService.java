package com.www.ledger.service.entity;

import com.baomidou.mybatisplus.extension.service.IService;
import com.www.ledger.data.entity.OrderInfoEntity;

/**
 * <p>@Description 订单信息Service接口 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
public interface IOrderInfoService extends IService<OrderInfoEntity> {
    /**
     * <p>@Description 删除用户的订单信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 21:00 </p>
     * @param userId 用户ID
     * @param oiId 订单主键id
     * @return 订单信息
     */
    boolean deleteOrderInfo(String userId,Long oiId);
    /**
     * <p>@Description 查询用户的订单信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 21:00 </p>
     * @param userId 用户ID
     * @param oiId 订单主键id
     * @return 订单信息
     */
    OrderInfoEntity findOrderInfo(String userId,Long oiId);
}
