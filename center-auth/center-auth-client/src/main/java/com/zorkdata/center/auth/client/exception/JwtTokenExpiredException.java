package com.zorkdata.center.auth.client.exception;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/22 11:22
 */
public class JwtTokenExpiredException extends Exception {
    public JwtTokenExpiredException(String s) {
        super(s);
    }
}