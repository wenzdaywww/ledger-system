package com.www.ledger.data.vo.pay;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>@Description 查询支出信息列表的响应数据 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class PayListOutVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 支出主键ID **/
    private Long payId;
    /** 支出日期 **/
    private String payDat;
    /** 店铺ID **/
    private Long shopId;
    /** 店铺名称 **/
    private String shopNm;
    /** 支出类型 **/
    private String payTp;
    /** 支出类型名称 **/
    private String payNm;
    /** 支出金额 **/
    private BigDecimal payAmt;
    /** 备注 **/
    private String remark;
}
