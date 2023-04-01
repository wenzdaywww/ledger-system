package com.www.ledger.controller.chart;

import com.www.common.config.security.meta.JwtAuthorizationTokenFilter;
import com.www.common.data.response.Result;
import com.www.ledger.data.dto.ChartDataDTO;
import com.www.ledger.data.enums.ChartEnum;
import com.www.ledger.data.vo.chart.ChartDataOutVO;
import com.www.ledger.service.chart.IChartService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @param shopId 店铺ID
     * @return 日销售利润图表数据
     */
    @GetMapping("daySales")
    public Result<ChartDataOutVO> findDaySales(Long shopId){
        List<ChartEnum> chartList = new ArrayList<>();
        chartList.add(ChartEnum.DAY_SALE);
        chartList.add(ChartEnum.DAY_COST);
        chartList.add(ChartEnum.DAY_PROFIT);
        Result<ChartDataDTO> resultDTO = chartService.findDayData(JwtAuthorizationTokenFilter.getUserId(),shopId,30,chartList);
        return new Result<>(this.buildChartDataOutVO(resultDTO.getData()));
    }
    /**
     * <p>@Description 查询日订单图表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/31 23:19 </p>
     * @param shopId 店铺ID
     * @return 日订单图表数据
     */
    @GetMapping("dayOrder")
    public Result<ChartDataOutVO> findDayOrder(Long shopId){
        List<ChartEnum> chartList = new ArrayList<>();
        chartList.add(ChartEnum.DAY_TOTAL);
        chartList.add(ChartEnum.DAY_SUCCEED);
        chartList.add(ChartEnum.DAY_FAILED);
        Result<ChartDataDTO> resultDTO = chartService.findDayData(JwtAuthorizationTokenFilter.getUserId(),shopId,30,chartList);
        return new Result<>(this.buildChartDataOutVO(resultDTO.getData()));
    }
    /**
     * <p>@Description 查询月销售图表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/31 23:19 </p>
     * @param shopId 店铺ID
     * @return 月销售图表数据
     */
    @GetMapping("monthSales")
    public Result<ChartDataOutVO> findMonthSales(Long shopId){
        List<ChartEnum> chartList = new ArrayList<>();
        chartList.add(ChartEnum.MONTH_SALE);
        chartList.add(ChartEnum.MONTH_PAYOUT);
        chartList.add(ChartEnum.MONTH_PROFIT);
        Result<ChartDataDTO> resultDTO = chartService.findMonthData(JwtAuthorizationTokenFilter.getUserId(),shopId,chartList);
        return new Result<>(this.buildChartDataOutVO(resultDTO.getData()));
    }
    /**
     * <p>@Description 查询月订单图表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/31 23:19 </p>
     * @param shopId 店铺ID
     * @return 月订单图表数据
     */
    @GetMapping("monthOrder")
    public Result<ChartDataOutVO> findMonthOrder(Long shopId){
        List<ChartEnum> chartList = new ArrayList<>();
        chartList.add(ChartEnum.MONTH_TOTAL);
        chartList.add(ChartEnum.MONTH_SUCCEED);
        chartList.add(ChartEnum.MONTH_FAILED);
        Result<ChartDataDTO> resultDTO = chartService.findMonthData(JwtAuthorizationTokenFilter.getUserId(),shopId,chartList);
        return new Result<>(this.buildChartDataOutVO(resultDTO.getData()));
    }
    /**
     * <p>@Description 查询年销售图表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/31 23:19 </p>
     * @param shopId 店铺ID
     * @return 年销售图表数据
     */
    @GetMapping("yearSales")
    public Result<ChartDataOutVO> findYearSales(Long shopId){
        List<ChartEnum> chartList = new ArrayList<>();
        chartList.add(ChartEnum.YEAR_SALE);
        chartList.add(ChartEnum.YEAR_PAYOUT);
        chartList.add(ChartEnum.YEAR_PROFIT);
        Result<ChartDataDTO> resultDTO = chartService.findYearData(JwtAuthorizationTokenFilter.getUserId(),shopId,chartList);
        return new Result<>(this.buildChartDataOutVO(resultDTO.getData()));
    }
    /**
     * <p>@Description 查询年订单图表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/31 23:19 </p>
     * @param shopId 店铺ID
     * @return 年订单图表数据
     */
    @GetMapping("yearOrder")
    public Result<ChartDataOutVO> findYearOrder(Long shopId){
        List<ChartEnum> chartList = new ArrayList<>();
        chartList.add(ChartEnum.YEAR_TOTAL);
        chartList.add(ChartEnum.YEAR_SUCCEED);
        chartList.add(ChartEnum.YEAR_FAILED);
        Result<ChartDataDTO> resultDTO = chartService.findYearData(JwtAuthorizationTokenFilter.getUserId(),shopId,chartList);
        return new Result<>(this.buildChartDataOutVO(resultDTO.getData()));
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
            List<ChartDataOutVO.Datasets> dataVOList = new ArrayList<>();
            tempVO.setLabels(dto.getLabels());
            if(CollectionUtils.isNotEmpty(dto.getDatasets())){
                dto.getDatasets().forEach(datasetsDTO -> {
                    ChartDataOutVO.Datasets datasetsVO = new ChartDataOutVO.Datasets();
                    datasetsVO.setLabel(datasetsDTO.getLabel()).setData(datasetsDTO.getData());
                    dataVOList.add(datasetsVO);
                });
            }
            tempVO.setDatasets(dataVOList);
            return tempVO;
        }).orElse(null);
        return outVO;
    }
}
