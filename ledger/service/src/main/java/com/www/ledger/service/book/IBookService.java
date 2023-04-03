package com.www.ledger.service.book;

import com.www.common.data.response.Result;
import com.www.ledger.data.dto.BookDTO;

/**
 * <p>@Description 我的账本service </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:06 </p>
 */
public interface IBookService {
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
     * @param isThrow 查不到数据是否抛出业务异常，true抛出，false不抛出
     * @return 用户账簿信息
     */
    BookDTO findBookData(String userId,boolean isThrow);
}
