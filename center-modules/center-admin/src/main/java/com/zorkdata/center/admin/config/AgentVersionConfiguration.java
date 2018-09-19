package com.zorkdata.center.admin.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @description: ${todo}
 * @author: huziyue
 * @create: 2018/6/8 10:15
 */
@Configuration
@Data
public class AgentVersionConfiguration {
    @Value("${agent.version}")
    private String version;
}
