package com.zorkdata.center.adminv2.Configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/6/5 16:17
 */
@Configuration
@Data
public class BeatsConfiguration {
    @Value("${bizpc.url}")
    private String bizpcurl;
}
