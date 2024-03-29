package com.www.ledger.data.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>@Description 图表数据枚举值 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/4/1 20:34 </p>
 */
public enum ChartEnum {
    /** 日净利润 **/
    DAY_PROFIT("日净利润","RETAINED_PROFITS"),
    /** 日毛利润 **/
    DAY_GROSS("日毛利润","GROSS_PROFIT"),
    /** 日订单量 **/
    DAY_TOTAL("日订单量","TOTAL_ORDER"),
    /** 日成交单 **/
    DAY_SUCCEED("日成交单","SUCCEED_ORDER"),
    /** 日流失单 **/
    DAY_FAILED("日流失单","FAILED_ORDER"),
    /** 日销售额 **/
    DAY_SALE("日销售额","SALE_AMOUNT"),
    /** 日成本费 **/
    DAY_COST("日成本费","COST_AMOUNT"),
    /** 日刷单费 **/
    DAY_VIRTUAL("日刷单费","SERVICE_AMOUNT"),
    /** 日支出费 **/
    DAY_PAYOUT("日支出费","TOTAL_COST"),

    /** 月净利润 **/
    MONTH_PROFIT("月净利润","RETAINED_PROFITS"),
    /** 月毛利润 **/
    MONTH_GROSS("月毛利润","GROSS_PROFIT"),
    /** 月订单量 **/
    MONTH_TOTAL("月订单量","TOTAL_ORDER"),
    /** 月成交单 **/
    MONTH_SUCCEED("月成交单","SUCCEED_ORDER"),
    /** 月流失单 **/
    MONTH_FAILED("月流失单","FAILED_ORDER"),
    /** 月销售额 **/
    MONTH_SALE("月销售额","SALE_AMOUNT"),
    /** 月成本费 **/
    MONTH_COST("月成本费","COST_AMOUNT"),
    /** 月推广费 **/
    MONTH_ADVERT("月推广费","ADVERT_AMOUNT"),
    /** 月服务费 **/
    MONTH_SERVICE("月服务费","SERVICE_AMOUNT"),
    /** 月刷单费 **/
    MONTH_VIRTUAL("月刷单费","VIRTUAL_AMOUNT"),
    /** 月支出费 **/
    MONTH_PAYOUT("月支出费","TOTAL_COST"),

    /** 年净利润 **/
    YEAR_PROFIT("年净利润","RETAINED_PROFITS"),
    /** 年毛利润 **/
    YEAR_GROSS("年毛利润","GROSS_PROFIT"),
    /** 年订单量 **/
    YEAR_TOTAL("年订单量","TOTAL_ORDER"),
    /** 年成交单 **/
    YEAR_SUCCEED("年成交单","SUCCEED_ORDER"),
    /** 年流失单 **/
    YEAR_FAILED("年流失单","FAILED_ORDER"),
    /** 年销售额 **/
    YEAR_SALE("年销售额","SALE_AMOUNT"),
    /** 年成本费 **/
    YEAR_COST("年成本费","COST_AMOUNT"),
    /** 年推广费 **/
    YEAR_ADVERT("年推广费","ADVERT_AMOUNT"),
    /** 年服务费 **/
    YEAR_SERVICE("年服务费","SERVICE_AMOUNT"),
    /** 年刷单费 **/
    YEAR_VIRTUAL("年刷单费","VIRTUAL_AMOUNT"),
    /** 年支出费 **/
    YEAR_PAYOUT("年支出费","TOTAL_COST"),

