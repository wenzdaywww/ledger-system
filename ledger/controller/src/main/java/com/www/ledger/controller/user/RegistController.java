package com.www.ledger.controller.user;

import com.www.common.data.response.Result;
import com.www.ledger.data.dto.UserDTO;
import com.www.ledger.data.vo.user.NewUserInVO;
import com.www.ledger.service.user.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>@Description 用户注册contoller </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/1/19 22:14 </p>
 */
@Slf4j
@RestController
@RequestMapping("new")
public class RegistController {
    @Autowired
    private IUserInfoService userInfoService;
    /**
     * <p>@Description 创建用户信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/7 21:20 </p>
     * @param userInVO 新增用户信息
     * @return Response<java.lang.String>
     */
    @PostMapping("user")
    public Result<String> createUser(@Validated NewUserInVO userInVO){
        UserDTO user = new UserDTO();
        user.setUserId(userInVO.getUserId())
                .setUserName(userInVO.getUserName())
                .setPassword(userInVO.getPassword());
        return userInfoService.createUser(user);
    }
}
