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
 * <p>@Description 年销售额信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
@TableName("YEAR_SALES")
public class YearSalesEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 年账单主键ID **/
    @TableId(value = "YS_ID", type = IdType.AUTO)
    private Long ysId;
    /** 店铺主键ID **/
    @TableField("SHOP_ID")
    private Long shopId;
    /** 用户名 **/
    @TableField("USER_ID")
    private String userId;
    /** 账单年份 **/
    @TableField("MONTH_DATE")
    private Date monthDate;
    /** 年净利润 **/
    @TableField("RETAINED_PROFITS")
    private BigDecimal retainedProfits;
    /** 年净利率 **/
    @TableField("RETAINED_PROFITS_RATE")
    private BigDecimal retainedProfitsRate;
    /** 年毛利润 **/
    @TableField("GROSS_PROFIT")
    private BigDecimal grossProfit;
    /** 年毛利率 **/
    @TableField("GROSS_PROFIT_RATE")
    private BigDecimal grossProfitRate;
    /** 年订单数 **/
    @TableField("TOTAL_ORDER")
    private Long totalOrder;
    /** 年成交单数 **/
    @TableField("SUCCEED_ORDER")
    private Long succeedOrder;
    /** 年失败单数 **/
    @TableField("FAILED_ORDER")
    private Long failedOrder;
    /** 年销售额 **/
    @TableField("SALE_AMOUNT")
    private BigDecimal saleAmount;
    /** 年成本费 **/
    @TableField("COST_AMOUNT")
    private BigDecimal costAmount;
    /** 年推广费 **/
    @TableField("ADVERT_AMOUNT")
    private BigDecimal advertAmount;
    /** 年服务费 **/
    @TableField("SERVICE_AMOUNT")
    private BigDecimal serviceAmount;
    /** 年刷单费 **/
    @TableField("VIRTUAL_AMOUNT")
    private BigDecimal virtualAmount;
    /** 更新时间 **/
    @TableField("UPDATE_TIME")
    private Date updateTime;
    /** 创建时间 **/
    @TableField("CREATE_TIME")
    private Date createTime;
}
