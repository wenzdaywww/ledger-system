package com.www.ledger.data.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.www.common.config.exception.BusinessException;
import com.www.ledger.data.dao.IUserBookDAO;
import com.www.ledger.data.entity.UserBookEntity;
import com.www.ledger.data.mapper.UserBookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * <p>@Description 用户账簿信息DAO实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
@Repository
public class UserBookDAOImpl extends ServiceImpl<UserBookMapper, UserBookEntity> implements IUserBookDAO {
    @Autowired
    private UserBookMapper userBookMapper;

    /**
     * <p>@Description 查询用户的账簿信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:13 </p>
     * @param userId 用户ID
     * @param isThrow 查不到数据是否抛出业务异常，true抛出，false不抛出
     * @return 用户的账簿信息
     */
    @Override
    public UserBookEntity findUserBook(String userId,boolean isThrow) {
        QueryWrapper<UserBookEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserBookEntity::getUserId,userId);
        UserBookEntity entity = userBookMapper.selectOne(wrapper);
        if(entity == null && isThrow){
            throw new BusinessException("用户账簿信息不存在");
        }
        return entity;
    }
}
