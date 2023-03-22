package com.www.ledger.data.vo.user;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>@Description 查询当前登录的用户信息返回对象 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/17 20:02 </p>
 */
@Data
@Accessors(chain = true)
public class UserInfoOutVO {
    private static final long serialVersionUID = 1L;
    /** 用户主键 */
    private Long suId;
    /** 用户ID */
    private String userId;
    /** 用户昵称 */
    private String userName;
}
