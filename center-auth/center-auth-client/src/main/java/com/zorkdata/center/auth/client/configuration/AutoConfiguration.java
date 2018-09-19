package com.zorkdata.center.auth.client.configuration;

import com.zorkdata.center.auth.client.config.UserAuthConfig;
import org.springframework.context.annotation.Bean;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/22 11:18
 */
public class AutoConfiguration {
    @Bean
    UserAuthConfig getUserAuthConfig() {
        return new UserAuthConfig();
    }
}
