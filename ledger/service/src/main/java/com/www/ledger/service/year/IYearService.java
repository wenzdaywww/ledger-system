package com.www.ledger.service.year;

import com.www.common.data.response.Result;
import com.www.ledger.data.dto.YearDTO;

import java.util.List;

/**
 * <p>@Description 年销售额service </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:06 </p>
 */
public interface IYearService {
    /**
     * <p>@Description 统计年销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 20:10 </p>
     * @param userId 用户ID
     * @return
     */
    Result<String> saveAndCountYearData(String userId);
    /**
     * <p>@Description 查询我的年销售额列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 20:12 </p>
     * @param yearDTO 查询条件
     * @param pageNum 分页数量
     * @param pageSize 页码
     * @return
     */
    Result<List<YearDTO>> findYearList(YearDTO yearDTO, int pageNum, long pageSize);
}
