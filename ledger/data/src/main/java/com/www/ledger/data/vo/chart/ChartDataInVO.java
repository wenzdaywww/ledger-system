package com.www.ledger.data.vo.chart;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>@Description 图表数据查询请求数据 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/19 15:05 </p>
 */
@Data
@Accessors(chain = true)
public class ChartDataInVO {
    /** 店铺ID **/
    private Long shopId;
    /** 起始日期 **/
    private String startDate;
    /** 截止日期 **/
    private String endDate;
}
