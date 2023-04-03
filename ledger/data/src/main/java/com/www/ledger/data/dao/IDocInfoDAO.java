package com.www.ledger.data.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.www.ledger.data.dto.DocDTO;
import com.www.ledger.data.entity.DocInfoEntity;

/**
 * <p>@Description 文档信息Service接口 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
public interface IDocInfoDAO extends IService<DocInfoEntity> {
    /**
     * <p>@Description 查询用户正在生成的报表文件 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/3 21:10 </p>
     * @param userId 用户ID
     * @return 报表文件信息
     */
    DocInfoEntity findCreateingDoc(String userId);
    /**
     * <p>@Description 查询用户的报表文档 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/3 20:38 </p>
     * @param page 分页信息
     * @param userId 用户ID
     * @return 文档信息
     */
    Page<DocDTO> findDocReport(Page<DocDTO> page,String userId);
}
