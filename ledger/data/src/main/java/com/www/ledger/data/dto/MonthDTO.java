package com.www.ledger.data.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>@Description 月销售额信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class MonthDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 月账单主键ID **/
    private Long msId;
    /** 店铺主键ID **/
    private Long shopId;
    /** 是否查询所有店铺的月销售额 **/
    private Boolean allShop;
    /** 店铺名称 **/
    private String shopName;
    /** 用户名 **/
    private String userId;
    /** 账单月份 **/
    private Date monthDate;
    /** 账单月份 **/
    private String monthDateStr;
    /** 月净利润 **/
    private BigDecimal retainedProfits;
    /** 月净利率 **/
    private BigDecimal retainedProfitsRate;
    /** 月毛利润 **/
    private BigDecimal grossProfit;
    /** 月毛利率 **/
    private BigDecimal grossProfitRate;
    /** 月订单数 **/
    private Long totalOrder;
    /** 月成交单数 **/
    private Long succeedOrder;
    /** 月失败单数 **/
    private Long failedOrder;
    /** 月销售额 **/
    private BigDecimal saleAmount;
    /** 月成本费 **/
    private BigDecimal costAmount;
    /** 月推广费 **/
    private BigDecimal advertAmount;
    /** 月服务费 **/
    private BigDecimal serviceAmount;
    /** 月刷单费 **/
    private BigDecimal virtualAmount;
    /** 月支出费 **/
    private BigDecimal totalCost;
    /** 更新时间 **/
    private Date updateTime;
    /** 创建时间 **/
    private Date createTime;
}
