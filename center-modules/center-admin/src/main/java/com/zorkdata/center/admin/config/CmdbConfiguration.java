package com.zorkdata.center.admin.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * cmdb配置文件
 * @author zhuzhigang
 * @date 2018/6/14
 */
@Configuration
@Data
public class CmdbConfiguration {
    @Value("${bkcmdb.apiserver}")
    public String apiServer;
    @Value("${cmdb.apiserver}")
    public String zorkcmdbApiServer;
}
