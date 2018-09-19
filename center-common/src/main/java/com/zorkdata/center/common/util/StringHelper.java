package com.zorkdata.center.common.util;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/15 11:11
 */
public class StringHelper {
    public static String getObjectValue(Object obj) {
        return obj == null ? "" : obj.toString();
    }
}
