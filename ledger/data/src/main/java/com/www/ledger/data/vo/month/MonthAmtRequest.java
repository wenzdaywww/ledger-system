package com.www.ledger.data.vo.month;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>@Description 编辑月销售额数据的请求数据 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class MonthAmtRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 月销售ID **/
    @NotNull(message = "月销售ID不能为空")
    private Long msId;
    /** 操作类型：1增加，0减少 **/
    @NotBlank(message = "操作类型不能为空")
    private String type;
    /** 推广费步进值 **/
    @Digits(integer = 8,fraction = 2,message = "推广费整数上限8位，小数位上限2位")
    private BigDecimal advStep;
    /** 服务费步进值 **/
    @Digits(integer = 8,fraction = 2,message = "服务费整数上限8位，小数位上限2位")
    private BigDecimal serStep;
}
