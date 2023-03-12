package com.www.ledger.controller.user;

import com.www.common.config.security.meta.JwtAuthorizationTokenFilter;
import com.www.common.data.dto.response.ResponseDTO;
import com.www.ledger.data.dto.SysUserDTO;
import com.www.ledger.service.user.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    /**
     * <p>@Description 查询当前登录的用户信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/8 19:43 </p>
     * @return com.www.myblog.common.pojo.ResponseDTO<com.www.myblog.base.data.dto.SysUserDTO>
     */
    @GetMapping("info")
    public ResponseDTO<SysUserDTO> findUser(){
        return userInfoService.findUser(JwtAuthorizationTokenFilter.getUserId());
    }
}
