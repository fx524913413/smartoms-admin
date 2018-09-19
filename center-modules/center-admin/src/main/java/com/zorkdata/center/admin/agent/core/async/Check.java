package com.zorkdata.center.admin.agent.core.async;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/5/30 12:42
 */
@Deprecated
public class Check {

    /**
     * 判读str是否为null或""
     *
     * @param param 需要判断的字符串
     * @return 如果str为null或""返回true否则返回false
     */
    public static boolean isNullOrEmpty(String param) {
        if (param == null || "".equals(param.trim()))
            return true;
        return false;
    }

    public static boolean isNull(String param) {
        if (param == null)
            return true;
        return false;
    }

    public static boolean isEmpty(String param) {
        if ("".equals(param.trim()))
            return true;
        return false;
    }
}
