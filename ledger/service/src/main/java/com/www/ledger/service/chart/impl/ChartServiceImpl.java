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
import com.www.ledger.data.dto.DayDTO;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.dto.YearDTO;
import com.www.ledger.data.enums.ChartEnum;
import com.www.ledger.service.chart.IChartService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     * <p>@Description 查询日销售图表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param days 查询的日期区间天数
     * @param chartList 图表数据枚举值集合
     * @return 日销售图表数据
     */
    @Override
    public Result<ChartDataDTO> findDayData(String userId, Long shopId, int days, List<ChartEnum> chartList) {
        //获取订单中最大的日期
        Date maxDate = orderInfoDAO.getMaxOrderDate(userId,shopId);
        if(maxDate == null){
            return new Result<>();
        }
        //获取maxDateStr的days天前日期
        int dayStep = -1*days;
        String minData = DateUtils.format(DateUtils.stepDay(maxDate,dayStep),DateFormatEnum.YYYYMMDD1);
        //查询日期区间的店铺汇总日销售额
        List<DayDTO> dtoList = daySalesDAO.findLastDaySales(userId,shopId,minData,DateUtils.format(maxDate, DateFormatEnum.YYYYMMDD1));
        if(CollectionUtils.isEmpty(dtoList)){
            return new Result<>();
        }
        //dayList转为map，用于判断近days日哪天没有销售额
        //Map<日期，当日销售额>
        Map<String,DayDTO> ordMap = dtoList.stream().collect(Collectors.toMap(k -> k.getDayDateStr(), day -> day));
        //循环判断近days日是否都有销售额，没有则补齐数据,从最早日期开始
        for (int i = (-1*(days-1)); i <= 0; i++){
            Date day = DateUtils.stepDay(maxDate,i);
            String dayStr = DateUtils.format(day,DateFormatEnum.YYYYMD4);
            if(!ordMap.containsKey(dayStr)){
                DayDTO tempDTO = new DayDTO();
                tempDTO.setDayDate(day).setDayDateStr(dayStr);
                ordMap.put(dayStr,tempDTO);
            }
        }
        List<DayDTO> dayList = new ArrayList<>(ordMap.values());
        //按日期升序排列
        dayList.sort((a,b) -> a.getDayDate().compareTo(b.getDayDate()));
        //组装图表数据
        ChartDataDTO chartDTO = new ChartDataDTO();
        List<String> labels = new ArrayList<>();//图表x坐标名称集合
        List<ChartDataDTO.Datasets> datasetsList = new ArrayList<>();//图表数据
        //遍历需要组装的图表数据
        chartList.forEach(enums -> {
            if(ChartEnum.getDayEnum().contains(enums)){
                //组装图表数据
                ChartDataDTO.Datasets datasets = new ChartDataDTO.Datasets();
                datasets.setLabel(enums.getName()).setData(new ArrayList<>());
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
        chartDTO.setLabels(labels).setDatasets(datasetsList);
        return new Result<>(chartDTO);
    }
    /**
     * <p>@Description 查询月销售图表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId    用户ID
     * @param shopId    店铺ID
     * @param chartList 图表数据枚举值集合
     * @return 月销售图表数据
     */
    @Override
    public Result<ChartDataDTO> findMonthData(String userId, Long shopId, List<ChartEnum> chartList) {
        //获取订单中最大的日期
        Date maxDate = orderInfoDAO.getMaxOrderDate(userId,shopId);
        //查询近一年的销售额
        List<MonthDTO> dtoList = monthSalesDAO.findLastMonthData(userId,shopId,DateUtils.format(maxDate,DateFormatEnum.YYYYMM1) + "-01");
        if(CollectionUtils.isEmpty(dtoList)){
            return new Result<>();
        }
        Map<String,MonthDTO> dtoMap = CollectionUtils.isEmpty(dtoList) ? new HashMap<>()
                : dtoList.stream().collect(Collectors.toMap(k -> k.getMonthDateStr(),month -> month));
        //近一年的销售额数据
        List<MonthDTO> monthList = new ArrayList<>();
        for (int i = -11; i <= 0; i++){
            //获取前i个月日期
            Date monthDate = DateUtils.stepMonth(DateUtils.getCurrentDateTime(),i);
            //获取前i个月月份
            String monthStr = DateUtils.format(monthDate, DateFormatEnum.YYYYMM3);
            if(dtoMap.containsKey(monthStr)){
                monthList.add(dtoMap.get(monthStr));
            }else {
                MonthDTO monthDTO = new MonthDTO();
                monthDTO.setMonthDate(monthDate).setMonthDateStr(monthStr);
                monthList.add(monthDTO);
            }
        }
        //按日期升序排列
        monthList.sort((a,b) -> a.getMonthDate().compareTo(b.getMonthDate()));
        //组装图表数据
        ChartDataDTO chartDTO = new ChartDataDTO();
        List<String> labels = new ArrayList<>();//图表x坐标名称集合
        List<ChartDataDTO.Datasets> datasetsList = new ArrayList<>();//图表数据
        //遍历需要组装的图表数据
        chartList.forEach(enums -> {
            if(ChartEnum.getMonthEnum().contains(enums)){
                //组装图表数据
                ChartDataDTO.Datasets datasets = new ChartDataDTO.Datasets();
                datasets.setLabel(enums.getName()).setData(new ArrayList<>());
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
        chartDTO.setLabels(labels).setDatasets(datasetsList);
        return new Result<>(chartDTO);
    }
    /**
     * <p>@Description 查询年利润图表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param chartList 图表数据枚举值集合
     * @return 年利润图表数据
     */
    @Override
    public Result<ChartDataDTO> findYearData(String userId, Long shopId, List<ChartEnum> chartList) {
        //获取订单中最大的日期
        Date maxDate = orderInfoDAO.getMaxOrderDate(userId,shopId);
        //查询近一年的销售额
        List<YearDTO> yearList = yearSalesDAO.findLastYearData(userId,shopId,DateUtils.format(maxDate,DateFormatEnum.YYYY1) + "-01-01");
        if(CollectionUtils.isEmpty(yearList)){
            return new Result<>();
        }
        //按日期升序排列
        yearList.sort((a,b) -> a.getYear().compareTo(b.getYear()));
        //组装图表数据
        ChartDataDTO chartDTO = new ChartDataDTO();
        List<String> labels = new ArrayList<>();//图表x坐标名称集合
        List<ChartDataDTO.Datasets> datasetsList = new ArrayList<>();//图表数据
        //遍历需要组装的图表数据
        chartList.forEach(enums -> {
            if(ChartEnum.getYearEnum().contains(enums)){
                //组装图表数据
                ChartDataDTO.Datasets datasets = new ChartDataDTO.Datasets();
                datasets.setLabel(enums.getName()).setData(new ArrayList<>());
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
        chartDTO.setLabels(labels).setDatasets(datasetsList);
        return new Result<>(chartDTO);
    }
}
