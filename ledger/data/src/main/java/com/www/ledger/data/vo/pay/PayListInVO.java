package com.www.ledger.data.vo.pay;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * <p>@Description 查询支出信息列表的请求数据 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class PayListInVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 店铺ID **/
    private Long shopId;
    /** 支出日期起始日 **/
    private String strDat;
    /** 支出日期截止日 **/
    private String endDat;
    /** 支出类型 **/
    private String payTp;
    /** 当前页码 **/
    @Min(value = 1,message = "当前页码必须大于0")
    private int pageNum;
    /** 查询数量 **/
    @Min(value = 1,message = "查询数量必须大于0")
    private long pageSize;
}
