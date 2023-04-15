package com.www.ledger.service.chart;

import com.www.common.data.response.Result;
import com.www.ledger.data.dto.ChartDataDTO;
import com.www.ledger.data.dto.ChartPieDTO;
import com.www.ledger.data.enums.ChartEnum;

import java.util.List;

/**
 * <p>@Description 图表数据业务处理接口 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/26 18:06 </p>
 */
public interface IChartService {
    /**
     * <p>@Description 查询日期范围内日销售图表数据，最大范围30天 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param startDate 起始日期
     * @param endDate 截止日期
     * @param chartList 图表数据枚举值集合
     * @return 日销售图表数据
     */
    Result<ChartDataDTO> findDayData(String userId, Long shopId, String startDate, String endDate, List<ChartEnum> chartList);
    /**
     * <p>@Description 查询日期范围内订单状态饼状图表数据，最大范围60天 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param startDate 起始日期
     * @param endDate 截止日期
     * @return 日订单状态饼状图表数据
     */
    Result<ChartPieDTO> findDayOrderState(String userId, Long shopId, String startDate, String endDate);
    /**
     * <p>@Description 查询日期范围内订单状态饼状图表数据，最大范围1年 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param startDateStr 起始日期
     * @param endDateStr 截止日期
     * @return 月订单状态饼状图表数据
     */
    Result<ChartPieDTO> findMonthOrderState(String userId, Long shopId, String startDateStr, String endDateStr);
    /**
     * <p>@Description 查询日期范围内订单状态饼状图表数据，最大范围10年 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param startDateStr 起始日期
     * @param endDateStr 截止日期
     * @return 年订单状态饼状图表数据
     */
    Result<ChartPieDTO> findYearOrderState(String userId, Long shopId, String startDateStr, String endDateStr);
    /**
     * <p>@Description 查询月销售图表数据，最大范围12个月 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param startData 起始日期
     * @param endDate 截止日期
     * @param chartList 图表数据枚举值集合
     * @return 月销售图表数据
     */
    Result<ChartDataDTO> findMonthData(String userId, Long shopId,String startData, String endDate,  List<ChartEnum> chartList);
    /**
     * <p>@Description 查询年利润图表数据，最大范围10年 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param startDate 起始日期
     * @param endDate 截止日期
     * @param chartList 图表数据枚举值集合
     * @return 年利润图表数据
     */
    Result<ChartDataDTO> findYearData(String userId, Long shopId,String startDate, String endDate,  List<ChartEnum> chartList);
}
