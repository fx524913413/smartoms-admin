package com.zorkdata.center.adminv2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/15 13:05
 */
@EnableEurekaClient
@EnableCircuitBreaker
@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@MapperScan("com.zorkdata.center.adminv2.mapper")
@ComponentScan(basePackages = {"com.zorkdata.center.adminv2"})
//@EnableSwagger2Doc
public class AdminBootstrapV2 {
    public static void main(String[] args) {
        new SpringApplicationBuilder(AdminBootstrapV2.class).web(true).run(args);
    }
}
