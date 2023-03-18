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
 * <p>@Description 店铺商品信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
@TableName("SHOP_GOODS")
public class ShopGoodsEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 商品主键ID **/
    @TableId(value = "SG_ID", type = IdType.AUTO)
    private Long sgId;
    /** 商品ID **/
    @TableField("GOODS_ID")
    private Long goodsId;
    /** 商品名称 **/
    @TableField("GOODS_NAME")
    private String goodsName;
    /** 商品链接 **/
    @TableField("GOODS_URL")
    private String goodsUrl;
    /** 店铺主键ID **/
    @TableField("SHOP_ID")
    private Long shopId;
    /** 用户名 **/
    @TableField("USER_ID")
    private String userId;
    /** 商品状态(code=GoodsState)：0删除，1上架，2下架 **/
    @TableField("GOODS_STATE")
    private String goodsState;
    /** 更新时间 **/
    @TableField("UPDATE_TIME")
    private Date updateTime;
    /** 创建时间 **/
    @TableField("CREATE_TIME")
    private Date createTime;
}
