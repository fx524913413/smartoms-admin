package com.zorkdata.center.auth.common.util;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/16 9:59
 */
public class StringHelper {
    public static String getObjectValue(Object obj) {
        return obj == null ? "" : obj.toString();
    }
}
