package com.zorkdata.center.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@EnableEurekaServer
@SpringBootApplication
/**
 * 启动一个服务注册中心提供给其他应用进行对话
 * @author zhuzhigang
 * @create 2018-03-14 12:44
 */
public class EurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }
}