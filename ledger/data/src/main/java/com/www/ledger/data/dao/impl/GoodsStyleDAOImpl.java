package com.www.ledger.data.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.www.ledger.data.dao.IGoodsStyleDAO;
import com.www.ledger.data.entity.GoodsStyleEntity;
import com.www.ledger.data.mapper.GoodsStyleMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>@Description 商品样式信息DAO实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
@Repository
public class GoodsStyleDAOImpl extends ServiceImpl<GoodsStyleMapper,GoodsStyleEntity> implements IGoodsStyleDAO {
}
