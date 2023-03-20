package com.www.ledger.service.entity;

import com.baomidou.mybatisplus.extension.service.IService;
import com.www.ledger.data.entity.UserBookEntity;

/**
 * <p>@Description 用户账簿信息Service接口 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
public interface IUserBookService extends IService<UserBookEntity> {
    /**
     * <p>@Description 查询用户的账簿信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:13 </p>
     * @param userId 用户ID
     * @param ubId 用户账簿主键ID,可为空
     * @return 用户的账簿信息
     */
    UserBookEntity findUserBook(String userId,Long ubId);
    /**
     * <p>@Description 查询用户的账簿信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:13 </p>
     * @param userId 用户ID
     * @return 用户的账簿信息
     */
    UserBookEntity findUserBook(String userId);
}
