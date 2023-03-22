package com.www.ledger.data.vo.shop;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>@Description 查询我的店铺信息列表的响应数据 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class ShopListOutVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 店铺主键ID **/
    private Long shopId;
    /** 店铺名称 **/
    private String shopNm;
    /** 店铺平台 **/
    private String shopTp;
    /** 店铺平台名称 **/
    private String shopTpNm;
    /** 店净利润 **/
    private BigDecimal retPro;
    /** 店净利率 **/
    private BigDecimal retProRat;
    /** 店毛利润 **/
    private BigDecimal groPro;
    /** 店毛利率 **/
    private BigDecimal groProRat;
    /** 店订单数 **/
    private Long talOrd;
    /** 店成交单数 **/
    private Long sucOrd;
    /** 店失败单数 **/
    private Long faiOrd;
    /** 店销售额 **/
    private BigDecimal salAmt;
    /** 店成本费 **/
    private BigDecimal cosAmt;
    /** 店推广费 **/
    private BigDecimal advAmt;
    /** 店服务费 **/
    private BigDecimal serAmt;
    /** 店刷单费 **/
    private BigDecimal virAmt;
    /** 店支出费 **/
    private BigDecimal talCos;
}
