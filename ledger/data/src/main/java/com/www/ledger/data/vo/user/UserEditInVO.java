package com.www.ledger.data.vo.user;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * <p>@Description 更新当前登录的用户信息的请求对象 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/17 21:08 </p>
 */
@Data
@Accessors(chain = true)
public class UserEditInVO {
    private static final long serialVersionUID = 1L;
    /** 用户ID */
    private String userId;
    /** 用户名称 */
    @NotBlank(message = "用户名称不能为空")
    @Length(min = 1, max = 40,message = "用户名称字数为1~40")
    private String userName;
}
