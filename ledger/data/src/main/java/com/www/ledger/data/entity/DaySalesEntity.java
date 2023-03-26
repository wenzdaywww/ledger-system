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
 * <p>@Description 日销售额信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
@TableName("DAY_SALES")
public class DaySalesEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 日账单主键ID **/
    @TableId(value = "DS_ID", type = IdType.AUTO)
    private Long dsId;
    /** 店铺主键ID **/
    @TableField("SHOP_ID")
    private Long shopId;
    /** 用户名 **/
    @TableField("USER_ID")
    private String userId;
    /** 账单日期 **/
    @TableField("DAY_DATE")
    private Date dayDate;
    /** 日净利润 **/
    @TableField("RETAINED_PROFITS")
    private BigDecimal retainedProfits;
    /** 日净利率 **/
    @TableField("RETAINED_PROFITS_RATE")
    private BigDecimal retainedProfitsRate;
    /** 日毛利润 **/
    @TableField("GROSS_PROFIT")
    private BigDecimal grossProfit;
    /** 日毛利率 **/
    @TableField("GROSS_PROFIT_RATE")
    private BigDecimal grossProfitRate;
    /** 日订单数 **/
    @TableField("TOTAL_ORDER")
    private Long totalOrder;
    /** 日成交单数 **/
    @TableField("SUCCEED_ORDER")
    private Long succeedOrder;
    /** 日失败单数 **/
    @TableField("FAILED_ORDER")
    private Long failedOrder;
    /** 日销售额 **/
    @TableField("SALE_AMOUNT")
    private BigDecimal saleAmount;
    /** 日成本费 **/
    @TableField("COST_AMOUNT")
    private BigDecimal costAmount;
    /** 日刷单费 **/
    @TableField("VIRTUAL_AMOUNT")
    private BigDecimal virtualAmount;
    /** 日支出费 **/
    @TableField("TOTAL_COST")
    private BigDecimal totalCost;
    /** 更新时间 **/
    @TableField("UPDATE_TIME")
    private Date updateTime;
    /** 创建时间 **/
    @TableField("CREATE_TIME")
    private Date createTime;
}
