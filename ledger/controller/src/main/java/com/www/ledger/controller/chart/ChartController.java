package com.www.ledger.controller.chart;

import com.www.common.config.security.meta.JwtAuthorizationTokenFilter;
import com.www.common.data.response.Result;
import com.www.ledger.data.dto.ChartDataDTO;
import com.www.ledger.data.dto.ChartPieDTO;
import com.www.ledger.data.dto.ChartRankDTO;
import com.www.ledger.data.enums.ChartEnum;
import com.www.ledger.data.vo.chart.ChartDataInVO;
import com.www.ledger.data.vo.chart.ChartDataOutVO;
import com.www.ledger.data.vo.chart.ChartPieOutVO;
import com.www.ledger.data.vo.chart.ChartRankOutVO;
import com.www.ledger.service.chart.IChartService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>@Description 图表数据查询Controller </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/31 23:08 </p>
 */
@RestController
@RequestMapping("chart")
public class ChartController {
    @Autowired
    private IChartService chartService;

    /**
     * <p>@Description 查询日销售利润图表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/31 23:19 </p>
     * @param inVO 查询条件
     * @return 日销售利润图表数据
     */
    @GetMapping("daySales")
    public Result<ChartDataOutVO> findDaySales(ChartDataInVO inVO){
        List<ChartEnum> chartList = new ArrayList<>();
        chartList.add(ChartEnum.DAY_SALE);
        chartList.add(ChartEnum.DAY_COST);
        chartList.add(ChartEnum.DAY_GROSS);
        Result<ChartDataDTO> resultDTO = chartService.findDayData(JwtAuthorizationTokenFilter.getUserId(),inVO.getShopId(),inVO.getStartDate(),inVO.getEndDate(),chartList);
        return new Result<>(this.buildChartDataOutVO(resultDTO.getData()));
    }
    /**
     * <p>@Description 查询日订单图表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/31 23:19 </p>
     * @param inVO 查询条件
     * @return 日订单图表数据
     */
    @GetMapping("dayOrder")
    public Result<ChartDataOutVO> findDayOrder(ChartDataInVO inVO){
        List<ChartEnum> chartList = new ArrayList<>();
        chartList.add(ChartEnum.DAY_TOTAL);
        chartList.add(ChartEnum.DAY_SUCCEED);
        chartList.add(ChartEnum.DAY_FAILED);
        Result<ChartDataDTO> resultDTO = chartService.findDayData(JwtAuthorizationTokenFilter.getUserId(),inVO.getShopId(),inVO.getStartDate(),inVO.getEndDate(),chartList);
        return new Result<>(this.buildChartDataOutVO(resultDTO.getData()));
    }
    /**
     * <p>@Description 查询日订单交易状态数量饼状图表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/31 23:19 </p>
     * @param inVO 查询条件
     * @return 日订单交易状态数量饼状图表数据
     */
    @GetMapping("dayState")
    public Result<ChartPieOutVO> findDayState(ChartDataInVO inVO){
        Result<ChartPieDTO> dtoResult = chartService.findDayOrderState(JwtAuthorizationTokenFilter.getUserId(),inVO.getShopId(),inVO.getStartDate(),inVO.getEndDate());
        ChartPieOutVO outVO = new ChartPieOutVO();
        BigDecimal total = Optional.ofNullable(dtoResult).filter(e -> dtoResult.getData() != null)
                .map(d -> d.getData().getTotal()).orElse(null);
        String startDate = Optional.ofNullable(dtoResult).filter(e -> dtoResult.getData() != null)
                .map(d -> d.getData().getStartDate()).orElse(null);
        String endDate = Optional.ofNullable(dtoResult).filter(e -> dtoResult.getData() != null)
                .map(d -> d.getData().getEndDate()).orElse(null);
        List<ChartPieOutVO.Series> series = Optional.ofNullable(dtoResult)
                .filter(e -> dtoResult.getData() != null && CollectionUtils.isNotEmpty(dtoResult.getData().getSeries()))
                .map(dto -> {
                    List<ChartPieOutVO.Series> tempList = new ArrayList<>();
                    dto.getData().getSeries().forEach(item -> {
                        ChartPieOutVO.Series stateData = new ChartPieOutVO.Series();
                        stateData.setName(item.getName()).setValue(item.getValue());
                        tempList.add(stateData);
                    });
                    return tempList;
                }).orElse(null);
        outVO.setTotal(total).setStartDate(startDate).setEndDate(endDate).setSeries(series);
        return new Result<>(outVO);
    }
    /**
     * <p>@Description 查询日销售额排行榜 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/31 23:19 </p>
     * @param inVO 查询条件
     * @return 日销售额排行榜
     */
    @GetMapping("daySalesRank")
    public Result<ChartRankOutVO> findDaySalesRank(ChartDataInVO inVO){
        List<ChartEnum> chartList = new ArrayList<>();
        chartList.add(ChartEnum.DAY_SALE);
        chartList.add(ChartEnum.DAY_COST);
        chartList.add(ChartEnum.DAY_GROSS);
        Result<ChartRankDTO> resultDTO = chartService.findDayRank(JwtAuthorizationTokenFilter.getUserId(),inVO.getShopId(),ChartEnum.DAY_SALE.getField(),chartList);
        return new Result<>(this.buildChartRankOutVO(resultDTO.getData()));
    }
    /**
     * <p>@Description 查询日订单量排行榜 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/31 23:19 </p>
     * @param inVO 查询条件
     * @return 日订单量排行榜
     */
    @GetMapping("dayOrderRank")
    public Result<ChartRankOutVO> findDayOrderRank(ChartDataInVO inVO){
        List<ChartEnum> chartList = new ArrayList<>();
        chartList.add(ChartEnum.DAY_TOTAL);
        chartList.add(ChartEnum.DAY_SUCCEED);
        chartList.add(ChartEnum.DAY_FAILED);
        Result<ChartRankDTO> resultDTO = chartService.findDayRank(JwtAuthorizationTokenFilter.getUserId(),inVO.getShopId(),ChartEnum.DAY_TOTAL.getField(),chartList);
        return new Result<>(this.buildChartRankOutVO(resultDTO.getData()));
    }
    /**
     * <p>@Description 查询月销售图表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/31 23:19 </p>
     * @param inVO 查询条件
     * @return 月销售图表数据
     */
    @GetMapping("monthSales")
    public Result<ChartDataOutVO> findMonthSales(ChartDataInVO inVO){
        List<ChartEnum> chartList = new ArrayList<>();
        chartList.add(ChartEnum.MONTH_SALE);
        chartList.add(ChartEnum.MONTH_PAYOUT);
        chartList.add(ChartEnum.MONTH_PROFIT);
        Result<ChartDataDTO> resultDTO = chartService.findMonthData(JwtAuthorizationTokenFilter.getUserId(),inVO.getShopId(),inVO.getStartDate(),inVO.getEndDate(),chartList);
        return new Result<>(this.buildChartDataOutVO(resultDTO.getData()));
    }
    /**
     * <p>@Description 查询月订单图表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/31 23:19 </p>
     * @param inVO 查询条件
     * @return 月订单图表数据
     */
    @GetMapping("monthOrder")
    public Result<ChartDataOutVO> findMonthOrder(ChartDataInVO inVO){
        List<ChartEnum> chartList = new ArrayList<>();
        chartList.add(ChartEnum.MONTH_TOTAL);
        chartList.add(ChartEnum.MONTH_SUCCEED);
        chartList.add(ChartEnum.MONTH_FAILED);
        Result<ChartDataDTO> resultDTO = chartService.findMonthData(JwtAuthorizationTokenFilter.getUserId(),inVO.getShopId(),inVO.getStartDate(),inVO.getEndDate(),chartList);
        return new Result<>(this.buildChartDataOutVO(resultDTO.getData()));
    }
    /**
     * <p>@Description 查询月订单交易状态数量饼状图表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/31 23:19 </p>
     * @param inVO 查询条件
     * @return 月订单交易状态数量饼状图表数据
     */
    @GetMapping("monthState")
    public Result<ChartPieOutVO> findMonthState(ChartDataInVO inVO){
        Result<ChartPieDTO> dtoResult = chartService.findMonthOrderState(JwtAuthorizationTokenFilter.getUserId(),inVO.getShopId(),inVO.getStartDate(),inVO.getEndDate());
        ChartPieOutVO outVO = new ChartPieOutVO();
        BigDecimal total = Optional.ofNullable(dtoResult).filter(e -> dtoResult.getData() != null)
                .map(d -> d.getData().getTotal()).orElse(null);
        String startDate = Optional.ofNullable(dtoResult).filter(e -> dtoResult.getData() != null)
                .map(d -> d.getData().getStartDate()).orElse(null);
        String endDate = Optional.ofNullable(dtoResult).filter(e -> dtoResult.getData() != null)
                .map(d -> d.getData().getEndDate()).orElse(null);
        List<ChartPieOutVO.Series> series = Optional.ofNullable(dtoResult)
                .filter(e -> dtoResult.getData() != null && CollectionUtils.isNotEmpty(dtoResult.getData().getSeries()))
                .map(dto -> {
                    List<ChartPieOutVO.Series> tempList = new ArrayList<>();
                    dto.getData().getSeries().forEach(item -> {
                        ChartPieOutVO.Series stateData = new ChartPieOutVO.Series();
                        stateData.setName(item.getName()).setValue(item.getValue());
                        tempList.add(stateData);
                    });
                    return tempList;
                }).orElse(null);
        outVO.setTotal(total).setStartDate(startDate).setEndDate(endDate).setSeries(series);
        return new Result<>(outVO);
    }
    /**
     * <p>@Description 查询月销售额排行榜 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/31 23:19 </p>
     * @param inVO 查询条件
     * @return 月销售额排行榜
     */
    @GetMapping("monthSalesRank")
    public Result<ChartRankOutVO> findMonthSalesRank(ChartDataInVO inVO){
        List<ChartEnum> chartList = new ArrayList<>();
        chartList.add(ChartEnum.MONTH_SALE);
        chartList.add(ChartEnum.MONTH_COST);
        chartList.add(ChartEnum.MONTH_GROSS);
        Result<ChartRankDTO> resultDTO = chartService.findMonthRank(JwtAuthorizationTokenFilter.getUserId(),inVO.getShopId(),ChartEnum.MONTH_SALE.getField(),chartList);
        return new Result<>(this.buildChartRankOutVO(resultDTO.getData()));
    }
    /**
     * <p>@Description 查询月订单量排行榜 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/31 23:19 </p>
     * @param inVO 查询条件
     * @return 月订单量排行榜
     */
    @GetMapping("monthOrderRank")
    public Result<ChartRankOutVO> findMonthOrderRank(ChartDataInVO inVO){
        List<ChartEnum> chartList = new ArrayList<>();
        chartList.add(ChartEnum.MONTH_TOTAL);
        chartList.add(ChartEnum.MONTH_SUCCEED);
        chartList.add(ChartEnum.MONTH_FAILED);
        Result<ChartRankDTO> resultDTO = chartService.findMonthRank(JwtAuthorizationTokenFilter.getUserId(),inVO.getShopId(),ChartEnum.MONTH_TOTAL.getField(),chartList);
        return new Result<>(this.buildChartRankOutVO(resultDTO.getData()));
    }
    /**
     * <p>@Description 查询年销售图表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/31 23:19 </p>
     * @param inVO 查询条件
     * @return 年销售图表数据
     */
    @GetMapping("yearSales")
    public Result<ChartDataOutVO> findYearSales(ChartDataInVO inVO){
        List<ChartEnum> chartList = new ArrayList<>();
        chartList.add(ChartEnum.YEAR_SALE);
        chartList.add(ChartEnum.YEAR_PAYOUT);
        chartList.add(ChartEnum.YEAR_PROFIT);
        Result<ChartDataDTO> resultDTO = chartService.findYearData(JwtAuthorizationTokenFilter.getUserId(),inVO.getShopId(),inVO.getStartDate(),inVO.getEndDate(),chartList);
        return new Result<>(this.buildChartDataOutVO(resultDTO.getData()));
    }
    /**
     * <p>@Description 查询年订单图表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/31 23:19 </p>
     * @param inVO 查询条件
     * @return 年订单图表数据
     */
    @GetMapping("yearOrder")
    public Result<ChartDataOutVO> findYearOrder(ChartDataInVO inVO){
        List<ChartEnum> chartList = new ArrayList<>();
        chartList.add(ChartEnum.YEAR_TOTAL);
        chartList.add(ChartEnum.YEAR_SUCCEED);
        chartList.add(ChartEnum.YEAR_FAILED);
        Result<ChartDataDTO> resultDTO = chartService.findYearData(JwtAuthorizationTokenFilter.getUserId(),inVO.getShopId(),inVO.getStartDate(),inVO.getEndDate(),chartList);
        return new Result<>(this.buildChartDataOutVO(resultDTO.getData()));
    }
    /**
     * <p>@Description 查询年订单交易状态数量饼状图表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/31 23:19 </p>
     * @param inVO 查询条件
     * @return 年订单交易状态数量饼状图表数据
     */
    @GetMapping("yearState")
    public Result<ChartPieOutVO> findYearState(ChartDataInVO inVO){
        Result<ChartPieDTO> dtoResult = chartService.findYearOrderState(JwtAuthorizationTokenFilter.getUserId(),inVO.getShopId(),inVO.getStartDate(),inVO.getEndDate());
        ChartPieOutVO outVO = new ChartPieOutVO();
        BigDecimal total = Optional.ofNullable(dtoResult).filter(e -> dtoResult.getData() != null)
                .map(d -> d.getData().getTotal()).orElse(null);
        String startDate = Optional.ofNullable(dtoResult).filter(e -> dtoResult.getData() != null)
                .map(d -> d.getData().getStartDate()).orElse(null);
        String endDate = Optional.ofNullable(dtoResult).filter(e -> dtoResult.getData() != null)
                .map(d -> d.getData().getEndDate()).orElse(null);
        List<ChartPieOutVO.Series> series = Optional.ofNullable(dtoResult)
                .filter(e -> dtoResult.getData() != null && CollectionUtils.isNotEmpty(dtoResult.getData().getSeries()))
                .map(dto -> {
                    List<ChartPieOutVO.Series> tempList = new ArrayList<>();
                    dto.getData().getSeries().forEach(item -> {
                        ChartPieOutVO.Series stateData = new ChartPieOutVO.Series();
                        stateData.setName(item.getName()).setValue(item.getValue());
                        tempList.add(stateData);
                    });
                    return tempList;
                }).orElse(null);
        outVO.setTotal(total).setStartDate(startDate).setEndDate(endDate).setSeries(series);
        return new Result<>(outVO);
    }
    /**
     * <p>@Description 查询年销售额排行榜 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/31 23:19 </p>
     * @param inVO 查询条件
     * @return 年销售额排行榜
     */
    @GetMapping("yearSalesRank")
    public Result<ChartRankOutVO> findYearSalesRank(ChartDataInVO inVO){
        List<ChartEnum> chartList = new ArrayList<>();
        chartList.add(ChartEnum.YEAR_SALE);
        chartList.add(ChartEnum.YEAR_COST);
        chartList.add(ChartEnum.YEAR_GROSS);
        Result<ChartRankDTO> resultDTO = chartService.findYearRank(JwtAuthorizationTokenFilter.getUserId(),inVO.getShopId(),ChartEnum.YEAR_SALE.getField(),chartList);
        return new Result<>(this.buildChartRankOutVO(resultDTO.getData()));
    }
    /**
     * <p>@Description 查询年订单量排行榜 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/31 23:19 </p>
     * @param inVO 查询条件
     * @return 年订单量排行榜
     */
    @GetMapping("yearOrderRank")
    public Result<ChartRankOutVO> findYearOrderRank(ChartDataInVO inVO){
        List<ChartEnum> chartList = new ArrayList<>();
        chartList.add(ChartEnum.YEAR_TOTAL);
        chartList.add(ChartEnum.YEAR_SUCCEED);
        chartList.add(ChartEnum.YEAR_FAILED);
        Result<ChartRankDTO> resultDTO = chartService.findYearRank(JwtAuthorizationTokenFilter.getUserId(),inVO.getShopId(),ChartEnum.YEAR_TOTAL.getField(),chartList);
        return new Result<>(this.buildChartRankOutVO(resultDTO.getData()));
    }
    /**
     * <p>@Description 查询店铺销售额排行榜 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/31 23:19 </p>
     * @return 店铺销售额排行榜
     */
    @GetMapping("shopSalesRank")
    public Result<ChartRankOutVO> findShopSalesRank(){
        List<ChartEnum> chartList = new ArrayList<>();
        chartList.add(ChartEnum.SHOP_SALE);
        chartList.add(ChartEnum.SHOP_COST);
        chartList.add(ChartEnum.SHOP_GROSS);
        Result<ChartRankDTO> resultDTO = chartService.findShopRank(JwtAuthorizationTokenFilter.getUserId(),ChartEnum.SHOP_SALE.getField(),chartList);
        return new Result<>(this.buildChartRankOutVO(resultDTO.getData()));
    }
    /**
     * <p>@Description 查询店铺订单量排行榜 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/31 23:19 </p>
     * @return 店铺订单量排行榜
     */
    @GetMapping("shopOrderRank")
    public Result<ChartRankOutVO> findShopOrderRank(){
        List<ChartEnum> chartList = new ArrayList<>();
        chartList.add(ChartEnum.SHOP_TOTAL);
        chartList.add(ChartEnum.SHOP_SUCCEED);
        chartList.add(ChartEnum.SHOP_FAILED);
        Result<ChartRankDTO> resultDTO = chartService.findShopRank(JwtAuthorizationTokenFilter.getUserId(),ChartEnum.SHOP_TOTAL.getField(),chartList);
        return new Result<>(this.buildChartRankOutVO(resultDTO.getData()));
    }
    /**
     * <p>@Description dto图表数据转为vo图表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:53 </p>
     * @param dataDTO dto图表数据
     * @return vo图表数据
     */
    private ChartDataOutVO buildChartDataOutVO(ChartDataDTO dataDTO){
        ChartDataOutVO outVO = Optional.ofNullable(dataDTO).map(dto -> {
            ChartDataOutVO tempVO = new ChartDataOutVO();
            List<ChartDataOutVO.Series> dataVOList = new ArrayList<>();
            tempVO.setStartDate(dto.getStartDate()).setEndDate(dto.getEndDate()).setXaxis(dto.getXaxis());
            if(CollectionUtils.isNotEmpty(dto.getSeries())){
                dto.getSeries().forEach(series -> {
                    ChartDataOutVO.Series datasetsVO = new ChartDataOutVO.Series();
                    datasetsVO.setName(series.getName()).setData(series.getData());
                    dataVOList.add(datasetsVO);
                });
            }
            tempVO.setSeries(dataVOList);
            return tempVO;
        }).orElse(null);
        return outVO;
    }
    /**
     * <p>@Description dto图表数据转为vo图表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/1 00:53 </p>
     * @param dataDTO dto图表数据
     * @return vo图表数据
     */
    private ChartRankOutVO buildChartRankOutVO(ChartRankDTO dataDTO){
        ChartRankOutVO outVO = Optional.ofNullable(dataDTO).map(dto -> {
            ChartRankOutVO tempVO = new ChartRankOutVO();
            tempVO.setShopName(dto.getShopName()).setYaxis(dto.getYaxis());
            List<ChartRankOutVO.Series> seriesVOList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(dto.getSeries())){
                dto.getSeries().forEach(seriesDTO -> {
                    ChartRankOutVO.Series seriesVO = new ChartRankOutVO.Series();
                    seriesVO.setName(seriesDTO.getName()).setData(seriesDTO.getData());
                    seriesVOList.add(seriesVO);
                });
            }
            tempVO.setSeries(seriesVOList);
            return tempVO;
        }).orElse(null);
        return outVO;
    }
}
