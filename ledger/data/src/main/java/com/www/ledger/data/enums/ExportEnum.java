package com.www.ledger.data.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>@Description 报表导出的数据枚举 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/4/2 21:04 </p>
 */
public enum ExportEnum {
    /** 我的账簿 **/
    BOOK(0,"我的账簿"),
    /** 我的店铺 **/
    SHOP(1,"我的店铺"),
    /** 店铺年销售额 **/
    SHOP_YEAR(2,"店铺年销售额"),
    /** 汇总年销售额 **/
    YEAR(3,"汇总年销售额"),
    /** 店铺月销售额 **/
    SHOP_MONTH(4,"店铺月销售额"),
    /** 汇总月销售额 **/
    MONTH(5,"汇总月销售额"),
    /** 店铺日销售额 **/
    SHOP_DAY(6,"店铺日销售额"),
    /** 汇总日销售额 **/
    DAY(7,"汇总日销售额"),
    /** 订单信息 **/
    ORDER(8,"订单信息"),
    /** 支出管理 **/
    PAY(9,"支出管理"),
    ;

    /** 待导出报表数据前端编号 **/
    private int num;
    /** 报表文件sheet页名称 **/
    private String name;
    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 21:06 </p>
     * @param num 报表数据编号
     * @param name 报表数据名称
     */
    ExportEnum(int num, String name) {
        this.num = num;
        this.name = name;
    }
    /**
     * <p>@Description 获取所有枚举值 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 23:17 </p>
     * @return 所有枚举值
     */
    public static List<ExportEnum> getAllEnum(){
        List<ExportEnum> list = new ArrayList<>();
        list.add(BOOK);
        list.add(SHOP);
        list.add(SHOP_YEAR);
        list.add(YEAR);
        list.add(SHOP_MONTH);
        list.add(MONTH);
        list.add(SHOP_DAY);
        list.add(DAY);
        list.add(ORDER);
        list.add(PAY);
        return list;
    }

    public int getNum() {
        return num;
    }

    public String getName() {
        return name;
    }
}
