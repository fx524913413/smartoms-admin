package com.zorkdata.center.auth.client.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/6/6 15:30
 */
@Configuration
@Data
public class TokenConfig {
    @Value("${token.expire}")
    private int expire;
}