    /** 店净利润 **/
    SHOP_PROFIT("店净利润","RETAINED_PROFITS"),
    /** 店毛利润 **/
    SHOP_GROSS("店毛利润","GROSS_PROFIT"),
    /** 店订单量 **/
    SHOP_TOTAL("店订单量","TOTAL_ORDER"),
    /** 店成交单 **/
    SHOP_SUCCEED("店成交单","SUCCEED_ORDER"),
    /** 店流失单 **/
    SHOP_FAILED("店流失单","FAILED_ORDER"),
    /** 店销售额 **/
    SHOP_SALE("店销售额","SALE_AMOUNT"),
    /** 店成本费 **/
    SHOP_COST("店成本费","COST_AMOUNT"),
    /** 店推广费 **/
    SHOP_ADVERT("店推广费","ADVERT_AMOUNT"),
    /** 店服务费 **/
    SHOP_SERVICE("店服务费","SERVICE_AMOUNT"),
    /** 店刷单费 **/
    SHOP_VIRTUAL("店刷单费","VIRTUAL_AMOUNT"),
    /** 店支出费 **/
    SHOP_PAYOUT("店支出费","TOTAL_COST"),
    ;
    /** 图表数据名称 **/
    private String name;
    /** 图表数据名称对应字段名 **/
    private String field;
    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 20:35 </p>
     * @param name 图表数据名称
     * @param field 图表数据名称对应字段名
     * @return
     */
    ChartEnum(String name,String field) {
        this.name = name;
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public String getField() {
        return field;
    }
    /**
     * <p>@Description 获取日销售数据的图表数据枚举值 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 20:53 </p>
     * @return
     */
    public static List<ChartEnum> getDayEnum(){
        List<ChartEnum> list = new ArrayList<>();
        list.add(DAY_PROFIT);
        list.add(DAY_GROSS);
        list.add(DAY_TOTAL);
        list.add(DAY_SUCCEED);
        list.add(DAY_FAILED);
        list.add(DAY_SALE);
        list.add(DAY_COST);
        list.add(DAY_VIRTUAL);
        list.add(DAY_PAYOUT);
        return list;
    }
    /**
     * <p>@Description 获取月销售数据的图表数据枚举值 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 20:53 </p>
     * @return
     */
    public static List<ChartEnum> getMonthEnum(){
        List<ChartEnum> list = new ArrayList<>();
        list.add(MONTH_PROFIT);
        list.add(MONTH_GROSS);
        list.add(MONTH_TOTAL);
        list.add(MONTH_SUCCEED);
        list.add(MONTH_FAILED);
        list.add(MONTH_SALE);
        list.add(MONTH_COST);
        list.add(MONTH_ADVERT);
        list.add(MONTH_SERVICE);
        list.add(MONTH_VIRTUAL);
        list.add(MONTH_PAYOUT);
        return list;
    }
    /**
     * <p>@Description 获取年销售数据的图表数据枚举值 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 20:53 </p>
     * @return
     */
    public static List<ChartEnum> getYearEnum(){
        List<ChartEnum> list = new ArrayList<>();
        list.add(YEAR_PROFIT);
        list.add(YEAR_GROSS);
        list.add(YEAR_TOTAL);
        list.add(YEAR_SUCCEED);
        list.add(YEAR_FAILED);
        list.add(YEAR_SALE);
        list.add(YEAR_COST);
        list.add(YEAR_ADVERT);
        list.add(YEAR_SERVICE);
        list.add(YEAR_VIRTUAL);
        list.add(YEAR_PAYOUT);
        return list;
    }
    /**
     * <p>@Description 获取店铺销售数据的图表数据枚举值 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 20:53 </p>
     * @return
     */
    public static List<ChartEnum> getShopEnum(){
        List<ChartEnum> list = new ArrayList<>();
        list.add(SHOP_PROFIT);
        list.add(SHOP_GROSS);
        list.add(SHOP_TOTAL);
        list.add(SHOP_SUCCEED);
        list.add(SHOP_FAILED);
        list.add(SHOP_SALE);
        list.add(SHOP_COST);
        list.add(SHOP_ADVERT);
        list.add(SHOP_SERVICE);
        list.add(SHOP_VIRTUAL);
        list.add(SHOP_PAYOUT);
        return list;
    }
}
