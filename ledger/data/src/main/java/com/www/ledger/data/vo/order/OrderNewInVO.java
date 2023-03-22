package com.www.ledger.data.vo.order;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>@Description 保存订单信息的请求数据 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class OrderNewInVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 订单主键ID **/
    private Long oiId;
    /** 订单ID **/
    @Length(max = 40,message = "订单ID字数最大40")
    private String ordId;
    /** 店铺主键ID **/
    @NotNull(message = "店铺ID不能为空")
    private Long shopId;
    /** 订单日期 **/
    @NotBlank(message = "订单日期不能为空")
    private String ordDat;
    /** 1688订单ID **/
    @Length(max = 40,message = "1688订单ID字数最大40")
    private String supId;
    /** 商品ID **/
    @Length(max = 40,message = "商品ID字数最大40")
    private String gdsId;
    /** 商品名称 **/
    @Length(max = 60,message = "商品名称字数最大60")
    private String gdsName;
    /** 订单状态(code=OrderState) **/
    @NotBlank(message = "订单状态不能为空")
    private String ordSta;
    /** 商品总价 **/
    @Digits(integer = 8,fraction = 2,message = "商品总价整数上限8位，小数位上限2位")
    private BigDecimal salAmt;
    /** 实付金额 **/
    @Digits(integer = 8,fraction = 2,message = "实付金额整数上限8位，小数位上限2位")
    private BigDecimal payAmt;
    /** 商品成本 **/
    @Digits(integer = 8,fraction = 2,message = "商品成本整数上限8位，小数位上限2位")
    private BigDecimal cosAmt;
    /** 其他支出 **/
    @Digits(integer = 8,fraction = 2,message = "其他支出整数上限8位，小数位上限2位")
    private BigDecimal othAmt;
    /** 备注 **/
    @Length(max = 100,message = "备注字数最大100")
    private String remark;
}
