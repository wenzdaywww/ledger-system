package com.www.ledger.controller.month;

import com.www.common.config.code.CodeDict;
import com.www.common.config.security.meta.JwtAuthorizationTokenFilter;
import com.www.common.data.enums.ResponseEnum;
import com.www.common.data.response.Response;
import com.www.common.utils.MoneyUtils;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.enums.CodeTypeEnum;
import com.www.ledger.data.vo.month.MonthAmtInVO;
import com.www.ledger.data.vo.month.MonthListInVO;
import com.www.ledger.data.vo.month.MonthListOutVO;
import com.www.ledger.data.vo.month.MonthNewInVO;
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

import java.math.BigDecimal;
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
     * <p>@Description 增加/减少月销售推广及服务费用  </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 16:09 </p>
     * @param amtInVO 费用信息
     * @return
     */
    @PostMapping("amt")
    public Response<String> updateMonthAmt(@Validated MonthAmtInVO amtInVO){
        if(CodeDict.isIllegalValue(CodeTypeEnum.YesOrNo_No.getType(), amtInVO.getType())){
            return new Response<>(ResponseEnum.FAIL,"操作类型错误");
        }
        MonthDTO monthDTO = new MonthDTO();
        monthDTO.setMsId(amtInVO.getMsId()).setUserId(JwtAuthorizationTokenFilter.getUserId());
        monthDTO.setAdvertAmount(MoneyUtils.nullToZero(amtInVO.getAdvStep()))
                .setServiceAmount(MoneyUtils.nullToZero(amtInVO.getSerStep()));
        //操作类型是否，即为减少费用,则直接成负数-1
        if(StringUtils.equals(amtInVO.getType(),CodeDict.getValue(CodeTypeEnum.YesOrNo_No.getType(),CodeTypeEnum.YesOrNo_No.getKey()))){
            monthDTO.setAdvertAmount(monthDTO.getAdvertAmount().multiply(new BigDecimal("-1")))
                    .setServiceAmount(monthDTO.getServiceAmount().multiply(new BigDecimal("-1")));
        }
        return monthService.updateMonthAmt(monthDTO);
    }
    /**
     * <p>@Description 编辑月销售额数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 18:24 </p>
     * @param newInVO 月销售额数据
     * @return
     */
    @PostMapping("new")
    public Response<String> saveMonthSales(@Validated MonthNewInVO newInVO){
        MonthDTO monthDTO = new MonthDTO();
        monthDTO.setUserId(JwtAuthorizationTokenFilter.getUserId()).setMsId(newInVO.getMsId())
                .setMonthDateStr(newInVO.getMonth()).setShopId(newInVO.getShopId())
                .setAdvertAmount(newInVO.getAdvAmt()).setServiceAmount(newInVO.getSerAmt());
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
     * @param monthInVO 查询条件
     * @return
     */
    @GetMapping("list")
    public Response<List<MonthListOutVO>> findMonthList(@Validated MonthListInVO monthInVO){
        MonthDTO monthDTO = new MonthDTO();
        monthDTO.setUserId(JwtAuthorizationTokenFilter.getUserId())
                .setMonthDateStr(monthInVO.getMonth()).setShopId(monthInVO.getShopId());
        monthDTO.setMonthDateStr(StringUtils.isNotBlank(monthDTO.getMonthDateStr()) ? monthDTO.getMonthDateStr() + "-01" : null);//月份设置每月第一天
        Response<List<MonthDTO>> dtoResponse = monthService.findMonthList(monthDTO,monthInVO.getPageNum(),monthInVO.getPageSize());
        List<MonthListOutVO> monthList = Optional.ofNullable(dtoResponse.getData()).filter(e -> CollectionUtils.isNotEmpty(dtoResponse.getData()))
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
        return new Response<>(dtoResponse,monthList);
    }
}
