package com.www.ledger.data.enums;

/**
 * <p>@Description code类型枚举类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/2 21:04 </p>
 */
public enum CodeTypeEnum {
    /** 性别 **/
    Sex("sex","性别"),
    /** 用户状态 **/
    UserState("UserState","用户状态"),
    /** 是否 **/
    YesOrNo("YesOrNo","是否"),
    /** 店铺平台 **/
    ShopPlatform("ShopPlatform","店铺平台"),
    /** 商品状态 **/
    ShopState("ShopState","店铺状态"),
    /** 性别 **/
    GoodsState("GoodsState","商品状态"),
    /** 是否标志 **/
    OrderState("OrderState","订单状态")
    ;


    /** 名称 **/
    private String codeName;
    /** 码值 **/
    private String codeType;
    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/2 21:05 </p>
     * @param codeType code类型
     * @param codeName code类型名称
     * @return
     */
    CodeTypeEnum(String codeType, String codeName) {
        this.codeType = codeType;
        this.codeName = codeName;
    }

    public String getCodeName() {
        return codeName;
    }

    public String getCodeType() {
        return codeType;
    }

}
