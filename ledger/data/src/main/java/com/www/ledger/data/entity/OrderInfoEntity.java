package com.www.ledger.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>@Description 订单信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
@TableName("ORDER_INFO")
public class OrderInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 订单主键ID **/
    @TableId(value = "OI_ID", type = IdType.AUTO)
    private Long oiId;
    /** 订单ID **/
    @TableField(value = "ORDER_ID")
    private String orderId;
    /** 店铺主键ID **/
    @TableField("SHOP_ID")
    private Long shopId;
    /** 用户名 **/
    @TableField("USER_ID")
    private String userId;
    /** 订单日期 **/
    @TableField("ORDER_DATE")
    private Date orderDate;
    /** 1688订单ID **/
    @TableField("SUPPLY_ID")
    private String supplyId;
    /** 商品ID **/
    @TableField("GOODS_ID")
    private String goodsId;
    /** 商品名称 **/
    @TableField("GOODS_NAME")
    private String goodsName;
    /** 订单状态(code=OrderState) **/
    @TableField("ORDER_STATE")
    private String orderState;
    /** 商品总价 **/
    @TableField("SALE_AMOUNT")
    private BigDecimal saleAmount;
    /** 实付金额 **/
    @TableField("PAYMENT_AMOUNT")
    private BigDecimal paymentAmount;
    /** 商品成本 **/
    @TableField("COST_AMOUNT")
    private BigDecimal costAmount;
    /** 其他支出 **/
    @TableField("PAYOUT_AMOUNT")
    private BigDecimal payoutAmount;
    /** 总成本 **/
    @TableField("TOTAL_COST")
    private BigDecimal totalCost;
    /** 毛利润 **/
    @TableField("GROSS_PROFIT")
    private BigDecimal grossProfit;
    /** 毛利率 **/
    @TableField("GROSS_PROFIT_RATE")
    private BigDecimal grossProfitRate;
    /** 备注 **/
    @TableField("REMARK")
    private String remark;
    /** 更新时间 **/
    @TableField("UPDATE_TIME")
    private Date updateTime;
    /** 创建时间 **/
    @TableField("CREATE_TIME")
    private Date createTime;
}
