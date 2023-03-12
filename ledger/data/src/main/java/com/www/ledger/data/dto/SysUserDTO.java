package com.www.ledger.data.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>@Description 用户信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/10 22:24 </p>
 */
@Data
public class SysUserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
    * 用户主键
    */
    private Long suId;
    /**
    * 用户ID
    */
    private String userId;
    /**
     * 新密码
     */
    private String newPassWord;
    /**
     * 密码
     */
    private String password;
    /**
    * 用户昵称
    */
    private String userName;
}