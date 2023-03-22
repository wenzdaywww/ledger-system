package com.www.ledger.data.vo.order;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>@Description 查询订单信息列表的响应数据 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class OrderListOutVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 订单主键ID **/
    private Long oiId;
    /** 订单ID **/
    private String ordId;
    /** 店铺ID **/
    private Long shopId;
    /** 店铺名称 **/
    private String shopNm;
    /** 订单日期 **/
    private String ordDat;
    /** 1688订单 **/
    private String supId;
    /** 商品ID **/
    private String gdsId;
    /** 商品名称 **/
    private String gdsName;
    /** 商品链接 **/
    private String url;
    /** 订单状态 **/
    private String ordSta;
    /** 订单状态名称 **/
    private String ordStaNm;
    /** 商品总价 **/
    private BigDecimal salAmt;
    /** 实付金额 **/
    private BigDecimal payAmt;
    /** 商品成本 **/
    private BigDecimal cosAmt;
    /** 其他支出 **/
    private BigDecimal othAmt;
    /** 总成本 **/
    private BigDecimal talCos;
    /** 毛利润 **/
    private BigDecimal groPro;
    /** 毛利率 **/
    private BigDecimal groProRat;
    /** 备注 **/
    private String remark;
}
