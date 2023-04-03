package com.www.ledger.data.vo.book;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>@Description 查询报表文档信息列表的响应数据 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/19 15:05 </p>
 */
@Data
@Accessors(chain = true)
public class BookDocOutVO {
    /** 文档名称 **/
    private String name;
    /** 生成时间 **/
    private String date;
    /** 导出的数据 **/
    private String sheet;
    /** 报告状态 **/
    private String state;
    /** 文档下载路径 **/
    private String url;
}
