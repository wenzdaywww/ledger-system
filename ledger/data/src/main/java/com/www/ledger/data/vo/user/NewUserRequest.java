package com.www.ledger.data.vo.user;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * <p>@Description 创建用户信息的请求对象 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/17 21:08 </p>
 */
@Data
@Accessors(chain = true)
public class NewUserRequest {
    private static final long serialVersionUID = 1L;
    /** 用户ID */
    @NotBlank(message = "用户ID不能为空")
    @Length(min = 1, max = 20,message = "用户ID字数为6~20")
    private String userId;
    /** 密码 */
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 20,message = "密码字数为6~20")
    private String password;
    /** 用户昵称 */
    @NotBlank(message = "用户名称不能为空")
    @Length(min = 1, max = 40,message = "用户名称字数为1~40")
    private String userName;
}
