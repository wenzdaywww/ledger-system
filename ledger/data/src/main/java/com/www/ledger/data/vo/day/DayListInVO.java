package com.www.ledger.data.vo.day;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * <p>@Description 查询我的日销售额列表的请求数据 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class DayListInVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 订单日期起始日 **/
    private String strDat;
    /** 订单日期截止日 **/
    private String endDat;
    /** 店铺主键ID **/
    private Long shopId;
    /** 是否查询所有店铺的日销售额 **/
    private Boolean all;
    /** 当前页面 **/
    @Min(value = 1,message = "当前页码必须大于0")
    private int pageNum;
    /** 查询数量 **/
    @Min(value = 1,message = "查询数量必须大于0")
    private long pageSize;
}
