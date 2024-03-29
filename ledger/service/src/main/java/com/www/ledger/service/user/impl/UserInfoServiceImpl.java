package com.www.ledger.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.www.common.config.exception.BusinessException;
import com.www.common.config.redis.RedisOperation;
import com.www.common.config.security.MySecurityProperties;
import com.www.common.config.security.dto.AuthorityDTO;
import com.www.common.config.security.entity.SysRoleEntity;
import com.www.common.config.security.entity.SysUserEntity;
import com.www.common.config.security.entity.SysUserRoleEntity;
import com.www.common.config.security.mapper.AuthorityRoleMapper;
import com.www.common.config.security.mapper.SysRoleMapper;
import com.www.common.config.security.mapper.SysUserMapper;
import com.www.common.config.security.mapper.SysUserRoleMapper;
import com.www.common.data.constant.CharConstant;
import com.www.common.data.response.Result;
import com.www.common.utils.DateUtils;
import com.www.ledger.data.dao.IUserBookDAO;
import com.www.ledger.data.dto.UserDTO;
import com.www.ledger.data.entity.UserBookEntity;
import com.www.ledger.data.properties.LedgerProperties;
import com.www.ledger.service.user.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


/**
 * <p>@Description 用户信息service实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/14 15:32 </p>
 */
