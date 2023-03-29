package com.www.ledger.data.dto;

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
public class PayDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 支出主键ID **/
    private Long payId;
    /** 店铺主键ID **/
    private Long shopId;
    /** 店铺名称 **/
    private String shopName;
    /** 用户名 **/
    private String userId;
    /** 支出日期 **/
    private Date payDate;
    /** 支出日期 **/
    private String payDateStr;
    /** 支出日期起始日 **/
    private String startDate;
    /** 支出日期截止日 **/
    private String endDate;
    /** 支出类型 **/
    private String payType;
    /** 支出类型名称 **/
    private String payTypeName;
    /** 支出金额 **/
    private BigDecimal payAmount;
    /** 备注 **/
    private String remark;
    /** 更新时间 **/
    private Date updateTime;
    /** 创建时间 **/
    private Date createTime;
}
