package com.www.ledger.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>@Description 文档信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
@TableName("DOC_INFO")
public class DocInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 文档主键ID **/
    @TableId(value = "DOC_ID", type = IdType.AUTO)
    private Long docId;
    /** 用户名 **/
    @TableField("USER_ID")
    private String userId;
    /** 文档名称 **/
    @TableField("DOC_NAME")
    private String docName;
    /** 文档扩展名 **/
    @TableField("EXTENSION")
    private String extension;
    /** 文档类型(code=DocType) **/
    @TableField("DOC_TYPE")
    private String docType;
    /** 文档状态(code=DocState) **/
    @TableField("DOC_STATE")
    private String docState;
    /** 文档URL路径 **/
    @TableField("DOC_URL")
    private String docUrl;
    /** 文档过期时间 **/
    @TableField("OVERDUE_TIME")
    private Date overdueTime;
    /** 备注 **/
    @TableField("REMARK")
    private String remark;
    /** 更新时间 **/
    @TableField("UPDATE_TIME")
    private Date updateTime;
    /** 创建时间 **/
    @TableField("CREATE_TIME")
    private Date createTime;
}
