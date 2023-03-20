package com.www.ledger.service.entity.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.www.ledger.data.dto.BookDTO;
import com.www.ledger.data.dto.ShopDTO;
import com.www.ledger.data.entity.ShopSalesEntity;
import com.www.ledger.data.mapper.ShopSalesMapper;
import com.www.ledger.service.entity.IShopSalesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>@Description 店铺销售额信息Service实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
@Service
public class ShopSalesServiceImpl extends ServiceImpl<ShopSalesMapper, ShopSalesEntity> implements IShopSalesService {
    @Autowired
    private ShopSalesMapper shopSalesMapper;

    /**
     * <p>@Description 查询用户的店销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:56 </p>
     *
     * @param userId 用户ID
     * @return 店销售数据
     */
    @Override
    public List<ShopSalesEntity> findShopSalesList(String userId) {
        if(StringUtils.isBlank(userId)){
            return null;
        }
        QueryWrapper<ShopSalesEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ShopSalesEntity::getUserId,userId);
        return shopSalesMapper.selectList(wrapper);
    }
    /**
     * <p>@Description 统计所有店铺销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 00:46 </p>
     * @param userId 用户ID
     * @return
     */
    @Override
    public BookDTO countAllShopData(String userId) {
        return shopSalesMapper.countAllShopData(userId);
    }
    /**
     * <p>@Description 查询我的店铺信息列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:55 </p>
     * @param page 分页信息
     * @param dto  店铺信息查询条件
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.www.ledger.data.dto.ShopDTO>
     */
    @Override
    public Page<ShopDTO> findShopList(Page<ShopDTO> page, ShopDTO dto) {
        return shopSalesMapper.findShopList(page,dto);
    }
}
