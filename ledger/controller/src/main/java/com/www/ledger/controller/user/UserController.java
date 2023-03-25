package com.www.ledger.controller.user;

import com.www.common.config.code.write.CodeRedisWriteHandler;
import com.www.common.config.security.meta.JwtAuthorizationTokenFilter;
import com.www.common.data.response.Result;
import com.www.ledger.data.dto.UserDTO;
import com.www.ledger.data.vo.user.UserEditInVO;
import com.www.ledger.data.vo.user.UserInfoOutVO;
import com.www.ledger.data.vo.user.UserPwdInVO;
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
    public Result<UserInfoOutVO> findUser(){
        Result<UserDTO> result = userInfoService.findUser(JwtAuthorizationTokenFilter.getUserId());
        UserInfoOutVO userInfoOutVO = Optional.ofNullable(result.getData()).map(e -> {
            UserInfoOutVO tempOutVO = new UserInfoOutVO();
            tempOutVO.setSuId(e.getSuId())
                        .setUserId(e.getUserId())
                        .setUserName(e.getUserName());
            return tempOutVO;
        }).orElse(null);
        return new Result<>(result,userInfoOutVO);
    }
    /**
     * <p>@Description 更新当前登录的用户密码 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/8 19:58 </p>
     * @param pwdInVO 用户密码信息
     * @return com.www.myblog.common.pojo.Response<java.lang.String>
     */
    @PostMapping("pwd")
    public Result<String> updateUserPwd(@Validated UserPwdInVO pwdInVO){
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(JwtAuthorizationTokenFilter.getUserId())
            .setPassword(pwdInVO.getPassword())
            .setNewPassWord(pwdInVO.getNewPassWord());
        return userInfoService.updateUserPwd(userDTO);
    }
    /**
     * <p>@Description 更新当前登录的用户信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/8 19:58 </p>
     * @param editInVO 用户信息
     * @return com.www.myblog.common.pojo.Response<java.lang.String>
     */
    @PostMapping("edit")
    public Result<String> updateUserInfo(@Validated UserEditInVO editInVO){
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(JwtAuthorizationTokenFilter.getUserId())
                .setUserName(editInVO.getUserName());
        return userInfoService.updateUserInfo(userDTO);
    }
    /**
     * <p>@Description 获取当前角色拥有的路由 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/15 19:08 </p>
     * @return Response<java.util.List < java.lang.String>>
     */
    @GetMapping("router")
    public Result<List<String>> findRouter(){
        return userInfoService.findRouter(JwtAuthorizationTokenFilter.getUserId());
    }
}
