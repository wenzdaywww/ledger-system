package com.www.ledger.controller.user;

import com.www.common.config.code.write.CodeRedisWriteHandler;
import com.www.common.config.security.meta.JwtAuthorizationTokenFilter;
import com.www.common.data.response.Response;
import com.www.ledger.data.dto.UserDTO;
import com.www.ledger.data.vo.user.UserEditRequest;
import com.www.ledger.data.vo.user.UserInfoRepsonse;
import com.www.ledger.data.vo.user.UserPwdRequest;
import com.www.ledger.service.user.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * <p>@Description 用户信息controller </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/11 19:20 </p>
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private CodeRedisWriteHandler codeRedisWriteHandler;
    /**
     * <p>@Description 查询当前登录的用户信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/8 19:43 </p>
     * @return com.www.myblog.common.pojo.Response<com.www.myblog.base.data.dto.SysUserDTO>
     */
    @GetMapping("info")
    public Response<UserInfoRepsonse> findUser(){
        Response<UserDTO> userDTO = userInfoService.findUser(JwtAuthorizationTokenFilter.getUserId());
        UserInfoRepsonse userRepsonse = Optional.ofNullable(userDTO.getData()).map(e -> {
            UserInfoRepsonse tempRepsonse = new UserInfoRepsonse();
            tempRepsonse.setSuId(e.getSuId())
                        .setUserId(e.getUserId())
                        .setUserName(e.getUserName());
            return tempRepsonse;
        }).orElse(null);
        return new Response<>(userDTO,userRepsonse);
    }
    /**
     * <p>@Description 更新当前登录的用户密码 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/8 19:58 </p>
     * @param pwdRequest 用户密码信息
     * @return com.www.myblog.common.pojo.Response<java.lang.String>
     */
    @PostMapping("pwd")
    public Response<String> updateUserPwd(@Validated UserPwdRequest pwdRequest){
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(JwtAuthorizationTokenFilter.getUserId())
            .setPassword(pwdRequest.getPassword())
            .setNewPassWord(pwdRequest.getNewPassWord());
        return userInfoService.updateUserPwd(userDTO);
    }
    /**
     * <p>@Description 更新当前登录的用户信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/8 19:58 </p>
     * @param editRequest 用户信息
     * @return com.www.myblog.common.pojo.Response<java.lang.String>
     */
    @PostMapping("edit")
    public Response<String> updateUserInfo(@Validated UserEditRequest editRequest){
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(JwtAuthorizationTokenFilter.getUserId())
                .setUserName(editRequest.getUserName());
        return userInfoService.updateUserInfo(userDTO);
    }
    /**
     * <p>@Description 获取当前角色拥有的路由 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/15 19:08 </p>
     * @return Response<java.util.List < java.lang.String>>
     */
    @GetMapping("router")
    public Response<List<String>> findRouter(){
        return userInfoService.findRouter(JwtAuthorizationTokenFilter.getUserId());
    }
}
