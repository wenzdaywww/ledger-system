package com.www.ledger.data.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.www.ledger.data.dao.IYearSalesDAO;
import com.www.ledger.data.dto.ShopDTO;
import com.www.ledger.data.dto.YearDTO;
import com.www.ledger.data.entity.YearSalesEntity;
import com.www.ledger.data.mapper.YearSalesMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>@Description 年销售额信息DAO实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
@Repository
public class YearSalesDAOImpl extends ServiceImpl<YearSalesMapper, YearSalesEntity> implements IYearSalesDAO {
    @Autowired
    private YearSalesMapper yearSalesMapper;

    /**
     * <p>@Description 根据所有店铺的年销售额汇总每年所有店铺销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/27 20:12 </p>
     * @param userId 用户ID
     * @return 每年所有店铺销售额
     */
    @Override
    public List<YearDTO> countTotalYearData(String userId) {
        return yearSalesMapper.countAllYearData(userId);
    }
    /**
     * <p>@Description 删除用户的年销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:26 </p>
     * @param userId 用户ID
     * @param isShop true删除店铺的年销售额，false删除统计的年销售额
     * @return true删除成功，false删除失败
     */
    @Override
    public boolean deleteYearList(String userId,boolean isShop) {
        if(StringUtils.isBlank(userId)){
            return false;
        }
        QueryWrapper<YearSalesEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(YearSalesEntity::getUserId,userId);
        if(isShop){
            wrapper.lambda().isNotNull(YearSalesEntity::getShopId);
        }else {
            wrapper.lambda().isNull(YearSalesEntity::getShopId);
        }
        return yearSalesMapper.delete(wrapper) != 0;
    }
    /**
     * <p>@Description 查询用户店铺的年销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:23 </p>
     * @param userId 用户ID
     * @return 用户店铺的年销售数据
     */
    @Override
    public List<YearSalesEntity> findShopYearSalesList(String userId) {
        return StringUtils.isBlank(userId) ? null : yearSalesMapper.findShopYearSalesList(userId);
    }
    /**
     * <p>@Description 查询用户店铺汇总的年销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:23 </p>
     * @param userId 用户ID
     * @return 用户店铺汇总的年销售数据
     */
    @Override
    public List<YearSalesEntity> findTotalYearSalesList(String userId) {
        return StringUtils.isBlank(userId) ? null : yearSalesMapper.findTotalYearSalesList(userId);
    }
    /**
     * <p>@Description 统计店铺销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 23:50 </p>
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public List<ShopDTO> countShopData(String userId) {
        return yearSalesMapper.countShopData(userId);
    }

    /**
     * <p>@Description 查询我的年销售额列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:55 </p>
     *
     * @param page 分页信息
     * @param dto  查询条件
     * @return
     */
    @Override
    public Page<YearDTO> findShopYearSalesList(Page<YearDTO> page, YearDTO dto) {
        return yearSalesMapper.findYearList(page,dto);
    }
}
