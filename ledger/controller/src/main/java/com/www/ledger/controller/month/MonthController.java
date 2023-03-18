package com.www.ledger.controller.month;

import com.www.common.config.security.meta.JwtAuthorizationTokenFilter;
import com.www.common.data.response.Response;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.vo.month.MonthListRequest;
import com.www.ledger.data.vo.month.MonthListResponse;
import com.www.ledger.data.vo.month.MonthNewRequest;
import com.www.ledger.service.month.IMonthService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * <p>@Description 月销售额controller </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/11 19:20 </p>
 */
@RestController
@RequestMapping("month")
public class MonthController {
    @Autowired
    private IMonthService monthService;
    /**
     * <p>@Description 编辑月销售额数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 18:24 </p>
     * @param monthRequest 月销售额数据
     * @return
     */
    @PostMapping("new")
    public Response<String> saveMonthSales(@Validated MonthNewRequest monthRequest){
        MonthDTO monthDTO = new MonthDTO();
        monthDTO.setUserId(JwtAuthorizationTokenFilter.getUserId())
                .setMonthDateStr(monthRequest.getMonth()).setShopId(monthRequest.getShopId())
                .setAdvertAmount(monthRequest.getAdvAmt()).setServiceAmount(monthRequest.getSerAmt());
        return monthService.saveMonthSales(monthDTO);
    }
    /**
     * <p>@Description 删除月销售额数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 18:21 </p>
     * @param msId 月销售额ID
     * @return
     */
    @PostMapping("dlt/{msId}")
    public Response<String> deleteMonthData(@PathVariable("msId") Long msId){
        return monthService.deleteMonthData(JwtAuthorizationTokenFilter.getUserId(),msId);
    }
    /**
     * <p>@Description 统计月销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 14:00 </p>
     * @return
     */
    @PostMapping("tal")
    public Response<String> saveMonthList(){
        return monthService.saveAndCountMonthData(JwtAuthorizationTokenFilter.getUserId());
    }
    /**
     * <p>@Description 查询月销售额列表信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 14:07 </p>
     * @param monthRequest 查询条件
     * @return
     */
    @GetMapping("list")
    public Response<List<MonthListResponse>> findMonthList(@Validated MonthListRequest monthRequest){
        MonthDTO monthDTO = new MonthDTO();
        monthDTO.setUserId(JwtAuthorizationTokenFilter.getUserId())
                .setMonthDateStr(monthRequest.getMonth()).setShopId(monthRequest.getShopId());
        monthDTO.setMonthDateStr(StringUtils.isNotBlank(monthDTO.getMonthDateStr()) ? monthDTO.getMonthDateStr() + "-01" : null);//月份设置每月第一天
        Response<List<MonthDTO>> dtoResponse = monthService.findMonthList(monthDTO,monthRequest.getPageNum(),monthRequest.getPageSize());
        List<MonthListResponse> monthList = Optional.ofNullable(dtoResponse.getData()).filter(e -> CollectionUtils.isNotEmpty(dtoResponse.getData()))
                .map(list -> {
                    List<MonthListResponse> tempList = new ArrayList<>();
                    list.forEach(dto -> {
                        MonthListResponse month = new MonthListResponse();
                        month.setMonth(dto.getMonthDateStr()).setShopNm(dto.getShopName())
                                .setMsId(dto.getMsId()).setShopId(dto.getShopId())
                                .setRetPro(dto.getRetainedProfits()).setRetProRat(dto.getRetainedProfitsRate())
                                .setGroPro(dto.getGrossProfit()).setGroProRat(dto.getGrossProfitRate())
                                .setTalOrd(dto.getTotalOrder()).setSucOrd(dto.getSucceedOrder())
                                .setFaiOrd(dto.getFailedOrder()).setSalAmt(dto.getSaleAmount())
                                .setCosAmt(dto.getCostAmount()).setAdvAmt(dto.getAdvertAmount())
                                .setSerAmt(dto.getServiceAmount()).setVirAmt(dto.getVirtualAmount());
                        tempList.add(month);
                    });
                    return tempList;
                }).orElse(null);
        return new Response<>(dtoResponse,monthList);
    }
}
