package com.www.ledger.data.vo.book;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>@Description 查询用户账簿信息的响应数据 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class BookInfoOutVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 总净利润 **/
    private BigDecimal retPro;
    /** 总净利率 **/
    private BigDecimal retProRat;
    /** 总毛利润 **/
    private BigDecimal groPro;
    /** 总毛利率 **/
    private BigDecimal groProRat;
    /** 总订单数 **/
    private Long talOrd;
    /** 总成交单数 **/
    private Long sucOrd;
    /** 订单成交率 **/
    private BigDecimal sucOrdRat;
    /** 总失败单数 **/
    private Long faiOrd;
    /** 总销售额 **/
    private BigDecimal salAmt;
    /** 总成本费 **/
    private BigDecimal cosAmt;
    /** 总推广费 **/
    private BigDecimal advAmt;
    /** 总服务费 **/
    private BigDecimal serAmt;
    /** 总刷单费 **/
    private BigDecimal virAmt;
    /** 总支出费 **/
    private BigDecimal talCos;
    /** 店铺数 **/
    private int shopNum;
    /** 商品数 **/
    private int gdsNum;
}
