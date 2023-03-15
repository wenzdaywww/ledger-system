package com.www.ledger.data.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
    @Value("${com.www.ledger.router-redis-key}")
    private String routerRedisKey;
    /** 角色拥有的路由数据的redis的key的过期时间（小时） **/
    @Value("${com.www.ledger.router-expire-hour}")
    private long routerExpireHour;
}
