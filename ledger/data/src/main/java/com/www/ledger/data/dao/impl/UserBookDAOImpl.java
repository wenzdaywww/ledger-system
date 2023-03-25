package com.www.ledger.data.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.www.ledger.data.dao.IUserBookDAO;
import com.www.ledger.data.entity.UserBookEntity;
import com.www.ledger.data.mapper.UserBookMapper;
import org.apache.commons.lang3.StringUtils;
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
     * @return 用户的账簿信息
     */
    @Override
    public UserBookEntity findUserBook(String userId) {
        return this.findUserBook(userId,null);
    }
    /**
     * <p>@Description 查询用户的账簿信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:13 </p>
     * @param userId 用户ID
     * @param ubId   用户账簿主键ID
     * @return 用户的账簿信息
     */
    @Override
    public UserBookEntity findUserBook(String userId, Long ubId) {
        if(StringUtils.isBlank(userId)){
            return null;
        }
        QueryWrapper<UserBookEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserBookEntity::getUserId,userId);
        if(ubId != null){
            wrapper.lambda().eq(UserBookEntity::getUbId,ubId);
        }
        return userBookMapper.selectOne(wrapper);
    }
}
