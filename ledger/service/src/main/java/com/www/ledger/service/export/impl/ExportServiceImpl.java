package com.www.ledger.service.export.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.common.config.code.CodeDict;
import com.www.common.config.exception.BusinessException;
import com.www.common.config.filter.core.TraceIdFilter;
import com.www.common.config.security.meta.JwtAuthorizationTokenFilter;
import com.www.common.data.constant.CharConstant;
import com.www.common.data.enums.DateFormatEnum;
import com.www.common.data.response.Result;
import com.www.common.utils.DateUtils;
import com.www.ledger.data.dao.IDocInfoDAO;
import com.www.ledger.data.dto.DocDTO;
import com.www.ledger.data.entity.DocInfoEntity;
import com.www.ledger.data.enums.CodeTypeEnum;
import com.www.ledger.data.enums.ExportEnum;
import com.www.ledger.service.async.AsyncCreateReportService;
import com.www.ledger.service.export.IExportService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>@Description 文件导出service实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/4/2 17:18 </p>
 */
@Slf4j
@Service
public class ExportServiceImpl implements IExportService {
    @Autowired
    private IDocInfoDAO docInfoDAO;
    @Autowired
    private AsyncCreateReportService asyncCreateReportService;


    /**
     * <p>@Description 创建文件生成记录数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 19:54 </p>
     * @param userId 用户ID
     * @param sheetList 要导出的数据选项
     * @return 创建结果信息
     */
    @Override
    public Result<String> createDocumentData(String userId, List<Integer>sheetList){
        //查询用户正在生成的报表文件
        DocInfoEntity docEntity = docInfoDAO.findCreateingDoc(userId);
        //没有在生成中的文件，则需要生成报表文件
        if(docEntity == null){
            docEntity = new DocInfoEntity();
            docEntity.setUserId(userId).setOverdueTime(DateUtils.stepHour(DateUtils.getCurrentDateTime(),24))
                    .setDocName(userId + CharConstant.MINUS_SIGN + DateUtils.format(DateUtils.getCurrentDateTime(), DateFormatEnum.YYYYMMDDHHMMSSSSS))
                    .setDocType(CodeDict.getValue(CodeTypeEnum.DocType_Report.getType(), CodeTypeEnum.DocType_Report.getKey()))
                    .setDocState(CodeDict.getValue(CodeTypeEnum.DocState_Create.getType(), CodeTypeEnum.DocState_Create.getKey()));
            StringBuilder remarkSB = new StringBuilder();
            ExportEnum.getAllEnum().forEach(enums -> {
                if(sheetList.contains(enums.getNum())){
                    remarkSB.append(enums.getName() + CharConstant.SEMICOLON);
                }
            });
            docEntity.setRemark(remarkSB.toString());
            docInfoDAO.save(docEntity);
            //异步创建报表文件
            asyncCreateReportService.createReport(JwtAuthorizationTokenFilter.getUserId(), MDC.get(TraceIdFilter.TRACE_ID),sheetList,docEntity.getDocId());
            return new Result<>("报表生成中，请等待。。。");
        }else {
            throw new BusinessException("已有报表在生成中，请等待。。。");
        }
    }

    /**
     * <p>@Description 查询报表文档信息列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/3 21:02 </p>
     * @param userId 用户ID
     * @param pageNum  分页数量
     * @param pageSize 页面
     * @return 报表文档信息列表
     */
    @Override
    public Result<List<DocDTO>> findReportDoc(String userId,int pageNum, long pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <= 0 ? 10 : pageSize;
        Page<DocDTO> page = new Page<>(pageNum,pageSize);
        page = docInfoDAO.findDocReport(page,userId);
        return new Result<>(pageNum,pageSize,page.getTotal(),page.getRecords());
    }
}
