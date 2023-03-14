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
 * <p>@Description 商品样式信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
@TableName("GOODS_STYLE")
public class GoodsStyleEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 商品样式主键ID **/
    @TableId(value = "STYLE_ID", type = IdType.AUTO)
    private Long styleId;
    /** 商品样式名称 **/
    @TableField("STYLE_NAME")
    private String styleName;
    /** 商品ID **/
    @TableField("GOODS_ID")
    private Long goodsId;
    /** 店铺主键ID **/
    @TableField("SHOP_ID")
    private Long shopId;
    /** 用户名 **/
    @TableField("USER_ID")
    private String userId;
    /** 价格 **/
    @TableField("SALE_AMOUNT")
    private BigDecimal saleAmount;
    /** 成本 **/
    @TableField("COST_AMOUNT")
    private BigDecimal costAmount;
    /** 毛利润 **/
    @TableField("GROSS_PROFIT")
    private BigDecimal grossProfit;
    /** 毛利率 **/
    @TableField("GROSS_PROFIT_RATE")
    private BigDecimal grossProfitRate;
    /** 更新时间 **/
    @TableField("UPDATE_TIME")
    private Date updateTime;
    /** 创建时间 **/
    @TableField("CREATE_TIME")
    private Date createTime;
}
