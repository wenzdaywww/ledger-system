package com.www.ledger.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.common.config.mybatis.annotation.RowLimitInterceptor;
import com.www.ledger.data.dto.DayDTO;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.entity.DaySalesEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>@Description 日销售额信息Mapper </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 19:43 </p>
 */
@Mapper
public interface DaySalesMapper extends BaseMapper<DaySalesEntity> {
    /**
     * <p>@Description 导出店铺日销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 15:19 </p>
     * @param userId 用户ID
     * @param isShop true查询店铺的日销售额，false查询店铺汇总的日销售额
     * @return 店铺月销售额
     */
    @RowLimitInterceptor
    List<DayDTO> exportDaySaleData(@Param("userId") String userId, @Param("isShop") boolean isShop);
    /**
     * <p>@Description 查询日期区间的日销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/28 21:46 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID 为空则查询用户店铺汇总日销售额，不为空则查询指定店铺日销售额
     * @param startData 起始日期
     * @param endDate 截止日期
     * @return 日销售额
     */
    List<DayDTO> findLastDaySales(@Param("userId") String userId,@Param("shopId") Long shopId,@Param("startData") String startData,@Param("endDate") String endDate);
    /**
     * <p>@Description 统计店铺月销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 15:19 </p>
     * @param userId 用户ID
     * @return 店铺月销售额
     */
    @RowLimitInterceptor
    List<MonthDTO> countShopMonthSale(@Param("userId") String userId);
    /**
     * <p>@Description 统计店铺汇总日销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 15:19 </p>
     * @param userId 用户ID
     * @return 店铺汇总日销售额
     */
    @RowLimitInterceptor
    List<DayDTO> countTotalDaySale(@Param("userId") String userId);
    /**
     * <p>@Description 根据用户ID查询用户店铺日销售额数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/26 19:20 </p>
     * @param userId 用户ID
     * @return 店铺日销售额entity数据
     */
    @RowLimitInterceptor
    List<DaySalesEntity> findShopDaySalesList(@Param("userId") String userId);
    /**
     * <p>@Description 根据用户ID查询用户店铺日汇总销售额数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/26 19:20 </p>
     * @param userId 用户ID
     * @return 店铺汇总日销售额entity数据
     */
    @RowLimitInterceptor
    List<DaySalesEntity> findTotalDaySalesList(@Param("userId") String userId);
    /**
     * <p>@Description 查询日销售额列表信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:55 </p>
     * @param page 分页信息
     * @param dto 查询条件
     * @return Page<ShopDTO>
     */
    Page<DayDTO> findDayList(Page<DayDTO> page, @Param("obj") DayDTO dto);
}
