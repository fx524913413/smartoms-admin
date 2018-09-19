package com.zorkdata.center.auth.client;

import com.zorkdata.center.auth.client.configuration.AutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/22 11:14
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(AutoConfiguration.class)
@Documented
@Inherited
public @interface EnableZorkdataAuthClient {
}
