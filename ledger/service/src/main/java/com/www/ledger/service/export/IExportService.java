package com.www.ledger.service.export;

import com.www.common.data.response.Result;
import com.www.ledger.data.dto.DocDTO;

import java.util.List;

/**
 * <p>@Description 文件导出service </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/4/2 17:17 </p>
 */
public interface IExportService {
    /**
     * <p>@Description 创建文件生成记录数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 19:54 </p>
     * @param userId 用户ID
     * @param sheetList 要导出的数据选项
     * @return 创建结果信息
     */
    Result<String> createDocumentData(String userId, List<Integer> sheetList);
    /**
     * <p>@Description 查询报表文档信息列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/3 21:02 </p>
     * @param userId 用户ID
     * @param pageNum 分页数量
     * @param pageSize 页面
     * @return 报表文档信息列表
     */
    Result<List<DocDTO>> findReportDoc(String userId, int pageNum, long pageSize);
}
