package com.www.ledger.data.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>@Description 日销售额信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class DayDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 日账单主键ID **/
    private Long dsId;
    /** 店铺主键ID **/
    private Long shopId;
    /** 是否查询所有店铺的日销售额 **/
    private Boolean allShop;
    /** 店铺名称 **/
    private String shopName;
    /** 用户名 **/
    private String userId;
    /** 账单日期 **/
    private Date dayDate;
    /** 账单日期 **/
    private String dayDateStr;
    /** 订单日期起始日 **/
    private String startDateStr;
    /** 订单日期截止日 **/
    private String endDateStr;
    /** 日净利润 **/
    private BigDecimal retainedProfits;
    /** 日净利率 **/
    private BigDecimal retainedProfitsRate;
    /** 日毛利润 **/
    private BigDecimal grossProfit;
    /** 日毛利率 **/
    private BigDecimal grossProfitRate;
    /** 日订单数 **/
    private Long totalOrder;
    /** 日成交单数 **/
    private Long succeedOrder;
    /** 日失败单数 **/
    private Long failedOrder;
    /** 日销售额 **/
    private BigDecimal saleAmount;
    /** 日成本费 **/
    private BigDecimal costAmount;
    /** 日刷单费 **/
    private BigDecimal virtualAmount;
    /** 日支出费 **/
    private BigDecimal totalCost;
    /** 更新时间 **/
    private Date updateTime;
    /** 创建时间 **/
    private Date createTime;
}
