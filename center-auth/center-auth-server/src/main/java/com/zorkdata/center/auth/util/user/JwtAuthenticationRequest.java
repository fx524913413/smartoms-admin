package com.zorkdata.center.auth.util.user;

import java.io.Serializable;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/20 17:28
 */
public class JwtAuthenticationRequest implements Serializable {

    private String username;
    private String password;
    private String productCode;


    public JwtAuthenticationRequest(String username, String password, String productCdoe) {
        this.username = username;
        this.password = password;
        this.productCode = productCdoe;
    }

    public JwtAuthenticationRequest() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
}
