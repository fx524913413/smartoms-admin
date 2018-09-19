package com.zorkdata.center.cache.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/19 9:09
 */
@Configuration("cacheWebConfig")
public class CacheWebConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/META-INF/static/cache/**").addResourceLocations(
                "classpath:/META-INF/static/");
        super.addResourceHandlers(registry);
    }
}