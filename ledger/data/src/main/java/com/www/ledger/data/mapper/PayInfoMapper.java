package com.www.ledger.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.ledger.data.dto.PayDTO;
import com.www.ledger.data.entity.PayInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>@Description 支出信息Mapper </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 19:43 </p>
 */
@Mapper
public interface PayInfoMapper extends BaseMapper<PayInfoEntity> {
    /**
     * <p>@Description 查询支出信息列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/29 19:58 </p>
     * @param page 分页信息
     * @param dto 查询条件
     * @return 支出信息列表
     */
    Page<PayDTO> findPayList(Page<PayDTO> page, @Param("obj") PayDTO dto);
}
