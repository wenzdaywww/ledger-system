package com.www.ledger.service.chart;

import com.www.common.data.response.Result;
import com.www.ledger.data.dto.ChartDataDTO;
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
     * <p>@Description 查询日销售图表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param days 查询的日期区间天数
     * @param chartList 图表数据枚举值集合
     * @return 日销售图表数据
     */
    Result<ChartDataDTO> findDayData(String userId, Long shopId, int days, List<ChartEnum> chartList);
    /**
     * <p>@Description 查询月销售图表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param chartList 图表数据枚举值集合
     * @return 月销售图表数据
     */
    Result<ChartDataDTO> findMonthData(String userId, Long shopId, List<ChartEnum> chartList);
    /**
     * <p>@Description 查询年利润图表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param chartList 图表数据枚举值集合
     * @return 年利润图表数据
     */
    Result<ChartDataDTO> findYearData(String userId, Long shopId, List<ChartEnum> chartList);
}
