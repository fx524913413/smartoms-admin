package com.zorkdata.center.auth.bean;

import com.zorkdata.center.auth.common.util.jwt.IJWTInfo;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/20 15:17
 */
public class ClientInfo implements IJWTInfo {
    String clientId;
    String name;

    public ClientInfo(String clientId, String name, String id) {
        this.clientId = clientId;
        this.name = name;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUniqueName() {
        return clientId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
