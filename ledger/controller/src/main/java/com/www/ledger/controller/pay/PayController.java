package com.www.ledger.controller.pay;

import com.www.common.config.security.meta.JwtAuthorizationTokenFilter;
import com.www.common.data.response.Result;
import com.www.ledger.data.dto.PayDTO;
import com.www.ledger.data.vo.pay.PayInfoInVO;
import com.www.ledger.data.vo.pay.PayListInVO;
import com.www.ledger.data.vo.pay.PayListOutVO;
import com.www.ledger.service.pay.IPayService;
import org.apache.commons.collections4.CollectionUtils;
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
 * <p>@Description 支出管理controller </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/11 19:20 </p>
 */
@Validated
@RestController
@RequestMapping("pay")
public class PayController {
    @Autowired
    private IPayService payService;

    /**
     * <p>@Description 保存支出信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/29 19:27 </p>
     * @param inVO 支出信息
     * @return 保存结果信息
     */
    @PostMapping("info")
    public Result<String> savePayInfo(@Validated PayInfoInVO inVO){
        PayDTO payDTO = new PayDTO();
        payDTO.setUserId(JwtAuthorizationTokenFilter.getUserId()).setPayId(inVO.getPayId())
                .setPayDateStr(inVO.getPayDat()).setShopId(inVO.getShopId()).setPayType(inVO.getPayTp())
                .setPayAmount(inVO.getPayAmt()).setRemark(inVO.getRemark());
        return payService.savePayInfo(payDTO);
    }
    /**
     * <p>@Description 删除支出信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/29 19:24 </p>
     * @param payId 支出ID
     * @return 删除结果信息
     */
    @PostMapping("dlt/{payId}")
    public Result<String> deletePayInfo(@PathVariable("payId") Long payId){
        return payService.deletePayInfo(JwtAuthorizationTokenFilter.getUserId(),payId);
    }
    /**
     * <p>@Description 查询支出信息列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/29 19:23 </p>
     * @param inVO 查询条件
     * @return 支出信息列表
     */
    @GetMapping("list")
    public Result<List<PayListOutVO>> findPayList(@Validated PayListInVO inVO){
        PayDTO payDTO = new PayDTO();
        payDTO.setStartDate(inVO.getStrDat()).setEndDate(inVO.getEndDat()).setUserId(JwtAuthorizationTokenFilter.getUserId())
                .setShopId(inVO.getShopId()).setPayType(inVO.getPayTp());
        Result<List<PayDTO>> dtoResult = payService.findPayList(payDTO,inVO.getPageNum(),inVO.getPageSize());
        List<PayListOutVO> payList = Optional.ofNullable(dtoResult.getData()).filter(e -> CollectionUtils.isNotEmpty(dtoResult.getData()))
                .map(list -> {
                    List<PayListOutVO> temList = new ArrayList<>();
                    list.forEach(dto -> {
                        PayListOutVO outVO = new PayListOutVO();
                        outVO.setPayId(dto.getPayId()).setPayDat(dto.getPayDateStr()).setShopId(dto.getShopId())
                                .setShopNm(dto.getShopName()).setPayTp(dto.getPayType()).setPayNm(dto.getPayTypeName())
                                .setPayAmt(dto.getPayAmount()).setRemark(dto.getRemark());
                        temList.add(outVO);
                    });
                    return temList;
                }).orElse(null);
        return new Result<>(dtoResult,payList);
    }

}
