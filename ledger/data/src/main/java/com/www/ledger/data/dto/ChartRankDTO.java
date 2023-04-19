package com.www.ledger.data.dto;

import com.www.ledger.data.enums.ChartEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>@Description 图表排行榜数据查询响应数据 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/19 15:05 </p>
 */
@Data
@Accessors(chain = true)
public class ChartRankDTO {
    /** 店铺名称 **/
    private String shopName;
    /** 图表排行榜y坐标名称集合 **/
    private List<String> yaxis;
    /** 图表排行榜数据 **/
    private List<Series> series;
    /**
     * <p>@Description 图表排行榜数据 </p>
     * <p>@Version 1.0 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/31 23:14 </p>
     */
    @Data
    @Accessors(chain = true)
    public static class Series {
        /** 数据名称枚举值 **/
        private ChartEnum enums;
        /** 数据名称 **/
        private String name;
        /** 数据数值 **/
        private List<BigDecimal> data;
    }
}
