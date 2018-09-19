package com.zorkdata.center.gate.ratelimit.config;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/16 9:12
 */
public interface IUserPrincipal {
    String getName(HttpServletRequest request);
}
