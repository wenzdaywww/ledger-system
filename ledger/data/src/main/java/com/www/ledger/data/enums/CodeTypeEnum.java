package com.www.ledger.data.enums;

/**
 * <p>@Description code类型枚举类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/2 21:04 </p>
 */
public enum CodeTypeEnum {
    /** 是否：否 **/
    YesOrNo_No("YesOrNo","是否","K0","否"),
    /** 是否：是 **/
    YesOrNo_Yes("YesOrNo","是否","K1","是"),
    /** 性别：男 **/
    Sex_Boy("Sex","性别","K1","男"),
    /** 性别：女 **/
    Sex_Girl("Sex","性别","K0","女"),
    /** 用户状态：有效 **/
    UserState_Valid("UserState","用户状态","K1","有效"),
    /** 用户状态：注销 **/
    UserState_Logout("UserState","用户状态","K2","注销"),
    /** 用户状态：封号 **/
    UserState_Ban("UserState","用户状态","K3","封号"),
    /** 店铺平台：拼多多 **/
    ShopPlatform_Pdd("ShopPlatform","店铺平台","K1","拼多多"),
    /** 店铺平台：拼多多 **/
    ShopPlatform_Taobao("ShopPlatform","店铺平台","K2","淘宝"),
    /** 店铺状态：有效 **/
    ShopState_Valid("ShopState","店铺状态","K1","有效"),
    /** 店铺状态：注销 **/
    ShopState_Logout("ShopState","店铺状态","K0","注销"),
    /** 商品状态：删除 **/
    GoodsState_Del("GoodsState","商品状态","K0","删除"),
    /** 商品状态：上架 **/
    GoodsState_Up("GoodsState","商品状态","K1","上架"),
    /** 商品状态：有效 **/
    GoodsState_Down("GoodsState","商品状态","K2","下架"),
    /** 订单状态：待确认 **/
    OrderState_Unconfirme("OrderState","订单状态","K0","待确认"),
    /** 订单状态：未支付，交易关闭 **/
    OrderState_Unpaid("OrderState","订单状态","K1","未支付，交易关闭"),
    /** 订单状态：未发货，退款成功 **/
    OrderState_Nosend("OrderState","订单状态","K2","未发货，退款成功"),
    /** 订单状态：已发货，待签收 **/
    OrderState_Sended("OrderState","订单状态","K3","已发货，待签收"),
    /** 订单状态：交易成功 **/
    OrderState_Success("OrderState","订单状态","K4","交易成功"),
    /** 订单状态：退货退款 **/
    OrderState_Return("OrderState","订单状态","K5","退货退款"),
    /** 订单状态：仅退款 **/
    OrderState_Refund("OrderState","订单状态","K6","仅退款"),
    /** 订单状态：刷单 **/
    OrderState_Virtual("OrderState","订单状态","K7","刷单"),
    /** 订单状态：待发货 **/
    OrderState_Sending("OrderState","订单状态","K8","待发货"),
    /** 订单状态：待申请退款 **/
    OrderState_Refunding("OrderState","订单状态","K9","待申请退款"),
    ;


    /** code类型 **/
    private String type;
    /** code类型名称 **/
    private String typeNm;
    /** code键值 **/
    private String key;
    /** code键值名称 **/
    private String keyNm;
    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/2 21:05 </p>
     * @param codeType code类型
     * @param codeName code类型名称
     * @param codeKey code键值
     * @param keyName code键值名称
     * @return
     */
    CodeTypeEnum(String codeType, String codeName,String codeKey, String keyName) {
        this.type = codeType;
        this.typeNm = codeName;
        this.key = codeKey;
        this.keyNm = keyName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeNm() {
        return typeNm;
    }

    public void setTypeNm(String typeNm) {
        this.typeNm = typeNm;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKeyNm() {
        return keyNm;
    }

    public void setKeyNm(String keyNm) {
        this.keyNm = keyNm;
    }
}
