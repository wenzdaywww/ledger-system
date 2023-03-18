package com.www.ledger.data.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>@Description 年销售额信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class YearDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 年账单主键ID **/
    private Long ysId;
    /** 店铺主键ID **/
    private Long shopId;
    /** 店铺名称 **/
    private String shopName;
    /** 用户名 **/
    private String userId;
    /** 账单年份 **/
    private Date year;
    /** 账单年份 **/
    private String yearStr;
    /** 年净利润 **/
    private BigDecimal retainedProfits;
    /** 年净利率 **/
    private BigDecimal retainedProfitsRate;
    /** 年毛利润 **/
    private BigDecimal grossProfit;
    /** 年毛利率 **/
    private BigDecimal grossProfitRate;
    /** 年订单数 **/
    private Long totalOrder;
    /** 年成交单数 **/
    private Long succeedOrder;
    /** 年失败单数 **/
    private Long failedOrder;
    /** 年销售额 **/
    private BigDecimal saleAmount;
    /** 年成本费 **/
    private BigDecimal costAmount;
    /** 年推广费 **/
    private BigDecimal advertAmount;
    /** 年服务费 **/
    private BigDecimal serviceAmount;
    /** 年刷单费 **/
    private BigDecimal virtualAmount;
}
