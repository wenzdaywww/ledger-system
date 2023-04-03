package com.www.ledger.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.ledger.data.dto.DocDTO;
import com.www.ledger.data.entity.DocInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>@Description 文档信息Mapper </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 19:43 </p>
 */
@Mapper
public interface DocInfoMapper extends BaseMapper<DocInfoEntity> {
    /**
     * <p>@Description 查询用户的报表文档 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/3 20:38 </p>
     * @param page 分页信息
     * @param userId 用户ID
     * @return 文档信息
     */
    Page<DocDTO> findDocReport(Page<DocDTO> page, @Param("userId") String userId);
}
