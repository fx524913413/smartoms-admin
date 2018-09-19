package com.zorkdata.center.auth.common.util.jwt;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/16 9:57
 */
public class JWTInfo implements Serializable, IJWTInfo {
    @JSONField(name = "uniqueName")
    private String username;
    @JSONField(name = "id")
    private String userId;
    @JSONField(name = "name")
    private String name;

    public JWTInfo() {
    }

    public JWTInfo(String username, String userId, String name) {
        this.username = username;
        this.userId = userId;
        this.name = name;
    }

    @Override
    public String getUniqueName() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JWTInfo jwtInfo = (JWTInfo) o;

        if (username != null ? !username.equals(jwtInfo.username) : jwtInfo.username != null) {
            return false;
        }
        return userId != null ? userId.equals(jwtInfo.userId) : jwtInfo.userId == null;

    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
