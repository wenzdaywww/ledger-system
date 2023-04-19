package com.www.ledger.data.dto;

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
public class YearDTO extends BaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 年账单主键ID **/
    private Long ysId;
    /** 店铺主键ID **/
    private Long shopId;
    /** 是否查询所有店铺的年销售额 **/
    private Boolean allShop;
    /** 店铺名称 **/
    private String shopName;
    /** 账单年份 **/
    private Date year;
    /** 账单年份 **/
    private String yearStr;
}
