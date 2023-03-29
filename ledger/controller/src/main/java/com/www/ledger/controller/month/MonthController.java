package com.www.ledger.controller.month;

import com.www.common.config.security.meta.JwtAuthorizationTokenFilter;
import com.www.common.data.response.Result;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.vo.month.MonthListInVO;
import com.www.ledger.data.vo.month.MonthListOutVO;
import com.www.ledger.service.month.IMonthService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
     * <p>@Description 统计月销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 14:00 </p>
     * @return
     */
    @PostMapping("tal")
    public Result<String> saveMonthList(){
        return monthService.saveAndCountMonthData(JwtAuthorizationTokenFilter.getUserId());
    }
    /**
     * <p>@Description 查询月销售额列表信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 14:07 </p>
     * @param monthInVO 查询条件
     * @return
     */
    @GetMapping("list")
    public Result<List<MonthListOutVO>> findMonthList(@Validated MonthListInVO monthInVO){
        MonthDTO monthDTO = new MonthDTO();
        monthDTO.setUserId(JwtAuthorizationTokenFilter.getUserId()).setAllShop(monthInVO.getAll())
                .setMonthDateStr(monthInVO.getMonth()).setShopId(monthInVO.getShopId());
        monthDTO.setMonthDateStr(StringUtils.isNotBlank(monthDTO.getMonthDateStr()) ? monthDTO.getMonthDateStr() + "-01" : null);//月份设置每月第一天
        Result<List<MonthDTO>> listResult = monthService.findMonthList(monthDTO,monthInVO.getPageNum(),monthInVO.getPageSize());
        List<MonthListOutVO> monthList = Optional.ofNullable(listResult.getData()).filter(e -> CollectionUtils.isNotEmpty(listResult.getData()))
                .map(list -> {
                    List<MonthListOutVO> tempList = new ArrayList<>();
                    list.forEach(dto -> {
                        MonthListOutVO month = new MonthListOutVO();
                        month.setMonth(dto.getMonthDateStr()).setShopNm(dto.getShopName())
                                .setMsId(dto.getMsId()).setShopId(dto.getShopId())
                                .setRetPro(dto.getRetainedProfits()).setRetProRat(dto.getRetainedProfitsRate())
                                .setGroPro(dto.getGrossProfit()).setGroProRat(dto.getGrossProfitRate())
                                .setTalOrd(dto.getTotalOrder()).setSucOrd(dto.getSucceedOrder())
                                .setFaiOrd(dto.getFailedOrder()).setSalAmt(dto.getSaleAmount())
                                .setCosAmt(dto.getCostAmount()).setAdvAmt(dto.getAdvertAmount())
                                .setSerAmt(dto.getServiceAmount()).setVirAmt(dto.getVirtualAmount())
                                .setTalCos(dto.getTotalCost());
                        tempList.add(month);
                    });
                    return tempList;
                }).orElse(null);
        return new Result<>(listResult,monthList);
    }
}
