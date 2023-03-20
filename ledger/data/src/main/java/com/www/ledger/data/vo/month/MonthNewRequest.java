package com.www.ledger.data.vo.month;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
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
public class MonthNewRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 月份Id **/
    private Long msId;
    /** 月份 **/
    @NotBlank(message = "月份不能为空")
    private String month;
    /** 店铺主键ID **/
    @NotNull(message = "店铺ID不能为空")
    private Long shopId;
    /** 推广费 **/
    @Digits(integer = 8,fraction = 2,message = "月推广费整数上限8位，小数位上限2位")
    @Min(value = 0,message = "月推广费不能为负数")
    private BigDecimal advAmt;
    /** 服务费 **/
    @Digits(integer = 8,fraction = 2,message = "月服务费整数上限8位，小数位上限2位")
    @Min(value = 0,message = "月服务费不能为负数")
    private BigDecimal serAmt;
}
