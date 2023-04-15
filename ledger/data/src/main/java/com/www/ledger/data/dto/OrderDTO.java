package com.www.ledger.data.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>@Description 订单信息DTO </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class OrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 订单主键ID **/
    private Long oiId;
    /** 订单ID **/
    private String orderId;
    /** 店铺主键ID **/
    private Long shopId;
    /** 店铺名 **/
    private String shopName;
    /** 用户名 **/
    private String userId;
    /** 订单日期 **/
    private Date orderDate;
    /** 订单日期(字符串) **/
    private String orderDateStr;
    /** 订单日期起始日 **/
    private String startDateStr;
    /** 订单日期截止日 **/
    private String endDateStr;
    /** 1688订单ID **/
    private String supplyId;
    /** 商品ID **/
    private String goodsId;
    /** 商品名称 **/
    private String goodsName;
    /** 商品链接 **/
    private String goodsUrl;
    /** 订单状态(code=OrderState) **/
    private String orderState;
    /** 订单状态名称 **/
    private String orderStateName;
    /** 商品总价 **/
    private BigDecimal saleAmount;
    /** 实付金额 **/
    private BigDecimal paymentAmount;
    /** 商品成本 **/
    private BigDecimal costAmount;
    /** 其他支出 **/
    private BigDecimal payoutAmount;
    /** 总成本 **/
    private BigDecimal totalCost;
    /** 毛利润 **/
    private BigDecimal grossProfit;
    /** 毛利率 **/
    private BigDecimal grossProfitRate;
    /** 备注 **/
    private String remark;
    /** 行号 **/
    private int rowNum;
    /** 错误信息 **/
    private String message;
    /** 日订单数 **/
    private Long totalOrder;
}
