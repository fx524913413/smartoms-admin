package com.zorkdata.center.cache.annotation;

import com.zorkdata.center.cache.parser.IKeyGenerator;
import com.zorkdata.center.cache.parser.impl.DefaultKeyGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/19 9:20
 */
@Retention(RetentionPolicy.RUNTIME)//在运行时可以获取
@Target(value = {ElementType.METHOD, ElementType.TYPE})//作用到类，方法，接口上等
public @interface CacheClear {
    /**
     * 缓存key的前缀
     *
     * @return
     */
    public String pre() default "";

    /**
     * 缓存key
     *
     * @return
     */
    public String key() default "";

    /**
     * 缓存keys
     *
     * @return
     */
    public String[] keys() default "";

    /**
     * 键值解析类
     *
     * @return
     */
    public Class<? extends IKeyGenerator> generator() default DefaultKeyGenerator.class;
}
