package com.www.ledger.data.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.www.ledger.data.dao.IShopGoodsDAO;
import com.www.ledger.data.entity.ShopGoodsEntity;
import com.www.ledger.data.mapper.ShopGoodsMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>@Description 店铺商品信息DAO实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
@Repository
public class ShopGoodsDAOImpl extends ServiceImpl<ShopGoodsMapper, ShopGoodsEntity> implements IShopGoodsDAO {
}
