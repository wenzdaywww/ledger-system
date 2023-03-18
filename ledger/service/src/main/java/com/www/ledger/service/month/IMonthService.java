package com.www.ledger.service.month;

import com.www.common.data.response.Response;
import com.www.ledger.data.dto.MonthDTO;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * <p>@Description 月销售额service </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:06 </p>
 */
public interface IMonthService {
    /**
     * <p>@Description 编辑月销售额数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 18:26 </p>
     * @param monthDTO 月销售额数据
     * @return
     */
    Response<String> saveMonthData(MonthDTO monthDTO);
    /**
     * <p>@Description 删除月销售额数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 18:21 </p>
     * @param msId 月销售额ID
     * @return
     */
    Response<String> deleteMonthData(Long msId);
    /**
     * <p>@Description 统计月销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 14:01 </p>
     * @param userId 用户ID
     * @return
     */
    Response<String> updateCountMonthSale(String userId);
    /**
     * <p>@Description 统计月销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 14:01 </p>
     * @param userId 用户ID
     * @return
     */
    Response<String> saveMonthList(String userId);
    /**
     * <p>@Description 查询月销售额列表信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 14:09 </p>
     * @param monthDTO 查询条件
     * @param pageNum 分页数量
     * @param pageSize 页面
     * @return
     */
    Response<List<MonthDTO>> findMonthList(MonthDTO monthDTO, int pageNum, long pageSize);
}
