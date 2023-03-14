package com.www.ledger.service.entity.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.www.ledger.data.entity.UserBookEntity;
import com.www.ledger.data.mapper.UserBookMapper;
import com.www.ledger.service.entity.IUserBookService;
import org.springframework.stereotype.Service;

/**
 * <p>@Description 用户账簿信息Service实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
@Service
public class UserBookServiceImpl extends ServiceImpl<UserBookMapper, UserBookEntity> implements IUserBookService {
}
