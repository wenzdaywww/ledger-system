package com.www.ledger.data.vo.shop;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * <p>@Description 新增店铺信息的请求对象 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/17 21:08 </p>
 */
@Data
@Accessors(chain = true)
public class ShopNewInVO {
    private static final long serialVersionUID = 1L;
    /** 店铺名称 **/
    @NotBlank(message = "店铺名称不能为空")
    @Length(min = 1, max = 20,message = "店铺名称字数为1~20")
    private String shopNm;
    /** 店铺平台 **/
    @NotBlank(message = "店铺平台不能为空")
    private String shopTp;
}
