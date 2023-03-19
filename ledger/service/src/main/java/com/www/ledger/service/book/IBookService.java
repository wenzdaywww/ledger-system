package com.www.ledger.service.book;

import com.www.common.data.response.Response;
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
     * <p>@Description 查询近10天销售额前3名店铺销售额趋势图 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 18:00 </p>
     * @param userId 用户ID
     * @return
     */
    Response<List<List<OrderDTO>>> findLast10DaySales(String userId);
    /**
     * <p>@Description 查询用户近一年的销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 16:46 </p>
     * @param userId 用户ID
     * @return
     */
    Response<List<MonthDTO>> findLastYearSales(String userId);
    /**
     * <p>@Description 统计用户账簿信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 00:29 </p>
     * @param userId 用户ID
     * @return
     */
    Response<String> saveAndCountBookData(String userId);
    /**
     * <p>@Description 查询用户账簿信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 00:29 </p>
     * @param userId 用户ID
     * @return
     */
    Response<BookDTO> findBookData(String userId);
}
