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
 * <p>@Description 店铺销售额信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
@TableName("SHOP_SALES")
public class ShopSalesEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 用户账簿主键ID **/
    @TableId(value = "SS_ID", type = IdType.AUTO)
    private Long ssId;
    /** 店铺主键ID **/
    @TableField("SHOP_ID")
    private Long shopId;
    /** 用户名 **/
    @TableField("USER_ID")
    private String userId;
    /** 店净利润 **/
    @TableField("RETAINED_PROFITS")
    private BigDecimal retainedProfits;
    /** 店净利率 **/
    @TableField("RETAINED_PROFITS_RATE")
    private BigDecimal retainedProfitsRate;
    /** 店毛利润 **/
    @TableField("GROSS_PROFIT")
    private BigDecimal grossProfit;
    /** 店毛利率 **/
    @TableField("GROSS_PROFIT_RATE")
    private BigDecimal grossProfitRate;
    /** 店订单数 **/
    @TableField("TOTAL_ORDER")
    private Long totalOrder;
    /** 店成交单数 **/
    @TableField("SUCCEED_ORDER")
    private Long succeedOrder;
    /** 店失败单数 **/
    @TableField("FAILED_ORDER")
    private Long failedOrder;
    /** 店销售额 **/
    @TableField("SALE_AMOUNT")
    private BigDecimal saleAmount;
    /** 店成本费 **/
    @TableField("COST_AMOUNT")
    private BigDecimal costAmount;
    /** 店推广费 **/
    @TableField("ADVERT_AMOUNT")
    private BigDecimal advertAmount;
    /** 店服务费 **/
    @TableField("SERVICE_AMOUNT")
    private BigDecimal serviceAmount;
    /** 店刷单费 **/
    @TableField("VIRTUAL_AMOUNT")
    private BigDecimal virtualAmount;
    /** 店支出费 **/
    @TableField("TOTAL_COST")
    private BigDecimal totalCost;
    /** 更新时间 **/
    @TableField("UPDATE_TIME")
    private Date updateTime;
    /** 创建时间 **/
    @TableField("CREATE_TIME")
    private Date createTime;
}
