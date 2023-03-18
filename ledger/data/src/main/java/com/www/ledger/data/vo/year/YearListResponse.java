package com.www.ledger.data.vo.year;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>@Description 查询我的年销售额列表的响应数据 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class YearListResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 月份 **/
    private String year;
    /** 店铺名称 **/
    private String shopNm;
    /** 年净利润 **/
    private BigDecimal retPro;
    /** 年净利率 **/
    private BigDecimal retProRat;
    /** 年毛利润 **/
    private BigDecimal groPro;
    /** 年毛利率 **/
    private BigDecimal groProRat;
    /** 年订单数 **/
    private Long talOrd;
    /** 年成交单数 **/
    private Long sucOrd;
    /** 年失败单数 **/
    private Long faiOrd;
    /** 年销售额 **/
    private BigDecimal salAmt;
    /** 年成本费 **/
    private BigDecimal cosAmt;
    /** 年推广费 **/
    private BigDecimal advAmt;
    /** 年服务费 **/
    private BigDecimal serAmt;
    /** 年刷单费 **/
    private BigDecimal virAmt;
}
