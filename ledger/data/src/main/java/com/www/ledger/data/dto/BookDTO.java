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
public class BookDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 用户账簿主键ID **/
    private Long ubId;
    /** 用户名 **/
    private String userId;
    /** 净利润 **/
    private BigDecimal retainedProfits;
    /** 净利率 **/
    private BigDecimal retainedProfitsRate;
    /** 毛利润 **/
    private BigDecimal grossProfit;
    /** 毛利率 **/
    private BigDecimal grossProfitRate;
    /** 总订单数 **/
    private Long totalOrder;
    /** 总成功单数 **/
    private Long succeedOrder;
    /** 订单成交率**/
    private BigDecimal succeedOrderRate;
    /** 总失败单数 **/
    private Long failedOrder;
    /** 总销售额 **/
    private BigDecimal saleAmount;
    /** 总成本费 **/
    private BigDecimal costAmount;
    /** 总推广费 **/
    private BigDecimal advertAmount;
    /** 总服务费 **/
    private BigDecimal serviceAmount;
    /** 总刷单费 **/
    private BigDecimal virtualAmount;
    /** 店铺数 **/
    private int shopNum;
    /** 商品数 **/
    private int goodsNum;
}
