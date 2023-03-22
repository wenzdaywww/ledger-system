package com.www.ledger.data.vo.shop;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>@Description 查询用户的所有店铺的响应数据 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class ShopAllOutVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 店铺ID **/
    private Long value;
    /** 店铺名称 **/
    private String name;
}
