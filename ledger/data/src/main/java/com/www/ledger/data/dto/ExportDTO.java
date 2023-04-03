package com.www.ledger.data.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>@Description 报表导出数据类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/4/2 17:32 </p>
 */
@Data
@Accessors(chain = true)
public class ExportDTO {
    /** 我的账簿数据 **/
    private BookDTO book;
    /** 我的店铺数据 **/
    private List<ShopDTO> shop;
    /** 店铺的年销售额数据 **/
    private List<YearDTO> shopYear;
    /** 店铺汇总的年销售额数据 **/
    private List<YearDTO> year;
    /** 店铺的月销售额数据 **/
    private List<MonthDTO> shopMonth;
    /** 店铺汇总的月销售额数据 **/
    private List<MonthDTO> month;
    /** 店铺的日销售额数据 **/
    private List<DayDTO> shopDay;
    /** 店铺汇总的日销售额数据 **/
    private List<DayDTO> day;
    /** 订单信息数据 **/
    private List<OrderDTO> order;
    /** 支出信息数据 **/
    private List<PayDTO> pay;
}
