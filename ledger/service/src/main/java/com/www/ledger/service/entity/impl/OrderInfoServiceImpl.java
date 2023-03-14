package com.www.ledger.service.entity.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.www.ledger.data.entity.OrderInfoEntity;
import com.www.ledger.data.mapper.OrderInfoMapper;
import com.www.ledger.service.entity.IOrderInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>@Description 订单信息Service实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfoEntity> implements IOrderInfoService {
}
