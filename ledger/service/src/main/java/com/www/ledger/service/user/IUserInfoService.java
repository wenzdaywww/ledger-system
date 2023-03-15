package com.www.ledger.service.user;

import com.www.common.config.security.entity.SysUserEntity;
import com.www.common.data.dto.response.ResponseDTO;
import com.www.ledger.data.dto.SysUserDTO;

import java.util.List;

/**
 * <p>@Description 用户信息service接口 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/14 15:31 </p>
 */
public interface IUserInfoService {
    /**
     * <p>@Description 查询用户信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/8 19:43 </p>
     * @param userId 用户ID
     * @return com.www.myblog.common.pojo.ResponseDTO<com.www.myblog.base.data.dto.SysUserDTO>
     */
    ResponseDTO<SysUserDTO> findUser(String userId);
    /**
     * <p>@Description 创建用户信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/7 21:03 </p>
     * @param user
     * @return com.www.myblog.common.pojo.ResponseDTO<java.lang.String>
     */
    ResponseDTO<String> createUser(SysUserDTO user);
    /**
     * <p>@Description 更新用户密码 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/8 19:58 </p>
     * @param user 用户信息
     * @return com.www.myblog.common.pojo.ResponseDTO<java.lang.String>
     */
    ResponseDTO<String> updateUserPwd(SysUserDTO user);
    /**
     * <p>@Description 更新用户信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/8 19:58 </p>
     * @param user 用户信息
     * @return com.www.myblog.common.pojo.ResponseDTO<java.lang.String>
     */
    ResponseDTO<String> updateUserInfo(SysUserDTO user);
    /**
     * <p>@Description 根据用户id查询用户信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/12 22:27 </p>
     * @param userId 用户id
     * @return com.www.common.config.security.entity.SysUserEntity
     */
    SysUserEntity findUserById(String userId);
    /**
     * <p>@Description 获取当前角色拥有的路由 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/15 19:08 </p>
     * @return com.www.common.data.dto.response.ResponseDTO<java.util.List < java.lang.String>>
     */
    ResponseDTO<List<String>> findRouter(String userId);
}
