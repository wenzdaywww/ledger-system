package com.www.ledger.data.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>@Description 销售数据共有字段信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class BaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 标签名称 **/
    private String label;
    /** 用户名 **/
    private String userId;
    /** 店铺名称 **/
    private String shopName;
    /** 净利润 **/
    private BigDecimal retainedProfits;
    /** 净利率 **/
    private BigDecimal retainedProfitsRate;
    /** 毛利润 **/
    private BigDecimal grossProfit;
    /** 毛利率 **/
    private BigDecimal grossProfitRate;
    /** 订单数 **/
    private Long totalOrder;
    /** 成交单数 **/
    private Long succeedOrder;
    /** 失败单数 **/
    private Long failedOrder;
    /** 销售额 **/
    private BigDecimal saleAmount;
    /** 成本费 **/
    private BigDecimal costAmount;
    /** 推广费 **/
    private BigDecimal advertAmount;
    /** 服务费 **/
    private BigDecimal serviceAmount;
    /** 刷单费 **/
    private BigDecimal virtualAmount;
    /** 支出费 **/
    private BigDecimal totalCost;
    /** 更新时间 **/
    private Date updateTime;
    /** 创建时间 **/
    private Date createTime;
}
