package com.zorkdata.center.cache;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/19 9:02
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(AutoConfiguration.class)
@Documented
@Inherited
public @interface EnableCenterCache {
}
