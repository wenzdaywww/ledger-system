package com.www.ledger.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.common.config.mybatis.annotation.RowLimitInterceptor;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.dto.YearDTO;
import com.www.ledger.data.entity.MonthSalesEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>@Description 月销售额信息Mapper </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 19:43 </p>
 */
@Mapper
public interface MonthSalesMapper extends BaseMapper<MonthSalesEntity> {
    /**
     * <p>@Description 查询用户店铺的月销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:40 </p>
     * @param userId 用户ID
     * @return 店铺月销售数据
     */
    @RowLimitInterceptor
    List<MonthSalesEntity> findShopMonthSalesList(@Param("userId") String userId);
    /**
     * <p>@Description 查询用户店铺汇总的月销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:40 </p>
     * @param userId 用户ID
     * @return 店铺汇总月销售数据
     */
    @RowLimitInterceptor
    List<MonthSalesEntity> findTotalMonthSalesList(@Param("userId") String userId);
    /**
     * <p>@Description 查询用户monthStr月份销量前1的店铺 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 19:13 </p>
     * @param userId 用户ID
     * @param monthStr 月份
     * @return 店铺ID
     */
    List<Long> findMaxSalesShop(@Param("userId") String userId, @Param("month") String monthStr);
    /**
     * <p>@Description 查询用户最近一年所有店铺销售额趋势图 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 21:40 </p>
     * @param userId 用户ID
     * @param date 统计的最近日期
     * @return
     */
    List<MonthDTO> findLastYearSales(@Param("userId") String userId,@Param("date") String date);
    /**
     * <p>@Description 统计店铺的年销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 21:40 </p>
     * @param userId 用户ID
     * @return 店铺的年销售额
     */
    @RowLimitInterceptor
    List<YearDTO> countShopYearData(@Param("userId") String userId);
    /**
     * <p>@Description 统计店铺汇总的月销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 21:40 </p>
     * @param userId 用户ID
     * @return 店铺汇总的月销售额
     */
    @RowLimitInterceptor
    List<MonthDTO> countTotalMonthData(@Param("userId") String userId);
    /**
     * <p>@Description 查询月销售额列表信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:55 </p>
     * @param page 分页信息
     * @param dto 查询条件
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.www.ledger.data.dto.ShopDTO>
     */
    Page<MonthDTO> findMonthList(Page<MonthDTO> page, @Param("obj") MonthDTO dto);
}
