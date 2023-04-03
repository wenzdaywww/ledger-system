package com.www.ledger.data.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
     * <p>@Date 2023/4/2 22:14 </p>
     * @param userId 用户ID
     * @param isShop true查询店铺的月销售额，false查询所有店铺的月销售额
     * @return 月销售数据
     */
    @Override
    public List<MonthDTO> exportMonthSalesData(String userId, boolean isShop) {
        return monthSalesMapper.exportMonthSalesData(userId, isShop);
    }
    /**
     * <p>@Description 查询用户店铺的月销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:40 </p>
     * @param userId 用户ID
     * @return 店铺月销售数据
     */
    @Override
    public List<MonthSalesEntity> findShopMonthSalesList(String userId) {
        return StringUtils.isBlank(userId) ? null :monthSalesMapper.findShopMonthSalesList(userId);
    }
    /**
     * <p>@Description 查询用户店铺汇总的月销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:40 </p>
     * @param userId 用户ID
     * @return 店铺汇总月销售数据
     */
    @Override
    public List<MonthSalesEntity> findTotalMonthSalesList(String userId) {
        return StringUtils.isBlank(userId) ? null :monthSalesMapper.findTotalMonthSalesList(userId);
    }
    /**
     * <p>@Description 删除用户的月销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:26 </p>
     * @param userId 用户ID
     * @param isShop true统计店铺的月销售额，false统计所有店铺的月销售额
     * @return true删除成功，false删除失败
     */
    @Override
    public boolean deleteMonthList(String userId, boolean isShop) {
        if(StringUtils.isBlank(userId)){
            return false;
        }
        QueryWrapper<MonthSalesEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MonthSalesEntity::getUserId,userId);
        if(isShop){
            wrapper.lambda().isNotNull(MonthSalesEntity::getShopId);
        }else {
            wrapper.lambda().isNull(MonthSalesEntity::getShopId);
        }
        return monthSalesMapper.delete(wrapper) != 0;
    }
    /**
     * <p>@Description 查询近一年月销售额数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 21:40 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID 为空则查询用户店铺汇总日销售额，不为空则查询指定店铺日销售额
     * @param date 月销售额数据
     * @return 月销售额数据
     */
    @Override
    public List<MonthDTO> findLastMonthData(String userId, Long shopId, String date) {
        return monthSalesMapper.findLastMonthData(userId,shopId,date);
    }
    /**
     * <p>@Description 统计店铺的年销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 21:40 </p>
     * @param userId 用户ID
     * @return 店铺的年销售额
     */
    @Override
    public List<YearDTO> countShopYearData(String userId) {
        return monthSalesMapper.countShopYearData(userId);
    }
    /**
     * <p>@Description 统计店铺汇总的月销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 21:40 </p>
     * @param userId 用户ID
     * @return 店铺汇总的月销售额
     */
    @Override
    public List<MonthDTO> countTotalMonthData(String userId) {
        return monthSalesMapper.countTotalMonthData(userId);
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
