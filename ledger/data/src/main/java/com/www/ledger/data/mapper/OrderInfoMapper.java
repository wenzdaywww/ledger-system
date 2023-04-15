package com.www.ledger.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.common.config.mybatis.annotation.RowLimitInterceptor;
import com.www.ledger.data.dto.DayDTO;
import com.www.ledger.data.dto.OrderDTO;
import com.www.ledger.data.entity.OrderInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>@Description 订单信息Mapper </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 19:43 </p>
 */
@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfoEntity> {
    /**
     * <p>@Description 查询日期范围内订单状态饼状图表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 22:27 </p>
     * @param userId    用户ID
     * @param shopId    店铺ID 为空则查询用户店铺汇总日销售额，不为空则查询指定店铺日销售额
     * @param startDate 起始日期
     * @param endDate   截止日期
     * @return 日订单状态饼状图表数据
     */
    List<OrderDTO> findDayOrderState(@Param("userId") String userId, @Param("shopId") Long shopId, @Param("startDate") String startDate, @Param("endDate") String endDate);
    /**
     * <p>@Description 导出订单信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 22:27 </p>
     * @param userId 用户ID
     * @return 订单信息
     */
    @RowLimitInterceptor
    List<OrderDTO> exportOrderData(@Param("userId") String userId);
    /**
     * <p>@Description 获取订单中最小的日期和最大的日期 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 19:08 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID 为空则查询用户下所有店铺订单，不为空则查询指定店铺下订单
     * @return 最小的日期和最大的日期
     */
    Map<String,Date> getMinMaxOrderDate(@Param("userId") String userId, @Param("shopId") Long shopId);
    /**
     * <p>@Description 统计店铺日销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 15:19 </p>
     * @param userId 用户ID
     * @return 店铺日销售额
     */
    @RowLimitInterceptor
    List<DayDTO> countShopDaySale(@Param("userId") String userId);
    /**
     * <p>@Description 查询订单信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:55 </p>
     * @param page 分页信息
     * @param dto 订单信息查询条件
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.www.ledger.data.dto.ShopDTO>
     */
    Page<OrderDTO> findOrdeList(Page<OrderDTO> page, @Param("obj") OrderDTO dto);
}
