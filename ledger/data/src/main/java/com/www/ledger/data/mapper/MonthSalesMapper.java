package com.www.ledger.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.www.ledger.data.entity.MonthSalesEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>@Description 月销售额信息Mapper </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 19:43 </p>
 */
@Mapper
public interface MonthSalesMapper extends BaseMapper<MonthSalesEntity> {
}
