package com.www.ledger;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * <p>@Description 启动类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/9 21:37 </p>
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.www.common.config.code","com.www.common.config.security","com.www.ledger.data"})
public class LedgerApplication {
    /**
     * <p>@Description 启动方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/9 21:37 </p>
     * @param args
     * @return void
     */
    public static void main(String[] args) {
        SpringApplication.run(LedgerApplication.class, args);
    }
}
