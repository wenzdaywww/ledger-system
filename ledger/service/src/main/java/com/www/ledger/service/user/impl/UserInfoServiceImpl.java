package com.www.ledger.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.www.common.config.security.entity.SysRoleEntity;
import com.www.common.config.security.entity.SysUserEntity;
import com.www.common.config.security.entity.SysUserRoleEntity;
import com.www.common.config.security.mapper.SysRoleMapper;
import com.www.common.config.security.mapper.SysUserMapper;
import com.www.common.config.security.mapper.SysUserRoleMapper;
import com.www.common.data.dto.response.ResponseDTO;
import com.www.common.data.enums.ResponseEnum;
import com.www.common.utils.DateUtils;
import com.www.ledger.data.dto.SysUserDTO;
import com.www.ledger.service.user.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * <p>@Description 用户信息service实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/14 15:32 </p>
 */
@Service
@Slf4j
public class UserInfoServiceImpl implements IUserInfoService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    /**
     * <p>@Description 查询用户信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/8 19:43 </p>
     * @param userId 用户ID
     * @return com.www.myblog.common.pojo.ResponseDTO<com.www.myblog.base.data.dto.SysUserDTO>
     */
    @Override
    public ResponseDTO<SysUserDTO> findUser(String userId) {
        ResponseDTO<SysUserDTO> responseDTO = new ResponseDTO<>();
        if(StringUtils.isBlank(userId)){
            responseDTO.setCode(ResponseEnum.FAIL.getCode());
            responseDTO.setMsg("账号不能为空！");
            return responseDTO;
        }
        QueryWrapper<SysUserEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysUserEntity::getUserId,userId);
        SysUserEntity userEntity = sysUserMapper.selectOne(wrapper);
        SysUserDTO userDTO = Optional.ofNullable(userEntity).map(entity -> {
            SysUserDTO tempDTO = new SysUserDTO();
            tempDTO.setSuId(entity.getSuId());
            tempDTO.setUserId(entity.getUserId());
            tempDTO.setUserName(entity.getUserName());
            return tempDTO;
        }).orElse(null);
        if(userDTO == null){
            responseDTO.setCode(ResponseEnum.FAIL.getCode());
            responseDTO.setMsg("查询不到该用户信息");
            return responseDTO;
        }
        responseDTO.setResponse(ResponseEnum.SUCCESS,userDTO);
        return responseDTO;
    }
    /**
     * <p>@Description 创建用户信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/7 21:03 </p>
     * @param user
     * @return com.www.myblog.common.pojo.ResponseDTO<java.lang.String>
     */
    @Override
    public ResponseDTO<String> createUser(SysUserDTO user) throws Exception{
        ResponseDTO<String> responseDTO = new ResponseDTO<>();
        if(user == null || StringUtils.isAnyBlank(user.getUserId(),user.getUserName(),user.getPassword())){
            responseDTO.setResponse(ResponseEnum.FAIL,"信息不完整，创建用户失败");
            return responseDTO;
        }
        QueryWrapper<SysUserEntity> userWrapper = new QueryWrapper<>();
        userWrapper.lambda().eq(SysUserEntity::getUserId,user.getUserId());
        SysUserEntity existEntity = sysUserMapper.selectOne(userWrapper);
        if(existEntity != null){
            responseDTO.setResponse(ResponseEnum.FAIL,"用户ID已存在，请修改");
            return responseDTO;
        }
        QueryWrapper<SysRoleEntity> roleWrapper = new QueryWrapper<>();
        roleWrapper.lambda().eq(SysRoleEntity::getRoleCode,"ROLE_USER");
        SysRoleEntity roleEntity = sysRoleMapper.selectOne(roleWrapper);
        if(roleEntity == null){
            responseDTO.setCode(ResponseEnum.FAIL.getCode()).setMsg("用户角色错误，创建用户失败");
            return responseDTO;
        }
        //创建用户
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        SysUserEntity userEntity = new SysUserEntity();
        userEntity.setUserId(user.getUserId());
        userEntity.setUserName(user.getUserName());
        userEntity.setPassword(user.getPassword());
        userEntity.setCreateTime(DateUtils.getCurrentDateTime());
        userEntity.setUpdateTime(DateUtils.getCurrentDateTime());
        sysUserMapper.insert(userEntity);
        //创建用户角色
        SysUserRoleEntity userRoleEntity = new SysUserRoleEntity();
        userRoleEntity.setUserId(userEntity.getUserId());
        userRoleEntity.setRoleId(roleEntity.getRoleId());
        userRoleEntity.setCreateTime(DateUtils.getCurrentDateTime());
        userRoleEntity.setUpdateTime(DateUtils.getCurrentDateTime());
        sysUserRoleMapper.insert(userRoleEntity);
        responseDTO.setResponse(ResponseEnum.SUCCESS,"创建用户成功");
        return responseDTO;
    }
}
