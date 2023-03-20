package com.www.ledger.service.entity;

import com.baomidou.mybatisplus.extension.service.IService;
import com.www.ledger.data.entity.UserShopEntity;

/**
 * <p>@Description 用户店铺信息Service接口 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
public interface IUserShopService extends IService<UserShopEntity> {
    /**
     * <p>@Description 查询用户的有效店铺 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:07 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @return 店铺信息
     */
    UserShopEntity findUserShop(String userId,Long shopId);
    /**
     * <p>@Description 查询用户的有效店铺数量 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:36 </p>
     * @param userId 用户ID
     * @return 有效店铺数量
     */
    int countUserShop(String userId);
}
