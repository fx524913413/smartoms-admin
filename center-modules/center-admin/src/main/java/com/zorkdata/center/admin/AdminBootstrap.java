package com.zorkdata.center.admin;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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
@MapperScan("com.zorkdata.center.admin.mapper")
@ComponentScan(basePackages = {"com.zorkdata.center.admin.agent.core", "com.zorkdata.center.admin"})
@EnableFeignClients
@EnableAutoConfiguration
@EnableSwagger2
public class AdminBootstrap {
    public static void main(String[] args) {

        new SpringApplicationBuilder(AdminBootstrap.class).web(true).run(args);
    }
}
