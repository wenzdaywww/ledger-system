package com.www.ledger.data.vo.chart;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>@Description 图表数据查询响应数据 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/19 15:05 </p>
 */
@Data
@Accessors(chain = true)
public class ChartDataOutVO {
    /** 起始日期 **/
    private String startDate;
    /** 截止日期 **/
    private String endDate;
    /** 图表x坐标名称集合 **/
    private List<String> xaxis;
    /** 图表数据 **/
    private List<Series> series;
    /**
     * <p>@Description 图表数据 </p>
     * <p>@Version 1.0 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/31 23:14 </p>
     */
    @Data
    @Accessors(chain = true)
    public static class Series {
        /** 数据名称 **/
        private String name;
        /** 数据数值 **/
        private List<BigDecimal> data;
    }
}
