package com.zorkdata.center.auth;

import com.zorkdata.center.cache.EnableCenterCache;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/20 15:13
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@MapperScan("com.zorkdata.center.auth.mapper")
@RemoteApplicationEventScan(basePackages = "com.zorkdata.center.auth.common.event")
@EnableAutoConfiguration
@EnableCenterCache
public class AuthBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(AuthBootstrap.class, args);
    }
}
