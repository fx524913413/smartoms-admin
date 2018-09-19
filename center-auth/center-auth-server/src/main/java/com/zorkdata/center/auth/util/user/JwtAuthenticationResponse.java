package com.zorkdata.center.auth.util.user;

import java.io.Serializable;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/20 17:29
 */
public class JwtAuthenticationResponse implements Serializable {

    private final String token;

    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
