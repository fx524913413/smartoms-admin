package com.zorkdata.center.auth.configuration;

import com.zorkdata.center.auth.interceptor.UserAuthRestInterceptor;
import com.zorkdata.center.common.handler.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/21 8:24
 */
@Configuration("admimWebConfig")
@Primary
public class WebConfiguration implements WebMvcConfigurer {
    @Bean
    GlobalExceptionHandler getGlobalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(getServiceAuthRestInterceptor()).addPathPatterns("/service/**");
        registry.addInterceptor(getUserAuthRestInterceptor()).addPathPatterns("/service/**");
    }

//    @Bean
//    ServiceAuthRestInterceptor getServiceAuthRestInterceptor() {
//        return new ServiceAuthRestInterceptor();
//    }

    @Bean
    UserAuthRestInterceptor getUserAuthRestInterceptor() {
        return new UserAuthRestInterceptor();
    }

}
