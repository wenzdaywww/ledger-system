package com.www.ledger.data.vo.user;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * <p>@Description 更新当前登录的用户密码的请求对象 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/17 21:08 </p>
 */
@Data
@Accessors(chain = true)
public class UserPwdInVO {
    private static final long serialVersionUID = 1L;
    /** 用户ID */
    @Length(min = 1, max = 20,message = "用户ID字数为6~20")
    private String userId;
    /** 密码 */
    @NotBlank(message = "原密码不能为空")
    @Length(min = 6, max = 20,message = "原密码字数为6~20")
    private String password;
    /** 新密码 */
    @NotBlank(message = "新密码不能为空")
    @Length(min = 6, max = 20,message = "新密码字数为6~20")
    private String newPassWord;
}
