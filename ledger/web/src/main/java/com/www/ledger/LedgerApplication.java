package com.www.ledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * <p>@Description 启动类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/9 21:37 </p>
 */
@EnableAsync
@SpringBootApplication
public class LedgerApplication {
    /**
     * <p>@Description 启动方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/9 21:37 </p>
     * @param args
     * @return void
     */
    public static void main(String[] args) {
        //配置application加密的密钥
        System.setProperty("jasypt.encryptor.password","wenzday");
        //TODO 2023/4/11 23:03 无法使用common-spring-boot-starter包中的多数据源配置
        SpringApplication.run(LedgerApplication.class, args);
    }
}
