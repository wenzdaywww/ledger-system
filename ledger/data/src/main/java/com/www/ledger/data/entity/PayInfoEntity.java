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
 * <p>@Description 支出信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
@TableName("PAY_INFO")
public class PayInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 支出主键ID **/
    @TableId(value = "PAY_ID", type = IdType.AUTO)
    private Long payId;
    /** 店铺主键ID **/
    @TableField("SHOP_ID")
    private Long shopId;
    /** 用户名 **/
    @TableField("USER_ID")
    private String userId;
    /** 支出日期 **/
    @TableField("PAY_DATE")
    private Date payDate;
    /** 支出类型 **/
    @TableField("PAY_TYPE")
    private String payType;
    /** 支出金额 **/
    @TableField("PAY_AMOUNT")
    private BigDecimal payAmount;
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
