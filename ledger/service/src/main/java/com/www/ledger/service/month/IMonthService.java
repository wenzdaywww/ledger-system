package com.www.ledger.service.month;

import com.www.common.data.response.Result;
import com.www.ledger.data.dto.MonthDTO;

import java.util.List;

/**
 * <p>@Description 月销售额service </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:06 </p>
 */
public interface IMonthService {
    /**
     * <p>@Description 统计月销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 14:01 </p>
     * @param userId 用户ID
     * @return
     */
    Result<String> saveAndCountMonthData(String userId);
    /**
     * <p>@Description 查询月销售额列表信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 14:09 </p>
     * @param monthDTO 查询条件
     * @param pageNum 分页数量
     * @param pageSize 页码
     * @return
     */
    Result<List<MonthDTO>> findMonthList(MonthDTO monthDTO, int pageNum, long pageSize);
}
