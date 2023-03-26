package com.www.ledger.service.book;

import com.www.common.data.response.Result;
import com.www.ledger.data.dto.BookDTO;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.dto.OrderDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>@Description 我的账本service </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:06 </p>
 */
public interface IBookService {
    /**
     * <p>@Description 查询近些天销售额排名靠前店铺销售额趋势图 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 18:00 </p>
     * @param userId 用户ID
     * @return
     */
    Result<List<List<OrderDTO>>> findLastDaySales(String userId);
    /**
     * <p>@Description 查询用户最近一年所有店铺销售额趋势图 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 16:46 </p>
     * @param userId 用户ID
     * @return
     */
    Result<List<MonthDTO>> findLastYearSales(String userId);
    /**
     * <p>@Description 统计用户账簿信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 00:29 </p>
     * @param userId 用户ID
     * @return
     */
    Result<String> saveAndCountBookData(String userId);
    /**
     * <p>@Description 查询用户账簿信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 00:29 </p>
     * @param userId 用户ID
     * @return
     */
    Result<BookDTO> findBookData(String userId);
}
