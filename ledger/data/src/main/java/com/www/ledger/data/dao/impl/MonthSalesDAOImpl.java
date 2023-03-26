package com.www.ledger.data.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.www.ledger.data.dao.IMonthSalesDAO;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.dto.YearDTO;
import com.www.ledger.data.entity.MonthSalesEntity;
import com.www.ledger.data.mapper.MonthSalesMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * <p>@Description 月销售额信息DAO实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
@Repository
public class MonthSalesDAOImpl extends ServiceImpl<MonthSalesMapper, MonthSalesEntity> implements IMonthSalesDAO {
    @Autowired
    private MonthSalesMapper monthSalesMapper;

    /**
     * <p>@Description 查询用户的月销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:40 </p>
     * @param userId 用户ID
     * @return 月销售数据
     */
    @Override
    public List<MonthSalesEntity> findMonthSalesList(String userId) {
        if(StringUtils.isBlank(userId)){
            return null;
        }
        QueryWrapper<MonthSalesEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MonthSalesEntity::getUserId,userId);
        return monthSalesMapper.selectList(wrapper);
    }

    /**
     * <p>@Description 删除用户的月销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:40 </p>
     * @param userId 用户ID
     * @param msId   月销售ID
     * @return true成功，false失败
     */
    @Override
    public boolean deleteMonthSales(String userId, Long msId) {
        if(StringUtils.isBlank(userId) || msId == null){
            return false;
        }
        UpdateWrapper<MonthSalesEntity> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(MonthSalesEntity::getUserId,userId)
                .eq(MonthSalesEntity::getMsId,msId);
        return monthSalesMapper.delete(wrapper) != 0;
    }
    /**
     * <p>@Description 查询用户的月销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:40 </p>
     * @param userId 用户ID
     * @param msId   月销售ID
     * @return 月销售数据
     */
    @Override
    public MonthSalesEntity findMonthSales(String userId, Long msId) {
        if(StringUtils.isBlank(userId) || msId == null){
            return null;
        }
        QueryWrapper<MonthSalesEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MonthSalesEntity::getUserId,userId)
                .eq(MonthSalesEntity::getMsId,msId);
        return monthSalesMapper.selectOne(wrapper);
    }
    /**
     * <p>@Description 查询用户的月销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:40 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param month  月份（每月1日）
     * @return 月销售数据
     */
    @Override
    public MonthSalesEntity findMonthSales(String userId, Long shopId, Date month) {
        if(StringUtils.isBlank(userId) || shopId == null || month == null){
            return null;
        }
        QueryWrapper<MonthSalesEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MonthSalesEntity::getUserId,userId)
                .eq(MonthSalesEntity::getShopId,shopId)
                .eq(MonthSalesEntity::getMonthDate,month);
        return monthSalesMapper.selectOne(wrapper);
    }

    /**
     * <p>@Description 查询用户monthStr月份销量前1的店铺 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 19:13 </p>
     *
     * @param userId   用户ID
     * @param monthStr 月份
     * @return 店铺ID
     */
    @Override
    public List<Long> findMaxSalesShop(String userId, String monthStr) {
        return monthSalesMapper.findMaxSalesShop(userId,monthStr);
    }

    /**
     * <p>@Description 查询用户最近一年所有店铺销售额趋势图 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 21:40 </p>
     * @param userId 用户ID
     * @param date   统计的最近日期
     * @return
     */
    @Override
    public List<MonthDTO> findLastYearSales(String userId, String date) {
        return monthSalesMapper.findLastYearSales(userId,date);
    }

    /**
     * <p>@Description 统计年销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 21:40 </p>
     * @param userId 用户ID
     * @return
     */
    @Override
    public List<YearDTO> countYearData(String userId) {
        return monthSalesMapper.countYearData(userId);
    }

    /**
     * <p>@Description 查询月销售额列表信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:55 </p>
     * @param page 分页信息
     * @param dto  查询条件
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.www.ledger.data.dto.ShopDTO>
     */
    @Override
    public Page<MonthDTO> findMonthList(Page<MonthDTO> page, MonthDTO dto) {
        return monthSalesMapper.findMonthList(page,dto);
    }
}
