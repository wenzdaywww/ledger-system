package com.www.ledger.data.vo.pay;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>@Description 保存支出信息的请求数据 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class PayInfoInVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 支出主键ID **/
    private Long payId;
    /** 支出日期 **/
    @NotBlank(message = "支出日期不能为空")
    private String payDat;
    /** 店铺ID **/
    @NotNull(message = "店铺ID不能为空")
    private Long shopId;
    /** 支出类型 **/
    @NotBlank(message = "支出类型不能为空")
    private String payTp;
    /** 支出金额 **/
    @Digits(integer = 8,fraction = 2,message = "支出金额整数上限8位，小数位上限2位")
    private BigDecimal payAmt;
    /** 备注 **/
    @Length(max = 100,message = "备注字数最大100")
    private String remark;
}
