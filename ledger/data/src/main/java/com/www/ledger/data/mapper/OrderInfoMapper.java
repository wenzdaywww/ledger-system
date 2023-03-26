package com.www.ledger.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.ledger.data.dto.DayDTO;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.dto.OrderDTO;
import com.www.ledger.data.entity.OrderInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>@Description 订单信息Mapper </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 19:43 </p>
 */
@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfoEntity> {
    /**
     * <p>@Description 查询销量店铺近些日的销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 19:19 </p>
     * @param userId 用户ID
     * @param shopList 待统计的店铺ID
     * @param minDate 订单统计起始日期
     * @param maxDate 订单统计截止日期
     * @return 店铺近些日的销售额
     */
    List<OrderDTO> findMaxSalesOrder(@Param("userId") String userId, @Param("shop") List<Long> shopList, @Param("minDt") String minDate, @Param("maxDt") String maxDate);
    /**
     * <p>@Description 获取订单中最大的日期 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 19:08 </p>
     * @param userId 用户ID
     * @return 最大的日期
     */
    String getMaxOrderDate(@Param("userId") String userId);
    /**
     * <p>@Description 统计日销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 15:19 </p>
     * @param userId 用户ID
     * @return
     */
    List<DayDTO> countDaySale(@Param("userId") String userId);
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
