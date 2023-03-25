package com.www.ledger.data.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>@Description 项目配置参数 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/15 21:49 </p>
 */
@Data
@Component
@Accessors(chain = true)
@ConfigurationProperties(prefix = "com.www.ledger")
public class LedgerProperties {
    /** 角色拥有的路由数据的redis的key **/
    private String routerRedisKey;
    /** redis生成店铺ID的redis的key **/
    private String shopidRedisKey;
    /** 角色拥有的路由数据的redis的key的过期时间（小时） **/
    private long routerExpireHour;
    /** 文件导入上传的相对路径 **/
    private String importPath;
    /** 文件行数限制 **/
    private int rowLimit = 1000;
    /** 拼多多订单文件数据要读取的列表头名称集合,Map<列标题名称,数据库字段名> **/
    private Map<String,String> pddHeadMap;
    /** 淘宝订单文件数据要读取的列表头名称集合 **/
    private Map<String,String> taobaoHeadMap;
}
