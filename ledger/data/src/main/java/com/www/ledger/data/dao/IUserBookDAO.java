package com.www.ledger.data.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.www.ledger.data.entity.UserBookEntity;

/**
 * <p>@Description 用户账簿信息DAO接口 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
public interface IUserBookDAO extends IService<UserBookEntity> {
    /**
     * <p>@Description 查询用户的账簿信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:13 </p>
     * @param userId 用户ID
     * @param isThrow 查不到数据是否抛出业务异常，true抛出，false不抛出
     * @return 用户的账簿信息
     */
    UserBookEntity findUserBook(String userId,boolean isThrow);
}
