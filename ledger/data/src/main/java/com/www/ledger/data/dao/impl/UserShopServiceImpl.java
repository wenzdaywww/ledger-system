package com.www.ledger.data.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.www.common.config.code.CodeDict;
import com.www.common.config.exception.BusinessException;
import com.www.ledger.data.dao.IUserShopDAO;
import com.www.ledger.data.entity.UserShopEntity;
import com.www.ledger.data.enums.CodeTypeEnum;
import com.www.ledger.data.mapper.UserShopMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * <p>@Description 用户账簿信息Service实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
@Repository
public class UserShopServiceImpl extends ServiceImpl<UserShopMapper, UserShopEntity> implements IUserShopDAO {
    @Autowired
    private UserShopMapper userShopMapper;

    /**
     * <p>@Description 查询用户的有效店铺 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:07 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @return 店铺信息
     */
    @Override
    public UserShopEntity findUserShop(String userId, Long shopId) {
        QueryWrapper<UserShopEntity> shopWrrapper = new QueryWrapper<>();
        shopWrrapper.lambda().eq(UserShopEntity::getUserId,userId)
                .eq(UserShopEntity::getShopId,shopId)
                .eq(UserShopEntity::getShopState, CodeDict.getValue(CodeTypeEnum.ShopState_Valid.getType(), CodeTypeEnum.ShopState_Valid.getKey()));
        UserShopEntity entity = userShopMapper.selectOne(shopWrrapper);
        if(entity == null){
            throw new BusinessException("店铺不存在");
        }
        return entity;
    }
}
