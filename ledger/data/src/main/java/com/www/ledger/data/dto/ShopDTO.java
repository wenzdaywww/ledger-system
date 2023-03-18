package com.www.ledger.data.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>@Description 店铺信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class ShopDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 店铺主键ID **/
    private Long shopId;
    /** 店铺名称 **/
    private String shopName;
    /** 店铺平台 **/
    private String shopType;
    /** 用户名 **/
    private String userId;
    /** 店净利润 **/
    private BigDecimal retainedProfits;
    /** 店净利率 **/
    private BigDecimal retainedProfitsRate;
    /** 店毛利润 **/
    private BigDecimal grossProfit;
    /** 店毛利率 **/
    private BigDecimal grossProfitRate;
    /** 店订单数 **/
    private Long totalOrder;
    /** 店成交单数 **/
    private Long succeedOrder;
    /** 店失败单数 **/
    private Long failedOrder;
    /** 店销售额 **/
    private BigDecimal saleAmount;
    /** 店成本费 **/
    private BigDecimal costAmount;
    /** 店推广费 **/
    private BigDecimal advertAmount;
    /** 店服务费 **/
    private BigDecimal serviceAmount;
    /** 店刷单费 **/
    private BigDecimal virtualAmount;
}
