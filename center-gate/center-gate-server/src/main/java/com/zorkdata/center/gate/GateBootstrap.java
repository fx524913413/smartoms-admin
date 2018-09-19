package com.zorkdata.center.gate;

import com.zorkdata.center.auth.client.EnableZorkdataAuthClient;
import com.zorkdata.center.cache.EnableCenterCache;
import com.zorkdata.center.gate.config.UserPrincipal;
import com.zorkdata.center.gate.filter.AdminAccessFilter;
import com.zorkdata.center.gate.filter.ClearFilter;
import com.zorkdata.center.gate.ratelimit.EnableZorkdataGateRateLimit;
import com.zorkdata.center.gate.ratelimit.config.IUserPrincipal;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/16 9:48
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients({"com.zorkdata.center.auth.client.feign", "com.zorkdata.center.gate.feign"})
@EnableZuulProxy
@EnableScheduling
@EnableZorkdataAuthClient
@EnableZorkdataGateRateLimit
@ComponentScan(basePackages = {"com.zorkdata.center.auth.client", "com.zorkdata.center.gate.ratelimit.config"})
@EnableCenterCache
public class GateBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(GateBootstrap.class, args);
    }

    @Bean
    @Primary
    IUserPrincipal userPrincipal() {
        return new UserPrincipal();
    }

    @Bean
    public AdminAccessFilter preAdminAccessFilter() {
        return new AdminAccessFilter();
    }

    @Bean
    public ClearFilter postClearFilter() {
        return new ClearFilter();
    }
}
