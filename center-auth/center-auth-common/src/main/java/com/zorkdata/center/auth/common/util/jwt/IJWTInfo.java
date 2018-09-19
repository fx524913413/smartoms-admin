package com.zorkdata.center.auth.common.util.jwt;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/16 9:57
 */
public interface IJWTInfo {
    /**
     * 获取用户名
     *
     * @return
     */
    String getUniqueName();

    /**
     * 获取用户ID
     *
     * @return
     */
    String getId();

    /**
     * 获取名称
     *
     * @return
     */
    String getName();
}
