package com.www.ledger.controller.year;

import com.www.common.config.security.meta.JwtAuthorizationTokenFilter;
import com.www.common.data.response.Response;
import com.www.ledger.data.dto.YearDTO;
import com.www.ledger.data.vo.year.YearListRequest;
import com.www.ledger.data.vo.year.YearListResponse;
import com.www.ledger.service.year.IYearService;
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
 * <p>@Description 年销售额controller </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/11 19:20 </p>
 */
@RestController
@RequestMapping("year")
public class YearController {
    @Autowired
    private IYearService yearService;

    /**
     * <p>@Description 统计年销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 20:09 </p>
     * @return
     */
    @PostMapping("tal")
    public Response<String> saveYearData(){
        return yearService.saveAndCountYearData(JwtAuthorizationTokenFilter.getUserId());
    }
    /**
     * <p>@Description 查询我的年销售额列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 20:07 </p>
     * @param yearRequest 查询条件
     * @return
     */
    @GetMapping("list")
    public Response<List<YearListResponse>> findYearList(@Validated YearListRequest yearRequest){
        YearDTO yearDTO = new YearDTO();
        yearDTO.setShopId(yearRequest.getShopId()).setUserId(JwtAuthorizationTokenFilter.getUserId())
                .setYearStr(StringUtils.isNotBlank(yearRequest.getYear()) ? yearRequest.getYear() + "0101" : null);
        Response<List<YearDTO>> dtoResponse = yearService.findYearList(yearDTO,yearRequest.getPageNum(),yearRequest.getPageSize());
        List<YearListResponse> yearList = Optional.ofNullable(dtoResponse.getData()).filter(e -> CollectionUtils.isNotEmpty(dtoResponse.getData()))
                .map(list -> {
                    List<YearListResponse> tempList = new ArrayList<>();
                    list.forEach(dto -> {
                        YearListResponse year = new YearListResponse();
                        year.setYear(dto.getYearStr()).setShopNm(dto.getShopName())
                                .setRetPro(dto.getRetainedProfits()).setRetProRat(dto.getRetainedProfitsRate())
                                .setGroPro(dto.getGrossProfit()).setGroProRat(dto.getGrossProfitRate())
                                .setTalOrd(dto.getTotalOrder()).setSucOrd(dto.getSucceedOrder())
                                .setFaiOrd(dto.getFailedOrder()).setSalAmt(dto.getSaleAmount())
                                .setCosAmt(dto.getCostAmount()).setAdvAmt(dto.getAdvertAmount())
                                .setSerAmt(dto.getServiceAmount()).setVirAmt(dto.getVirtualAmount())
                                .setTalCos(dto.getTotalCost());
                        tempList.add(year);
                    });
                    return tempList;
                }).orElse(null);
        return new Response<>(dtoResponse,yearList);
    }
}
