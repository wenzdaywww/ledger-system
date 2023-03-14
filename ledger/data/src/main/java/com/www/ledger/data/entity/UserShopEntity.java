package com.www.ledger.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>@Description 用户店铺信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
@TableName("USER_SHOP")
public class UserShopEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 店铺主键ID **/
    @TableId(value = "SHOP_ID", type = IdType.AUTO)
    private Long shopId;
    /** 店铺名 **/
    @TableField("SHOP_NAME")
    private String shopName;
    /** 用户名 **/
    @TableField("USER_ID")
    private String userId;
    /** 店铺平台(code=ShopPlatform) **/
    @TableField("SHOP_TYPE")
    private String shopType;
    /** 店铺状态(code=ShopState)：1有效，0注销 **/
    @TableField("SHOP_STATE")
    private String shopState;
    /** 更新时间 **/
    @TableField("UPDATE_TIME")
    private Date updateTime;
    /** 创建时间 **/
    @TableField("CREATE_TIME")
    private Date createTime;
}
