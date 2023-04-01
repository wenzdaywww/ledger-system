package com.www.ledger.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.common.config.mybatis.annotation.RowLimitInterceptor;
import com.www.ledger.data.dto.ShopDTO;
import com.www.ledger.data.dto.YearDTO;
import com.www.ledger.data.entity.YearSalesEntity;
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
public interface YearSalesMapper extends BaseMapper<YearSalesEntity> {
    /**
     * <p>@Description 查询近10年年销售额数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 21:40 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID 为空则查询用户店铺汇总年销售额，不为空则查询指定店铺年销售额
     * @param date   年销售额数据
     * @return 年销售额数据
     */
    List<YearDTO> findLastYearData(@Param("userId") String userId, @Param("shopId") Long shopId, @Param("date") String date);
    /**
     * <p>@Description 查询用户店铺的年销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:23 </p>
     * @param userId 用户ID
     * @return 用户店铺的年销售数据
     */
    @RowLimitInterceptor
    List<YearSalesEntity> findShopYearSalesList(@Param("userId") String userId);
    /**
     * <p>@Description 查询用户店铺汇总的年销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:23 </p>
     * @param userId 用户ID
     * @return 用户店铺汇总的年销售数据
     */
    @RowLimitInterceptor
    List<YearSalesEntity> findTotalYearSalesList(@Param("userId") String userId);
    /**
     * <p>@Description 统计店铺销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 23:50 </p>
     * @param userId 用户ID
     * @return
     */
    @RowLimitInterceptor
    List<ShopDTO> countShopData(@Param("userId") String userId);
    /**
     * <p>@Description 根据所有店铺的年销售额汇总每年所有店铺销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 23:50 </p>
     * @param userId 用户ID
     * @return 每年所有店铺销售额
     */
    @RowLimitInterceptor
    List<YearDTO> countAllYearData(@Param("userId") String userId);
    /**
     * <p>@Description 查询我的年销售额列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:55 </p>
     * @param page 分页信息
     * @param dto 查询条件
     * @return
     */
    Page<YearDTO> findYearList(Page<YearDTO> page, @Param("obj") YearDTO dto);
}
