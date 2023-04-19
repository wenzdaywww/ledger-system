package com.www.ledger.data.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>@Description 店铺信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class ShopDTO extends BaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 店铺主键ID **/
    private Long shopId;
    /** 店铺名称 **/
    private String shopName;
    /** 店铺平台 **/
    private String shopType;
    /** 店铺平台名称 **/
    private String shopTypeName;
}
