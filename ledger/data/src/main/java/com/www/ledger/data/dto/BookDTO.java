package com.www.ledger.data.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>@Description 用户账簿信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class BookDTO extends BaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 用户账簿主键ID **/
    private Long ubId;
    /** 订单成交率**/
    private BigDecimal succeedOrderRate;
    /** 总店铺保证金 **/
    private BigDecimal guarantee;
}
