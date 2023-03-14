package com.www.ledger.service.entity.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.www.ledger.data.entity.ShopSalesEntity;
import com.www.ledger.data.mapper.ShopSalesMapper;
import com.www.ledger.service.entity.IShopSalesService;
import org.springframework.stereotype.Service;

/**
 * <p>@Description 店铺销售额信息Service实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
@Service
public class ShopSalesServiceImpl extends ServiceImpl<ShopSalesMapper, ShopSalesEntity> implements IShopSalesService {
}
