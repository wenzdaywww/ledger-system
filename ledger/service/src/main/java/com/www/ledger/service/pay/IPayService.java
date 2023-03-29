package com.www.ledger.service.pay;

import com.www.common.data.response.Result;
import com.www.ledger.data.dto.PayDTO;

import java.util.List;

/**
 * <p>@Description 支出管理Service </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/28 22:56 </p>
 */
public interface IPayService {
    /**
     * <p>@Description 保存支出信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/29 19:27 </p>
     * @param payDTO 支出信息
     * @return 保存结果信息
     */
    Result<String> savePayInfo(PayDTO payDTO);
    /**
     * <p>@Description 删除支出信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/29 19:24 </p>
     * @param userId 用户ID
     * @param payId 支出ID
     * @return 删除结果信息
     */
    Result<String> deletePayInfo(String userId,Long payId);
    /**
     * <p>@Description 查询支出信息列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/29 19:23 </p>
     * @param payDTO 查询条件
     * @param pageNum 分页数量
     * @param pageSize 页面
     * @return 支出信息列表
     */
    Result<List<PayDTO>> findPayList(PayDTO payDTO, int pageNum, long pageSize);
}
