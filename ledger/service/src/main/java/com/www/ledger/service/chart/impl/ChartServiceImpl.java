package com.www.ledger.service.chart.impl;

import com.www.common.data.enums.DateFormatEnum;
import com.www.common.data.response.Result;
import com.www.common.utils.BigDecimalUtils;
import com.www.common.utils.DateUtils;
import com.www.ledger.data.dao.IDaySalesDAO;
import com.www.ledger.data.dao.IMonthSalesDAO;
import com.www.ledger.data.dao.IOrderInfoDAO;
import com.www.ledger.data.dao.IYearSalesDAO;
import com.www.ledger.data.dto.ChartDataDTO;
import com.www.ledger.data.dto.ChartPieDTO;
import com.www.ledger.data.dto.DayDTO;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.dto.OrderDTO;
import com.www.ledger.data.dto.YearDTO;
import com.www.ledger.data.enums.ChartEnum;
import com.www.ledger.service.chart.IChartService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>@Description 图表数据业务处理接口实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/26 18:06 </p>
 */
@Service
public class ChartServiceImpl implements IChartService {
    @Autowired
    private IOrderInfoDAO orderInfoDAO;
    @Autowired
    private IDaySalesDAO daySalesDAO;
    @Autowired
    private IMonthSalesDAO monthSalesDAO;
    @Autowired
    private IYearSalesDAO yearSalesDAO;

