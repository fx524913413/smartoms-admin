package com.zorkdata.center.cache.test;

import com.zorkdata.center.cache.EnableCenterCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/19 10:02
 */
@SpringBootApplication
@EnableCenterCache
public class CacheTest {
    public static void main(String args[]) {
        SpringApplication app = new SpringApplication(CacheTest.class);
        app.run(args);
    }
}
