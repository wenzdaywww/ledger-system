package com.www.ledger.service.chart.impl;

import com.www.common.data.enums.DateFormatEnum;
import com.www.common.data.response.Result;
import com.www.common.utils.BigDecimalUtils;
import com.www.common.utils.DateUtils;
import com.www.ledger.data.constant.BizConstant;
import com.www.ledger.data.dao.IDaySalesDAO;
import com.www.ledger.data.dao.IMonthSalesDAO;
import com.www.ledger.data.dao.IOrderInfoDAO;
import com.www.ledger.data.dao.IShopSalesDAO;
import com.www.ledger.data.dao.IYearSalesDAO;
import com.www.ledger.data.dto.BaseDTO;
import com.www.ledger.data.dto.ChartDataDTO;
import com.www.ledger.data.dto.ChartPieDTO;
import com.www.ledger.data.dto.ChartRankDTO;
import com.www.ledger.data.dto.DayDTO;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.dto.OrderDTO;
import com.www.ledger.data.dto.ShopDTO;
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
    @Autowired
    private IShopSalesDAO shopSalesDAO;


    /**
     * <p>@Description 查询日期范围内日销售图表数据，最大范围30天 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param startDateStr 起始日期
     * @param endDateStr 截止日期
     * @param chartList 图表数据枚举值集合
     * @return 日销售图表数据
     */
    @Override
    public Result<ChartDataDTO> findDayData(String userId, Long shopId, String startDateStr, String endDateStr, List<ChartEnum> chartList) {
        //获取查询的起始日期和截止日期
        Map<String,Date> dateMap = this.getDateRange(userId,shopId, startDateStr, endDateStr,Calendar.DATE,30);
        startDateStr = DateUtils.format(dateMap.get(BizConstant.MIN_DATE),DateFormatEnum.YYYYMMDD1);
        endDateStr = DateUtils.format(dateMap.get(BizConstant.MAX_DATE),DateFormatEnum.YYYYMMDD1);
        if(StringUtils.isAnyBlank(startDateStr,endDateStr)){
            return new Result<>();
        }
        //查询日期区间的店铺汇总日销售额
        List<DayDTO> dayList = daySalesDAO.findLastDaySales(userId,shopId,startDateStr,endDateStr);
        //组装图表数据
        return this.buildChartDataDTO(DateUtils.format(dateMap.get(BizConstant.MIN_DATE),DateFormatEnum.YYYYMD3),DateUtils.format(dateMap.get(BizConstant.MAX_DATE),DateFormatEnum.YYYYMD3),dayList,chartList);
    }
    /**
     * <p>@Description 查询月销售图表数据，最大范围12个月 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId    用户ID
     * @param shopId    店铺ID
     * @param startDateStr 起始日期
     * @param endDateStr 截止日期
     * @param chartList 图表数据枚举值集合
     * @return 月销售图表数据
     */
    @Override
    public Result<ChartDataDTO> findMonthData(String userId, Long shopId, String startDateStr, String endDateStr, List<ChartEnum> chartList) {
        //获取查询的起始日期和截止日期
        Map<String,Date> dateMap = this.getDateRange(userId,shopId, startDateStr, endDateStr,Calendar.MONTH,12);
        startDateStr = DateUtils.format(dateMap.get(BizConstant.MIN_DATE),DateFormatEnum.YYYYMMDD1);
        endDateStr = DateUtils.format(dateMap.get(BizConstant.MAX_DATE),DateFormatEnum.YYYYMMDD1);
        if(StringUtils.isAnyBlank(startDateStr,endDateStr)){
            return new Result<>();
        }
        //查询近一年的销售额
        List<MonthDTO> monthList = monthSalesDAO.findLastMonthData(userId,shopId, startDateStr, endDateStr);
        //组装图表数据
        return this.buildChartDataDTO(DateUtils.format(dateMap.get(BizConstant.MIN_DATE),DateFormatEnum.YYYYM3),DateUtils.format(dateMap.get(BizConstant.MAX_DATE),DateFormatEnum.YYYYM3),monthList,chartList);
    }
    /**
     * <p>@Description 查询年利润图表数据，最大范围10年 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param startDateStr 起始日期
     * @param endDateStr 截止日期
     * @param chartList 图表数据枚举值集合
     * @return 年利润图表数据
     */
    @Override
    public Result<ChartDataDTO> findYearData(String userId, Long shopId, String startDateStr, String endDateStr, List<ChartEnum> chartList) {
        //获取查询的起始日期和截止日期
        Map<String,Date> dateMap = this.getDateRange(userId,shopId, startDateStr, endDateStr,Calendar.YEAR,10);
        startDateStr = DateUtils.format(dateMap.get(BizConstant.MIN_DATE),DateFormatEnum.YYYYMMDD1);
        endDateStr = DateUtils.format(dateMap.get(BizConstant.MAX_DATE),DateFormatEnum.YYYYMMDD1);
        if(StringUtils.isAnyBlank(startDateStr,endDateStr)){
            return new Result<>();
        }
        //查询近一年的销售额
        List<YearDTO> yearList = yearSalesDAO.findLastYearData(userId,shopId, startDateStr, endDateStr);
        //组装图表数据
        return this.buildChartDataDTO(DateUtils.format(dateMap.get(BizConstant.MIN_DATE),DateFormatEnum.YYYY2),DateUtils.format(dateMap.get(BizConstant.MAX_DATE),DateFormatEnum.YYYY2),yearList,chartList);
    }
    /**
     * <p>@Description 根据查询的销售数据组装返回图表DTO数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/19 19:30 </p>
     * @param startDateStr 起始日期
     * @param endDateStr 截止日期
     * @param dataList BaseDTO的子类销售数据
     * @param chartList 图表数据枚举值集合
     * @return 返回图表DTO数据
     */
    private Result<ChartDataDTO> buildChartDataDTO(String startDateStr,String endDateStr,List dataList, List<ChartEnum> chartList){
        ChartDataDTO chartDTO = new ChartDataDTO();
        chartDTO.setStartDate(startDateStr).setEndDate(endDateStr);
        if(CollectionUtils.isEmpty(dataList)){
            return new Result<>(chartDTO);
        }
        List<String> labels = new ArrayList<>();//图表x坐标名称集合
        List<ChartDataDTO.Series> datasetsList = new ArrayList<>();//图表数据
        //遍历需要组装的图表数据
        chartList.forEach(enums -> {
            if(ChartEnum.getDayEnum().contains(enums) || ChartEnum.getMonthEnum().contains(enums) || ChartEnum.getYearEnum().contains(enums)){
                //组装图表数据
                ChartDataDTO.Series datasets = new ChartDataDTO.Series();
                datasets.setName(enums.getName()).setData(new ArrayList<>());
                datasetsList.add(datasets);
                for (int i = 0; i < dataList.size(); i++) {
                    BaseDTO dayDTO = (BaseDTO) dataList.get(i);
                    BigDecimal data = BigDecimal.ZERO;
                    if(enums.equals(ChartEnum.DAY_PROFIT) || enums.equals(ChartEnum.MONTH_PROFIT) || enums.equals(ChartEnum.YEAR_PROFIT)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getRetainedProfits());
                    }else if(enums.equals(ChartEnum.DAY_GROSS) || enums.equals(ChartEnum.MONTH_GROSS) || enums.equals(ChartEnum.YEAR_GROSS)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getRetainedProfits());
                    }else if(enums.equals(ChartEnum.DAY_TOTAL) || enums.equals(ChartEnum.MONTH_TOTAL) || enums.equals(ChartEnum.YEAR_TOTAL)){
                        data = dayDTO.getTotalOrder() != null ? new BigDecimal(dayDTO.getTotalOrder().toString()) : BigDecimal.ZERO;
                    }else if(enums.equals(ChartEnum.DAY_SUCCEED) || enums.equals(ChartEnum.MONTH_SUCCEED) || enums.equals(ChartEnum.YEAR_SUCCEED)){
                        data = dayDTO.getSucceedOrder() != null ? new BigDecimal(dayDTO.getSucceedOrder().toString()) : BigDecimal.ZERO;
                    }else if(enums.equals(ChartEnum.DAY_FAILED) || enums.equals(ChartEnum.MONTH_FAILED) || enums.equals(ChartEnum.YEAR_FAILED)){
                        data = dayDTO.getFailedOrder() != null ? new BigDecimal(dayDTO.getFailedOrder().toString()) : BigDecimal.ZERO;
                    }else if(enums.equals(ChartEnum.DAY_SALE) || enums.equals(ChartEnum.MONTH_SALE) || enums.equals(ChartEnum.YEAR_SALE)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getSaleAmount());
                    }else if(enums.equals(ChartEnum.DAY_COST) || enums.equals(ChartEnum.MONTH_COST) || enums.equals(ChartEnum.YEAR_COST)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getCostAmount());
                    }else if(enums.equals(ChartEnum.DAY_VIRTUAL) || enums.equals(ChartEnum.MONTH_VIRTUAL) || enums.equals(ChartEnum.YEAR_VIRTUAL)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getVirtualAmount());
                    }else if(enums.equals(ChartEnum.DAY_PAYOUT) || enums.equals(ChartEnum.MONTH_PAYOUT) || enums.equals(ChartEnum.YEAR_PAYOUT)){
                        data = BigDecimalUtils.nullToZero(dayDTO.getTotalCost());
                    }
                    datasets.getData().add(data);
                }
            }
        });
        //组装图表x坐标名称集合数据
        for (int i = 0; i < dataList.size(); i++) {
            BaseDTO dayDTO = (BaseDTO) dataList.get(i);
            labels.add(dayDTO.getLabel());
        }
        chartDTO.setXaxis(labels).setSeries(datasetsList);
        return new Result<>(chartDTO);
    }
    /**
     * <p>@Description 查询日期范围内订单状态饼状图表数据，最大范围60天 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId    用户ID
     * @param shopId    店铺ID
     * @param startDateStr 起始日期
     * @param endDateStr   截止日期
     * @return 日订单状态饼状图表数据
     */
    @Override
    public Result<ChartPieDTO> findDayOrderState(String userId, Long shopId, String startDateStr, String endDateStr) {
        //获取查询的起始日期和截止日期
        Map<String,Date> dateMap = this.getDateRange(userId,shopId, startDateStr, endDateStr,Calendar.DATE,60);
        startDateStr = DateUtils.format(dateMap.get(BizConstant.MIN_DATE),DateFormatEnum.YYYYMMDD1);
        endDateStr = DateUtils.format(dateMap.get(BizConstant.MAX_DATE),DateFormatEnum.YYYYMMDD1);
        if(StringUtils.isAnyBlank(startDateStr,endDateStr)){
            return new Result<>();
        }
        //查询日期范围内订单状态饼状图表数据
        Result<ChartPieDTO> dtoResult = this.findOrderState(userId, shopId, startDateStr, endDateStr);
        dtoResult.getData().setStartDate(DateUtils.format(dateMap.get(BizConstant.MIN_DATE),DateFormatEnum.YYYYMD3))
                .setEndDate(DateUtils.format(dateMap.get(BizConstant.MAX_DATE),DateFormatEnum.YYYYMD3));
        return dtoResult;
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
        //获取查询的起始日期和截止日期
        Map<String,Date> dateMap = this.getDateRange(userId,shopId, startDateStr, endDateStr,Calendar.MONTH,12);
        startDateStr = DateUtils.format(dateMap.get(BizConstant.MIN_DATE),DateFormatEnum.YYYYMMDD1);
        endDateStr = DateUtils.format(dateMap.get(BizConstant.MAX_DATE),DateFormatEnum.YYYYMMDD1);
        if(StringUtils.isAnyBlank(startDateStr,endDateStr)){
            return new Result<>();
        }
        //查询日期范围内订单状态饼状图表数据
        Result<ChartPieDTO> dtoResult = this.findOrderState(userId, shopId, startDateStr,endDateStr);
        dtoResult.getData().setStartDate(DateUtils.format(dateMap.get(BizConstant.MIN_DATE),DateFormatEnum.YYYYM3))
                .setEndDate(DateUtils.format(dateMap.get(BizConstant.MAX_DATE),DateFormatEnum.YYYYM3));
        return dtoResult;
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
        //获取查询的起始日期和截止日期
        Map<String,Date> dateMap = this.getDateRange(userId,shopId, startDateStr, endDateStr,Calendar.YEAR,10);
        startDateStr = DateUtils.format(dateMap.get(BizConstant.MIN_DATE),DateFormatEnum.YYYYMMDD1);
        endDateStr = DateUtils.format(dateMap.get(BizConstant.MAX_DATE),DateFormatEnum.YYYYMMDD1);
        if(StringUtils.isAnyBlank(startDateStr,endDateStr)){
            return new Result<>();
        }
        //查询日期范围内订单状态饼状图表数据
        Result<ChartPieDTO> dtoResult = this.findOrderState(userId, shopId, startDateStr,endDateStr);
        dtoResult.getData().setStartDate(DateUtils.format(dateMap.get(BizConstant.MIN_DATE),DateFormatEnum.YYYY2))
                .setEndDate(DateUtils.format(dateMap.get(BizConstant.MAX_DATE),DateFormatEnum.YYYY2));
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
        ChartPieDTO dto = new ChartPieDTO();
        if(CollectionUtils.isEmpty(dtoList)){
            return new Result<>(dto);
        }
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
    /**
     * <p>@Description 查询日销售数据排行榜 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId    用户ID
     * @param shopId    店铺ID
     * @param descField 排序的字段
     * @param chartList 图表数据枚举值集合
     * @return 日销售数据排行榜
     */
    @Override
    public Result<ChartRankDTO> findDayRank(String userId, Long shopId, String descField, List<ChartEnum> chartList) {
        //查询日销售数据排行榜
        List<DayDTO> dayList = daySalesDAO.findDayRank(userId, shopId, descField);
        if(CollectionUtils.isEmpty(dayList)){
            return new Result<>();
        }
        //根据查询的销售数据组装返回排行榜图表DTO数据
        return this.buildChartRankDTO(dayList,chartList);
    }
    /**
     * <p>@Description 查询月销售数据排行榜 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId    用户ID
     * @param shopId    店铺ID
     * @param descField 排序的字段
     * @param chartList 图表数据枚举值集合
     * @return 月销售数据排行榜
     */
    @Override
    public Result<ChartRankDTO> findMonthRank(String userId, Long shopId, String descField, List<ChartEnum> chartList) {
        //查询月销售数据排行榜
        List<MonthDTO> monthList = monthSalesDAO.findMonthRank(userId, shopId, descField);
        if(CollectionUtils.isEmpty(monthList)){
            return new Result<>();
        }
        //根据查询的销售数据组装返回排行榜图表DTO数据
        return this.buildChartRankDTO(monthList,chartList);
    }
    /**
     * <p>@Description 查询年销售数据排行榜 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId    用户ID
     * @param shopId    店铺ID
     * @param descField 排序的字段
     * @param chartList 图表数据枚举值集合
     * @return 年销售数据排行榜
     */
    @Override
    public Result<ChartRankDTO> findYearRank(String userId, Long shopId, String descField, List<ChartEnum> chartList) {
        //查询年销售数据排行榜
        List<YearDTO> yearList = yearSalesDAO.findYearRank(userId, shopId, descField);
        if(CollectionUtils.isEmpty(yearList)){
            return new Result<>();
        }
        //根据查询的销售数据组装返回排行榜图表DTO数据
        return this.buildChartRankDTO(yearList,chartList);
    }
    /**
     * <p>@Description 查询店铺销售数据排行榜 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:28 </p>
     * @param userId    用户ID
     * @param descField 排序的字段
     * @param chartList 图表数据枚举值集合
     * @return 店铺销售数据排行榜
     */
    @Override
    public Result<ChartRankDTO> findShopRank(String userId, String descField, List<ChartEnum> chartList) {
        //查询店铺销售数据排行榜
        List<ShopDTO> shopList = shopSalesDAO.findShopRank(userId, descField);
        if(CollectionUtils.isEmpty(shopList)){
            return new Result<>();
        }
        //根据查询的销售数据组装返回排行榜图表DTO数据
        return this.buildChartRankDTO(shopList,chartList);
    }
    /**
     * <p>@Description 根据查询的销售数据组装返回排行榜图表DTO数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/19 19:46 </p>
     * @param dataList 销售数据
     * @param chartList 图表数据枚举值集合
     * @return 销售数据排行榜
     */
    private Result<ChartRankDTO> buildChartRankDTO(List dataList, List<ChartEnum> chartList){
        ChartRankDTO rankDTO = new ChartRankDTO();
        List<String> yaxisList = new ArrayList<>();
        Map<ChartEnum,ChartRankDTO.Series> seriesMap = new HashMap<>();
        String shopName = null;
        //遍历需要组装的图表数据
        for (int i = 0; i < chartList.size(); i++) {
            ChartEnum enums = chartList.get(i);
            if(ChartEnum.getShopEnum().contains(enums) || ChartEnum.getYearEnum().contains(enums)
                    || ChartEnum.getMonthEnum().contains(enums) || ChartEnum.getDayEnum().contains(enums)){
                ChartRankDTO.Series series = new ChartRankDTO.Series();
                series.setEnums(enums).setName(enums.getName()).setData(new ArrayList<>());
                seriesMap.put(enums,series);
                //非店铺数据排行榜需要查询店铺名称
                if(ChartEnum.getYearEnum().contains(enums) || ChartEnum.getMonthEnum().contains(enums) || ChartEnum.getDayEnum().contains(enums)){
                    shopName = StringUtils.isNotBlank(((BaseDTO)dataList.get(0)).getShopName()) ? ((BaseDTO)dataList.get(0)).getShopName() : "所有店铺汇总";
                }
            }
        }
        rankDTO.setShopName(shopName);
        for (int i = dataList.size() -1; i >= 0; i--) {
            BaseDTO shopDTO = (BaseDTO) dataList.get(i);
            yaxisList.add(shopDTO.getLabel());
            seriesMap.forEach((enums,series) -> {
                BigDecimal data = BigDecimal.ZERO;
                if(enums.equals(ChartEnum.SHOP_PROFIT) || enums.equals(ChartEnum.YEAR_PROFIT) || enums.equals(ChartEnum.MONTH_PROFIT) || enums.equals(ChartEnum.DAY_PROFIT)){
                    data = BigDecimalUtils.nullToZero(shopDTO.getRetainedProfits());
                }else if(enums.equals(ChartEnum.SHOP_GROSS) || enums.equals(ChartEnum.YEAR_GROSS) || enums.equals(ChartEnum.MONTH_GROSS) || enums.equals(ChartEnum.DAY_GROSS)){
                    data = BigDecimalUtils.nullToZero(shopDTO.getRetainedProfits());
                }else if(enums.equals(ChartEnum.SHOP_TOTAL) || enums.equals(ChartEnum.YEAR_TOTAL) || enums.equals(ChartEnum.MONTH_TOTAL) || enums.equals(ChartEnum.DAY_TOTAL)){
                    data = shopDTO.getTotalOrder() != null ? new BigDecimal(shopDTO.getTotalOrder().toString()) : BigDecimal.ZERO;
                }else if(enums.equals(ChartEnum.SHOP_SUCCEED) || enums.equals(ChartEnum.YEAR_SUCCEED) || enums.equals(ChartEnum.MONTH_SUCCEED) || enums.equals(ChartEnum.DAY_SUCCEED)){
                    data = shopDTO.getSucceedOrder() != null ? new BigDecimal(shopDTO.getSucceedOrder().toString()) : BigDecimal.ZERO;
                }else if(enums.equals(ChartEnum.SHOP_FAILED) || enums.equals(ChartEnum.YEAR_FAILED) || enums.equals(ChartEnum.MONTH_FAILED) || enums.equals(ChartEnum.DAY_FAILED)){
                    data = shopDTO.getFailedOrder() != null ? new BigDecimal(shopDTO.getFailedOrder().toString()) : BigDecimal.ZERO;
                }else if(enums.equals(ChartEnum.SHOP_SALE) || enums.equals(ChartEnum.YEAR_SALE) || enums.equals(ChartEnum.MONTH_SALE) || enums.equals(ChartEnum.DAY_SALE)){
                    data = BigDecimalUtils.nullToZero(shopDTO.getSaleAmount());
                }else if(enums.equals(ChartEnum.SHOP_COST) || enums.equals(ChartEnum.YEAR_COST) || enums.equals(ChartEnum.MONTH_COST) || enums.equals(ChartEnum.DAY_COST)){
                    data = BigDecimalUtils.nullToZero(shopDTO.getCostAmount());
                }else if(enums.equals(ChartEnum.SHOP_ADVERT) || enums.equals(ChartEnum.YEAR_ADVERT) || enums.equals(ChartEnum.MONTH_ADVERT)){
                    data = BigDecimalUtils.nullToZero(shopDTO.getAdvertAmount());
                }else if(enums.equals(ChartEnum.SHOP_SERVICE) || enums.equals(ChartEnum.YEAR_SERVICE) || enums.equals(ChartEnum.MONTH_SERVICE)){
                    data = BigDecimalUtils.nullToZero(shopDTO.getServiceAmount());
                }else if(enums.equals(ChartEnum.SHOP_VIRTUAL) || enums.equals(ChartEnum.YEAR_VIRTUAL) || enums.equals(ChartEnum.MONTH_VIRTUAL) || enums.equals(ChartEnum.DAY_VIRTUAL)){
                    data = BigDecimalUtils.nullToZero(shopDTO.getVirtualAmount());
                }else if(enums.equals(ChartEnum.SHOP_PAYOUT) || enums.equals(ChartEnum.YEAR_PAYOUT) || enums.equals(ChartEnum.MONTH_PAYOUT) || enums.equals(ChartEnum.DAY_PAYOUT)){
                    data = BigDecimalUtils.nullToZero(shopDTO.getTotalCost());
                }
                series.getData().add(data);
            });
        }
        List<ChartRankDTO.Series> seriesList = seriesMap.values().stream().collect(Collectors.toList());
        rankDTO.setYaxis(yaxisList).setSeries(seriesList);
        return new Result<>(rankDTO);
    }
    /**
     * <p>@Description 获取查询的起始日期和截止日期 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/16 17:07 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param startDateStr 起始日期
     * @param endDateStr 截止日期
     * @param stepType 日期最大相差类型，使用Calendar类中的常量，只能是YEAR，MONTH，DATE
     * @param stepNum 日期最大相差数值，如几年、几个月、几天
     * @return 起始日期和截止日期的Map，MIN_DATE起始日期，MAX_DATE截止日期
     */
    private Map<String,Date> getDateRange(String userId, Long shopId, String startDateStr, String endDateStr,int stepType,int stepNum){
        Map<String,Date> dateMap = new HashMap<>();
        //获取订单中最大的日期
        Date maxDate = orderInfoDAO.getMaxOrderDate(userId,shopId);
        if(maxDate == null){
            return dateMap;
        }
        stepNum = Calendar.YEAR == stepType ? stepNum : stepNum;//日期最大相差类型是年份，需要对日期最大相差数值乘12处理
        Date startDate = DateUtils.parse(startDateStr,DateFormatEnum.YYYYMMDD1);
        Date endDate = DateUtils.parse(endDateStr,DateFormatEnum.YYYYMMDD1);
        if(Calendar.DATE == stepType){
            endDate = endDate == null ? maxDate : endDate;
            //起始日期为订单中最大的日期的前stepNum天日期
            startDate = startDate == null ? DateUtils.stepDay(endDate,-1*(stepNum-1)) : startDate;
        }else if(Calendar.MONTH == stepType){
            endDate = endDate == null ? DateUtils.monthLastDay(maxDate) : DateUtils.monthLastDay(endDate);
            //起始日期为订单中最大的日期的前stepNum个月日期
            startDate = startDate == null ? DateUtils.stepMonth(DateUtils.monthFirstDay(endDate),-1*(stepNum-1)) : DateUtils.monthFirstDay(startDate);
        }else if(Calendar.YEAR == stepType){
            endDate = endDate == null ? DateUtils.yearLastDay(maxDate) : DateUtils.yearLastDay(endDate);
            //起始日期为订单中最大的日期的前stepNum年日期
            startDate = startDate == null ? DateUtils.stepDate(DateUtils.yearFirstDay(endDate),Calendar.YEAR,-1*(stepNum-1)) : DateUtils.yearFirstDay(startDate);
        }
        //起始日期大于截止日期，则调换下
        if(startDate.compareTo(endDate) == 1){
            Date tempDate = startDate;
            startDate = endDate;
            endDate = tempDate;
        }
        if(Calendar.DATE == stepType){
            //起始日期和截止日期相差超过stepNum天，则起始日期设置为截止日期的前stepNum天日期
            if(DateUtils.getAbsDays(startDate,endDate) > stepNum){
                startDate = DateUtils.stepDay(endDate,-1*(stepNum-1));
            }
        }else if(Calendar.MONTH == stepType){
            //起始日期和截止日期相差超过stepNum个月，则起始日期设置为截止日期的前stepNum个月1日日期
            if(DateUtils.getAbsMonths(startDate,endDate) > stepNum){
                startDate = DateUtils.stepMonth(endDate,-1*(stepNum-1));
            }
        }else if(Calendar.YEAR == stepType){
            //起始日期和截止日期相差超过stepNum年，则起始日期设置为截止日期的前stepNum年1月1日日期
            if(DateUtils.getAbsMonths(startDate,endDate) > stepNum*12){
                startDate = DateUtils.stepDate(DateUtils.yearFirstDay(endDate),Calendar.YEAR,-1*(stepNum-1));
            }
        }
        dateMap.put(BizConstant.MIN_DATE,startDate);
        dateMap.put(BizConstant.MAX_DATE,endDate);
        return dateMap;
    }
}
