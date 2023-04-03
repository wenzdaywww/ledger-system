package com.www.ledger.data.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.www.common.config.code.CodeDict;
import com.www.ledger.data.dao.IDocInfoDAO;
import com.www.ledger.data.dto.DocDTO;
import com.www.ledger.data.entity.DocInfoEntity;
import com.www.ledger.data.enums.CodeTypeEnum;
import com.www.ledger.data.mapper.DocInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * <p>@Description 文档信息DAO实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
@Repository
public class DocInfoDAOImpl extends ServiceImpl<DocInfoMapper, DocInfoEntity> implements IDocInfoDAO {
    @Autowired
    private DocInfoMapper docInfoMapper;


    /**
     * <p>@Description 查询用户正在生成的报表文件 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/3 21:10 </p>
     * @param userId 用户ID
     * @return 报表文件信息
     */
    @Override
    public DocInfoEntity findCreateingDoc(String userId) {
        QueryWrapper<DocInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(DocInfoEntity::getUserId,userId)
                .eq(DocInfoEntity::getDocType, CodeDict.getValue(CodeTypeEnum.DocType_Report.getType(), CodeTypeEnum.DocType_Report.getKey()))
                .eq(DocInfoEntity::getDocState, CodeDict.getValue(CodeTypeEnum.DocState_Create.getType(), CodeTypeEnum.DocState_Create.getKey()));
        return docInfoMapper.selectOne(wrapper);
    }
    /**
     * <p>@Description 查询用户的报表文档 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/3 20:38 </p>
     * @param page   分页信息
     * @param userId 用户ID
     * @return 文档信息
     */
    @Override
    public Page<DocDTO> findDocReport(Page<DocDTO> page, String userId) {
        return docInfoMapper.findDocReport(page,userId);
    }
}
