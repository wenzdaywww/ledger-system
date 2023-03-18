package com.www.ledger.service.user;

import com.www.common.config.security.entity.SysRoleEntity;
import com.www.common.data.response.Response;
import com.www.ledger.data.dto.UserDTO;

/**
 * <p>@Description 用户信息校验service接口 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/14 15:31 </p>
 */
public interface IUserInfoCheckService {
    /**
     * <p>@Description 创建用户信息前校验 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/16 23:36 </p>
     * @param user 用户信息
     * @param response 返回信息
     * @return  不为空校验通过，为空校验不通过
     */
    SysRoleEntity checkBeforeCreateUser(UserDTO user, Response<String> response);
}