@Slf4j
@Service
public class UserInfoServiceImpl implements IUserInfoService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private IUserBookDAO userBookDAO;
    @Autowired
    private LedgerProperties ledgerProperties;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private AuthorityRoleMapper authorityRoleMapper;
    @Autowired
    private MySecurityProperties mySecurityProperties;
    @Autowired
    private SysRoleMapper sysRoleMapper;

    /**
     * <p>@Description 查询用户信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/8 19:43 </p>
     * @param userId 用户ID
     * @return com.www.myblog.common.pojo.Response<com.www.myblog.base.data.dto.SysUserDTO>
     */
    @Override
    public Result<UserDTO> findUser(String userId) {
        if(StringUtils.isBlank(userId)){
            return new Result<>();
        }
        SysUserEntity userEntity = this.findUserById(userId);
        UserDTO userDTO = Optional.ofNullable(userEntity).map(entity -> {
            UserDTO tempDTO = new UserDTO();
            tempDTO.setSuId(entity.getSuId());
            tempDTO.setUserId(entity.getUserId());
            tempDTO.setUserName(entity.getUserName());
            return tempDTO;
        }).orElse(null);
        if(userDTO == null){
            return new Result<>();
        }
        return new Result<>(userDTO);
    }
    /**
     * <p>@Description 创建用户信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/7 21:03 </p>
     * @param user
     * @return com.www.myblog.common.pojo.Response<java.lang.String>
     */
    @Override
    public Result<String> createUser(UserDTO user) {
        //创建用户信息前校验
        SysUserEntity existEntity = this.findUserById(user.getUserId());
        if(existEntity != null){
            throw new BusinessException("用户ID已存在，请修改");
        }
        //创建用户信息前校验
        QueryWrapper<SysRoleEntity> roleWrapper = new QueryWrapper<>();
        roleWrapper.lambda().eq(SysRoleEntity::getRoleCode,"ROLE_USER");
        SysRoleEntity roleEntity = sysRoleMapper.selectOne(roleWrapper);
        if(roleEntity == null){
            throw new BusinessException("创建用户失败");
        }
        //创建用户信息
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        SysUserEntity userEntity = new SysUserEntity();
        userEntity.setUserId(user.getUserId());
        userEntity.setUserName(user.getUserName());
        userEntity.setPassword(user.getPassword());
        userEntity.setCreateTime(DateUtils.getCurrentDateTime());
        userEntity.setUpdateTime(DateUtils.getCurrentDateTime());
        sysUserMapper.insert(userEntity);
        //创建用户角色信息
        SysUserRoleEntity userRoleEntity = new SysUserRoleEntity();
        userRoleEntity.setUserId(userEntity.getUserId());
        userRoleEntity.setRoleId(roleEntity.getRoleId());
        userRoleEntity.setCreateTime(DateUtils.getCurrentDateTime());
        userRoleEntity.setUpdateTime(DateUtils.getCurrentDateTime());
        sysUserRoleMapper.insert(userRoleEntity);
        //创建用户账簿信息
        UserBookEntity bookEntity = new UserBookEntity();
        bookEntity.setUserId(userEntity.getUserId());
        userBookDAO.save(bookEntity);
        return new Result<>("创建用户成功");
    }
    /**
     * <p>@Description 更新用户密码 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/8 19:58 </p>
     * @param user 用户信息
     * @return com.www.myblog.common.pojo.Response<java.lang.String>
     */
    @Override
    public Result<String> updateUserPwd(UserDTO user) {
        SysUserEntity userEntity = this.findUserById(user.getUserId());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //有输入密码则校验密码
        if(!encoder.matches(user.getPassword(),userEntity.getPassword())){
            throw new BusinessException("原密码不正确");
        }
        //更新用户信息
        UpdateWrapper<SysUserEntity> userWrapper = new UpdateWrapper<>();
        userWrapper.lambda().eq(SysUserEntity::getUserId,user.getUserId());
        userWrapper.lambda().set(SysUserEntity::getPassword,encoder.encode(user.getNewPassWord()));
        userWrapper.lambda().set(SysUserEntity::getUpdateTime,DateUtils.getCurrentDateTime());
        int count = sysUserMapper.update(null,userWrapper);
        if(count == 0){
            return new Result<>("更新密码失败");
        }
        return new Result<>("更新密码成功");
    }
    /**
     * <p>@Description 更新用户信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/8 19:58 </p>
     * @param user 用户信息
     * @return com.www.myblog.common.pojo.Response<java.lang.String>
     */
    @Override
    public Result<String> updateUserInfo(UserDTO user) {
        //更新用户信息前校验
        SysUserEntity userEntity = this.findUserById(user.getUserId());
        //更新用户信息
        UpdateWrapper<SysUserEntity> userWrapper = new UpdateWrapper<>();
        userWrapper.lambda().eq(SysUserEntity::getUserId,user.getUserId());
        userWrapper.lambda().set(SysUserEntity::getUserName,user.getUserName());
        userWrapper.lambda().set(SysUserEntity::getUpdateTime,DateUtils.getCurrentDateTime());
        int count = sysUserMapper.update(null,userWrapper);
        if(count == 0){
            return new Result<>("更新用户信息失败");
        }
        return new Result<>("更新信息成功");
    }
    /**
     * <p>@Description 获取当前角色拥有的路由 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/15 19:08 </p>
     * @param userId
     * @return Response<java.util.List < java.lang.String>>
     */
    @Override
    public Result<List<String>> findRouter(String userId) {
        List<String> routerList = null;
        //redis中有角色路由信息则直接获取
        if(RedisOperation.hasKey(ledgerProperties.getRouterRedisKey())){
            routerList = (List<String>) RedisOperation.listGet(ledgerProperties.getRouterRedisKey());
        }else {
            //有配置使用redis保存用户的角色信息的key前缀，则从中获取用户的角色
            if(StringUtils.isNotBlank(mySecurityProperties.getUserPrefix())){
                String userRoleKey = mySecurityProperties.getUserPrefix() + CharConstant.COLON + userId;
                //查询角色拥有的路由权限
                List<String> roleList = (List<String>) RedisOperation.listGet(userRoleKey);
                routerList = this.findRoleRouter(roleList);
            }else {
                routerList = this.findUserRouter(userId);
            }
            if(CollectionUtils.isNotEmpty(routerList)){
                routerList.forEach(e -> {
                    RedisOperation.listSet(ledgerProperties.getRouterRedisKey(),e);
                });
                RedisOperation.keyExpire(ledgerProperties.getRouterRedisKey(),ledgerProperties.getRouterExpireHour(), TimeUnit.HOURS);
            }
        }
        return new Result<>(routerList);
    }
    /**
     * <p>@Description 查询角色拥有的路由权限 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/15 23:04 </p>
     * @param roleList 角色信息结合
     * @return java.util.List<java.lang.String>
     */
    private List<String> findRoleRouter(List<String> roleList){
        List<AuthorityDTO> authorityList = authorityRoleMapper.findRoleAuthority(roleList);
        List<String> routerList = Optional.ofNullable(authorityList).filter(e ->CollectionUtils.isNotEmpty(authorityList))
                .map(list -> {
                    List<String> tempList = new ArrayList<>();
                    list.forEach(dto -> {
                        tempList.add(dto.getUrl());
                    });
                    return tempList;
                }).orElse(null);
        return routerList;
    }
    /**
     * <p>@Description 查询用户角色拥有的路由权限 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/15 22:52 </p>
     * @param userId 用户id
     * @return java.util.List<java.lang.String>
     */
    private List<String> findUserRouter(String userId){
        List<AuthorityDTO> authorityList = authorityRoleMapper.findUserAuthorityRole(userId);
        List<String> routerList = Optional.ofNullable(authorityList).filter(e ->CollectionUtils.isNotEmpty(authorityList))
                .map(list -> {
                    List<String> tempList = new ArrayList<>();
                    list.forEach(dto -> {
                        tempList.add(dto.getUrl());
                    });
                    return tempList;
                }).orElse(null);
        return routerList;
    }
    /**
     * <p>@Description 根据用户id查询用户信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/12 22:27 </p>
     * @param userId 用户id
     * @return 用户信息
     */
    @Override
    public SysUserEntity findUserById(String userId){
        QueryWrapper<SysUserEntity> userWrapper = new QueryWrapper<>();
        userWrapper.lambda().eq(SysUserEntity::getUserId,userId);
        return sysUserMapper.selectOne(userWrapper);
    }
}
