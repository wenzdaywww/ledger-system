package com.www.ledger.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.www.common.config.security.entity.SysRoleEntity;
import com.www.common.config.security.entity.SysUserEntity;
import com.www.common.config.security.mapper.SysRoleMapper;
import com.www.common.data.enums.ResponseEnum;
import com.www.common.data.response.Response;
import com.www.ledger.data.dto.UserDTO;
import com.www.ledger.service.user.IUserInfoCheckService;
import com.www.ledger.service.user.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>@Description 用户信息校验service实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/14 15:32 </p>
 */
@Slf4j
@Service
public class UserInfoCheckServiceImpl implements IUserInfoCheckService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private IUserInfoService userInfoService;

    /**
     * <p>@Description 创建用户信息前校验 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/16 23:36 </p>
     * @param user 用户信息
     * @param response 返回信息
     * @return  不为空校验通过，为空校验不通过
     */
    @Override
    public SysRoleEntity checkBeforeCreateUser(UserDTO user, Response<String> response) {
        SysRoleEntity roleEntity = null;
        SysUserEntity existEntity = userInfoService.findUserById(user.getUserId());
        if(existEntity != null){
            response.setResponse(ResponseEnum.FAIL,"用户ID已存在，请修改");
            return roleEntity;
        }
        QueryWrapper<SysRoleEntity> roleWrapper = new QueryWrapper<>();
        roleWrapper.lambda().eq(SysRoleEntity::getRoleCode,"ROLE_USER");
        roleEntity = sysRoleMapper.selectOne(roleWrapper);
        if(roleEntity == null){
            response.setResponse(ResponseEnum.FAIL,"用户角色错误，创建用户失败");
            return roleEntity;
        }
        return roleEntity;
    }
}
