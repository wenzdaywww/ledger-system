package com.www.ledger.data.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>@Description 文档信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class DocDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 文档主键ID **/
    private Long docId;
    /** 用户名 **/
    private String userId;
    /** 文档名称 **/
    private String docName;
    /** 文档扩展名 **/
    private String extension;
    /** 文档类型(code=DocType) **/
    private String docType;
    /** 文档状态(code=DocState) **/
    private String docState;
    /** 文档状态(code=DocState) **/
    private String docStateStr;
    /** 文档URL路径 **/
    private String docUrl;
    /** 文档过期时间 **/
    private Date overdueTime;
    /** 备注 **/
    private String remark;
    /** 更新时间 **/
    private Date updateTime;
    /** 创建时间 **/
    private Date createTime;
    /** 创建时间 **/
    private String createTimeStr;
}
