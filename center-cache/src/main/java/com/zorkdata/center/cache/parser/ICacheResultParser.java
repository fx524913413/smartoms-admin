package com.zorkdata.center.cache.parser;

import java.lang.reflect.Type;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/19 9:21
 */
public interface ICacheResultParser {
    /**
     * 解析结果
     *
     * @param value
     * @param returnType
     * @param origins
     * @return
     */
    public Object parse(String value, Type returnType, Class<?>... origins);
}
