package com.www.ledger.data.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>@Description 订单导入的订单行数据DTO </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class OrderRowDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 订单数据所在文件行号 **/
    private int rowNum;
    /** 订单数据文件最大列号 **/
    private int maxColumn;
    /** 订单ID **/
    private String orderId;
    /** 店铺主键ID **/
    private Long shopId;
    /** 用户名 **/
    private String userId;
    /** 订单日期 **/
    private Date orderDate;
    /** 1688订单ID **/
    private String supplyId;
    /** 商品ID **/
    private String goodsId;
    /** 商品名称 **/
    private String goodsName;
    /** 订单状态(code=OrderState) **/
    private String orderState;
    /** 商品总价 **/
    private BigDecimal saleAmount;
    /** 实付金额 **/
    private BigDecimal paymentAmount;
    /** 商品成本 **/
    private BigDecimal costAmount;
    /** 备注 **/
    private String remark;
    /** 错误信息 **/
    private String message;
}
