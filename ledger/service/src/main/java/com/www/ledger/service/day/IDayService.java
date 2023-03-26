package com.www.ledger.service.day;

import com.www.common.data.response.Result;
import com.www.ledger.data.dto.DayDTO;

import java.util.List;

/**
 * <p>@Description 日销售额业务处理接口 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/26 18:06 </p>
 */
public interface IDayService {
    /**
     * <p>@Description 统计日销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/26 18:42 </p>
     * @param userId 用户ID
     * @return
     */
    Result<String> saveAndCountDayData(String userId);
    /**
     * <p>@Description 查询我的日销售额列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/26 18:36 </p>
     * @param dayDTO 查询条件
     * @param pageNum 分页数量
     * @param pageSize 页码
     * @return 日销售额列表数据
     */
    Result<List<DayDTO>> findDayList(DayDTO dayDTO,int pageNum, long pageSize);
}
