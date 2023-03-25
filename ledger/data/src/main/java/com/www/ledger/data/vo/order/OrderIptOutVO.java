package com.www.ledger.data.vo.order;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>@Description 导入订单文件的响应数据 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class OrderIptOutVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 订单ID **/
    private String ordId;
    /** 错误信息 **/
    private String msg;
}
