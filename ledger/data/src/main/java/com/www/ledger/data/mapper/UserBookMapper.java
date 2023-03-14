package com.www.ledger.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.www.ledger.data.entity.UserBookEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>@Description 用户账簿信息Mapper </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 19:43 </p>
 */
@Mapper
public interface UserBookMapper extends BaseMapper<UserBookEntity> {
}
