package com.www.ledger.data.vo.book;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>@Description 最近一年销售趋势图查询的响应数据 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/19 15:05 </p>
 */
@Data
@Accessors(chain = true)
public class BookDayOutVO {
    /** 日期 **/
    private String day;
    /** 日销售额 **/
    private BigDecimal sales;
}