    /**
     * <p>@Description 查询日期范围内日销售图表数据，最大范围30天 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param startDate 起始日期
     * @param endDate 截止日期
     * @param chartList 图表数据枚举值集合
     * @return 日销售图表数据
     */
    @Override
    public Result<ChartDataDTO> findDayData(String userId, Long shopId, String startDate, String endDate, List<ChartEnum> chartList) {
        //日期有为空，则取现有订单中最大日期
        if (StringUtils.isAnyBlank(startDate,endDate)){
            //获取订单中最大的日期
            Date maxDate = orderInfoDAO.getMaxOrderDate(userId,shopId);
            if(maxDate == null){
                return new Result<>();
            }
            endDate = StringUtils.isBlank(endDate) ? DateUtils.format(maxDate,DateFormatEnum.YYYYMMDD1) : endDate;
            //截止日期为订单中最大的日期的前30天日期
            startDate = StringUtils.isBlank(startDate) ? DateUtils.format(DateUtils.stepDay(maxDate,-29),DateFormatEnum.YYYYMMDD1) : startDate;
        }
        //查询日期区间的店铺汇总日销售额
        List<DayDTO> dayList = daySalesDAO.findLastDaySales(userId,shopId,startDate,endDate);
        if(CollectionUtils.isEmpty(dayList)){
            return new Result<>();
        }
        //组装图表数据
        ChartDataDTO chartDTO = new ChartDataDTO();
        List<String> labels = new ArrayList<>();//图表x坐标名称集合
        List<ChartDataDTO.Series> datasetsList = new ArrayList<>();//图表数据
        //遍历需要组装的图表数据
        chartList.forEach(enums -> {
            if(ChartEnum.getDayEnum().contains(enums)){
                //组装图表数据
                ChartDataDTO.Series datasets = new ChartDataDTO.Series();
                datasets.setName(enums.getName()).setData(new ArrayList<>());
                datasetsList.add(datasets);
                dayList.forEach(dayDTO -> {
                    BigDecimal data = BigDecimal.ZERO;
                    if(enums.equals(ChartEnum.DAY_PROFIT)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getRetainedProfits());
                    }else if(enums.equals(ChartEnum.DAY_GROSS)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getRetainedProfits());
                    }else if(enums.equals(ChartEnum.DAY_TOTAL)){
                        data = dayDTO.getTotalOrder() != null ? new BigDecimal(dayDTO.getTotalOrder().toString()) : BigDecimal.ZERO;
                    }else if(enums.equals(ChartEnum.DAY_SUCCEED)){
                        data = dayDTO.getSucceedOrder() != null ? new BigDecimal(dayDTO.getSucceedOrder().toString()) : BigDecimal.ZERO;
                    }else if(enums.equals(ChartEnum.DAY_FAILED)){
                        data = dayDTO.getFailedOrder() != null ? new BigDecimal(dayDTO.getFailedOrder().toString()) : BigDecimal.ZERO;
                    }else if(enums.equals(ChartEnum.DAY_SALE)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getSaleAmount());
                    }else if(enums.equals(ChartEnum.DAY_COST)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getCostAmount());
                    }else if(enums.equals(ChartEnum.DAY_VIRTUAL)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getVirtualAmount());
                    }else if(enums.equals(ChartEnum.DAY_PAYOUT)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getTotalCost());
                    }
                    datasets.getData().add(data);
                });
            }
        });
        //组装图表x坐标名称集合数据
        dayList.forEach(dayDTO -> {
            labels.add(dayDTO.getDayDateStr());
        });
        chartDTO.setXaxis(labels).setSeries(datasetsList);
        return new Result<>(chartDTO);
    }

    /**
     * <p>@Description 查询日期范围内订单状态饼状图表数据，最大范围30天 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId    用户ID
     * @param shopId    店铺ID
     * @param startDate 起始日期
     * @param endDate   截止日期
     * @return 日订单状态饼状图表数据
     */
    @Override
    public Result<ChartPieDTO> findDayOrderState(String userId, Long shopId, String startDate, String endDate) {
        //日期有为空，则取现有订单中最大日期
        if (StringUtils.isAnyBlank(startDate,endDate)){
            //获取订单中最大的日期
            Date maxDate = orderInfoDAO.getMaxOrderDate(userId,shopId);
            if(maxDate == null){
                return new Result<>();
            }
            endDate = StringUtils.isBlank(endDate) ? DateUtils.format(maxDate,DateFormatEnum.YYYYMMDD1) : endDate;
            //截止日期为订单中最大的日期的前60天日期
            startDate = StringUtils.isBlank(startDate) ? DateUtils.format(DateUtils.stepDay(maxDate,-59),DateFormatEnum.YYYYMMDD1) : startDate;
        }
        //查询日期范围内订单状态饼状图表数据
        Result<ChartPieDTO> dtoResult = this.findOrderState(userId, shopId, startDate, endDate);
        if(dtoResult != null && dtoResult.getData() != null){
            dtoResult.getData().setStartDate(startDate).setEndDate(endDate);
        }
        return dtoResult;
    }
    /**
     * <p>@Description 查询月销售图表数据，最大范围12个月 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId    用户ID
     * @param shopId    店铺ID
     * @param startDate 起始日期
     * @param endDate 截止日期
     * @param chartList 图表数据枚举值集合
     * @return 月销售图表数据
     */
    @Override
    public Result<ChartDataDTO> findMonthData(String userId, Long shopId, String startDate, String endDate,List<ChartEnum> chartList) {
        //日期有为空，则取现有订单中最大日期
        if (StringUtils.isAnyBlank(startDate,endDate)){
            //获取订单中最大的日期
            Date maxDate = orderInfoDAO.getMaxOrderDate(userId,shopId);
            if(maxDate == null){
                return new Result<>();
            }
            endDate = StringUtils.isBlank(endDate) ? DateUtils.monthFirstDay(maxDate,DateFormatEnum.YYYYMMDD1) : endDate;
            //截止日期为订单中最大的日期的前12个月
            startDate = StringUtils.isBlank(startDate) ?
                    DateUtils.monthFirstDay(DateUtils.stepMonth(DateUtils.monthFirstDay(maxDate),-11),DateFormatEnum.YYYYMMDD1) : startDate;
        }
        //查询近一年的销售额
        List<MonthDTO> monthList = monthSalesDAO.findLastMonthData(userId,shopId,startDate,endDate);
        if(CollectionUtils.isEmpty(monthList)){
            return new Result<>();
        }
        //组装图表数据
        ChartDataDTO chartDTO = new ChartDataDTO();
        List<String> labels = new ArrayList<>();//图表x坐标名称集合
        List<ChartDataDTO.Series> datasetsList = new ArrayList<>();//图表数据
        //遍历需要组装的图表数据
        chartList.forEach(enums -> {
            if(ChartEnum.getMonthEnum().contains(enums)){
                //组装图表数据
                ChartDataDTO.Series datasets = new ChartDataDTO.Series();
                datasets.setName(enums.getName()).setData(new ArrayList<>());
                datasetsList.add(datasets);
                monthList.forEach(dayDTO -> {
                    BigDecimal data = BigDecimal.ZERO;
                    if(enums.equals(ChartEnum.MONTH_PROFIT)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getRetainedProfits());
                    }else if(enums.equals(ChartEnum.MONTH_GROSS)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getRetainedProfits());
                    }else if(enums.equals(ChartEnum.MONTH_TOTAL)){
                        data = dayDTO.getTotalOrder() != null ? new BigDecimal(dayDTO.getTotalOrder().toString()) : BigDecimal.ZERO;
                    }else if(enums.equals(ChartEnum.MONTH_SUCCEED)){
                        data = dayDTO.getSucceedOrder() != null ? new BigDecimal(dayDTO.getSucceedOrder().toString()) : BigDecimal.ZERO;
                    }else if(enums.equals(ChartEnum.MONTH_FAILED)){
                        data = dayDTO.getFailedOrder() != null ? new BigDecimal(dayDTO.getFailedOrder().toString()) : BigDecimal.ZERO;
                    }else if(enums.equals(ChartEnum.MONTH_SALE)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getSaleAmount());
                    }else if(enums.equals(ChartEnum.MONTH_COST)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getCostAmount());
                    }else if(enums.equals(ChartEnum.MONTH_ADVERT)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getAdvertAmount());
                    }else if(enums.equals(ChartEnum.MONTH_SERVICE)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getServiceAmount());
                    }else if(enums.equals(ChartEnum.MONTH_VIRTUAL)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getVirtualAmount());
                    }else if(enums.equals(ChartEnum.MONTH_PAYOUT)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getTotalCost());
                    }
                    datasets.getData().add(data);
                });
            }
        });
        //组装图表x坐标名称集合数据
        monthList.forEach(dayDTO -> {
            labels.add(dayDTO.getMonthDateStr());
        });
        chartDTO.setXaxis(labels).setSeries(datasetsList);
        return new Result<>(chartDTO);
    }
    /**
     * <p>@Description 查询日期范围内订单状态饼状图表数据，最大范围1年 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId    用户ID
     * @param shopId    店铺ID
     * @param startDateStr 起始日期
     * @param endDateStr   截止日期
     * @return 月订单状态饼状图表数据
     */
    @Override
    public Result<ChartPieDTO> findMonthOrderState(String userId, Long shopId, String startDateStr, String endDateStr) {
        Date startDate = DateUtils.monthFirstDay(DateUtils.parse(startDateStr,DateFormatEnum.YYYYMMDD1));
        Date endDate = DateUtils.monthLastDay(DateUtils.parse(endDateStr,DateFormatEnum.YYYYMMDD1));
        //日期有为空，则取现有订单中最大日期
        if (StringUtils.isAnyBlank(startDateStr,endDateStr)){
            //获取订单中最大的日期
            Date maxDate = orderInfoDAO.getMaxOrderDate(userId,shopId);
            if(maxDate == null){
                return new Result<>();
            }
            endDate = StringUtils.isBlank(endDateStr) ? DateUtils.monthLastDay(maxDate) : endDate;
            //截止日期为订单中最大的日期的前12个月日期
            startDate = StringUtils.isBlank(startDateStr) ? DateUtils.monthFirstDay(DateUtils.stepMonth(endDate,-11)) : startDate;
        }
        //查询日期范围内订单状态饼状图表数据
        Result<ChartPieDTO> dtoResult = this.findOrderState(userId, shopId, DateUtils.format(startDate,DateFormatEnum.YYYYMMDD1),DateUtils.format(endDate,DateFormatEnum.YYYYMMDD1));
        if(dtoResult != null && dtoResult.getData() != null){
            dtoResult.getData().setStartDate(DateUtils.format(startDate,DateFormatEnum.YYYYMM1))
                    .setEndDate(DateUtils.format(endDate,DateFormatEnum.YYYYMM1));
        }
        return dtoResult;
    }
    /**
     * <p>@Description 查询年利润图表数据，最大范围10年 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param startDate 起始日期
     * @param endDate 截止日期
     * @param chartList 图表数据枚举值集合
     * @return 年利润图表数据
     */
    @Override
    public Result<ChartDataDTO> findYearData(String userId, Long shopId, String startDate, String endDate,List<ChartEnum> chartList) {
        //日期有为空，则取现有订单中最大日期
        if (StringUtils.isAnyBlank(startDate,endDate)){
            //获取订单中最大的日期
            Date maxDate = orderInfoDAO.getMaxOrderDate(userId,shopId);
            if(maxDate == null){
                return new Result<>();
            }
            endDate = StringUtils.isBlank(endDate) ? DateUtils.yearFirstDay(maxDate,DateFormatEnum.YYYYMMDD1) : endDate;
            //截止日期为订单中最大的日期的前10年的日期
            startDate = StringUtils.isBlank(startDate) ? DateUtils.yearFirstDay(DateUtils.stepMonth(maxDate,-9),DateFormatEnum.YYYYMMDD1) : startDate;
        }
        //查询近一年的销售额
        List<YearDTO> yearList = yearSalesDAO.findLastYearData(userId,shopId,startDate,endDate);
        if(CollectionUtils.isEmpty(yearList)){
            return new Result<>();
        }
        //组装图表数据
        ChartDataDTO chartDTO = new ChartDataDTO();
        List<String> labels = new ArrayList<>();//图表x坐标名称集合
        List<ChartDataDTO.Series> datasetsList = new ArrayList<>();//图表数据
        //遍历需要组装的图表数据
        chartList.forEach(enums -> {
            if(ChartEnum.getYearEnum().contains(enums)){
                //组装图表数据
                ChartDataDTO.Series datasets = new ChartDataDTO.Series();
                datasets.setName(enums.getName()).setData(new ArrayList<>());
                datasetsList.add(datasets);
                yearList.forEach(dayDTO -> {
                    BigDecimal data = BigDecimal.ZERO;
                    if(enums.equals(ChartEnum.YEAR_PROFIT)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getRetainedProfits());
                    }else if(enums.equals(ChartEnum.YEAR_GROSS)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getRetainedProfits());
                    }else if(enums.equals(ChartEnum.YEAR_TOTAL)){
                        data = dayDTO.getTotalOrder() != null ? new BigDecimal(dayDTO.getTotalOrder().toString()) : BigDecimal.ZERO;
                    }else if(enums.equals(ChartEnum.YEAR_SUCCEED)){
                        data = dayDTO.getSucceedOrder() != null ? new BigDecimal(dayDTO.getSucceedOrder().toString()) : BigDecimal.ZERO;
                    }else if(enums.equals(ChartEnum.YEAR_FAILED)){
                        data = dayDTO.getFailedOrder() != null ? new BigDecimal(dayDTO.getFailedOrder().toString()) : BigDecimal.ZERO;
                    }else if(enums.equals(ChartEnum.YEAR_SALE)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getSaleAmount());
                    }else if(enums.equals(ChartEnum.YEAR_COST)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getCostAmount());
                    }else if(enums.equals(ChartEnum.YEAR_ADVERT)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getAdvertAmount());
                    }else if(enums.equals(ChartEnum.YEAR_SERVICE)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getServiceAmount());
                    }else if(enums.equals(ChartEnum.YEAR_VIRTUAL)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getVirtualAmount());
                    }else if(enums.equals(ChartEnum.YEAR_PAYOUT)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getTotalCost());
                    }
                    datasets.getData().add(data);
                });
            }
        });
        //组装图表x坐标名称集合数据
        yearList.forEach(dayDTO -> {
            labels.add(dayDTO.getYearStr());
        });
        chartDTO.setXaxis(labels).setSeries(datasetsList);
        return new Result<>(chartDTO);
    }
    /**
     * <p>@Description 查询日期范围内订单状态饼状图表数据，最大范围10年 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId    用户ID
     * @param shopId    店铺ID
     * @param startDateStr 起始日期
     * @param endDateStr   截止日期
     * @return 年订单状态饼状图表数据
     */
    @Override
    public Result<ChartPieDTO> findYearOrderState(String userId, Long shopId, String startDateStr, String endDateStr) {
        Date startDate = DateUtils.yearFirstDay(DateUtils.parse(startDateStr,DateFormatEnum.YYYYMMDD1));
        Date endDate = DateUtils.yearLastDay(DateUtils.parse(endDateStr,DateFormatEnum.YYYYMMDD1));
        //日期有为空，则取现有订单中最大日期
        if (StringUtils.isAnyBlank(startDateStr, endDateStr)){
            //获取订单中最大的日期
            Date maxDate = orderInfoDAO.getMaxOrderDate(userId,shopId);
            if(maxDate == null){
                return new Result<>();
            }
            endDate = StringUtils.isBlank(endDateStr) ? DateUtils.yearLastDay(maxDate) : endDate;
            //截止日期为订单中最大的日期的前10年日期
            startDate = StringUtils.isBlank(startDateStr) ? DateUtils.yearFirstDay(DateUtils.stepDate(endDate, Calendar.YEAR, -9)) : startDate;
        }
        //查询日期范围内订单状态饼状图表数据
        Result<ChartPieDTO> dtoResult = this.findOrderState(userId, shopId, DateUtils.format(startDate,DateFormatEnum.YYYYMMDD1),DateUtils.format(endDate,DateFormatEnum.YYYYMMDD1));
        if(dtoResult != null && dtoResult.getData() != null){
            dtoResult.getData().setStartDate( DateUtils.format(startDate,DateFormatEnum.YYYY2))
                    .setEndDate(DateUtils.format(endDate,DateFormatEnum.YYYY2));
        }
        return dtoResult;
    }
    /**
     * <p>@Description 查询日期范围内订单状态饼状图表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId    用户ID
     * @param shopId    店铺ID
     * @param startDateStr 起始日期
     * @param endDateStr   截止日期
     * @return 订单状态饼状图表数据
     */
    private Result<ChartPieDTO> findOrderState(String userId, Long shopId, String startDateStr, String endDateStr){
        List<OrderDTO> dtoList = orderInfoDAO.findDayOrderState(userId,shopId, startDateStr, endDateStr);
        if(CollectionUtils.isEmpty(dtoList)){
            return new Result<>();
        }
        ChartPieDTO dto = new ChartPieDTO();
        Long total = 0L;
        List<ChartPieDTO.Series> seriesList = new ArrayList<>();
        for (int i=0;i < dtoList.size();i++){
            OrderDTO orderDTO = dtoList.get(i);
            total += orderDTO.getTotalOrder();
            ChartPieDTO.Series series = new ChartPieDTO.Series();
            series.setName(orderDTO.getOrderStateName()).setValue(new BigDecimal(orderDTO.getTotalOrder()));
            seriesList.add(series);
        }
        BigDecimal totalNum = new BigDecimal(total);
        dto.setTotal(totalNum).setSeries(seriesList);
        return new Result<>(dto);
    }
}
