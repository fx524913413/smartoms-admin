package com.zorkdata.center.gate.ratelimit.config;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/16 9:12
 */
public class DefaultUserPrincipal implements IUserPrincipal {
    @Override
    public String getName(HttpServletRequest request) {
        if (request.getUserPrincipal() == null) {
            return null;
        }
        return request.getUserPrincipal().getName();
    }
}
