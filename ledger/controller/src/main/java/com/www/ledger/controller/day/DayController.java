package com.www.ledger.controller.day;

import com.www.common.config.security.meta.JwtAuthorizationTokenFilter;
import com.www.common.data.response.Result;
import com.www.ledger.data.dto.DayDTO;
import com.www.ledger.data.vo.day.DayListInVO;
import com.www.ledger.data.vo.day.DayListOutVO;
import com.www.ledger.service.day.IDayService;
import org.apache.commons.collections4.CollectionUtils;
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
 * <p>@Description 年销售额controller </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/11 19:20 </p>
 */
@RestController
@RequestMapping("day")
public class DayController {
    @Autowired
    private IDayService dayService;

    /**
     * <p>@Description 统计日销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 20:09 </p>
     * @return
     */
    @PostMapping("tal")
    public Result<String> saveDayData(){
        return dayService.saveAndCountDayData(JwtAuthorizationTokenFilter.getUserId());
    }
    /**
     * <p>@Description 查询我的日销售额列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 20:07 </p>
     * @param dayInVO 查询条件
     * @return 日销售额列表数据
     */
    @GetMapping("list")
    public Result<List<DayListOutVO>> findDayList(@Validated DayListInVO dayInVO){
        DayDTO dayDTO = new DayDTO();
        dayDTO.setUserId(JwtAuthorizationTokenFilter.getUserId()).setShopId(dayInVO.getShopId())
                .setStartDateStr(dayInVO.getStrDat()).setEndDateStr(dayInVO.getEndDat()).setAllShop(dayInVO.getAll());
        Result<List<DayDTO>> listResult = dayService.findDayList(dayDTO,dayInVO.getPageNum(),dayInVO.getPageSize());
        List<DayListOutVO> dayList = Optional.ofNullable(listResult.getData()).filter(e -> CollectionUtils.isNotEmpty(listResult.getData()))
                .map(list -> {
                    List<DayListOutVO> tempList = new ArrayList<>();
                    list.forEach(dto -> {
                        DayListOutVO day = new DayListOutVO();
                        day.setDay(dto.getDayDateStr()).setShopNm(dto.getShopName())
                                .setRetPro(dto.getRetainedProfits()).setRetProRat(dto.getRetainedProfitsRate())
                                .setGroPro(dto.getGrossProfit()).setGroProRat(dto.getGrossProfitRate())
                                .setTalOrd(dto.getTotalOrder()).setSucOrd(dto.getSucceedOrder())
                                .setFaiOrd(dto.getFailedOrder()).setSalAmt(dto.getSaleAmount())
                                .setCosAmt(dto.getCostAmount()).setVirAmt(dto.getVirtualAmount())
                                .setTalCos(dto.getTotalCost());
                        tempList.add(day);
                    });
                    return tempList;
                }).orElse(null);
        return new Result<>(listResult,dayList);
    }
}
