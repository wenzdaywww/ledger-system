package com.www.ledger.data.vo.order;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * <p>@Description 查询订单信息列表的请求数据 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class OrderListInVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 店铺ID **/
    private Long shopId;
    /** 订单ID **/
    private String ordId;
    /** 订单日期起始日 **/
    private String strDat;
    /** 订单日期截止日 **/
    private String endDat;
    /** 1688订单 **/
    private String supId;
    /** 商品ID **/
    private String gdsId;
    /** 订单状态 **/
    private String ordSta;
    /** 备注 **/
    private String remark;
    /** 是否查询有备注 **/
    private Boolean rmk;
    /** 当前页码 **/
    @Min(value = 1,message = "当前页码必须大于0")
    private int pageNum;
    /** 查询数量 **/
    @Min(value = 1,message = "查询数量必须大于0")
    private long pageSize;
}
