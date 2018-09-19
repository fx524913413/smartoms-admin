package com.zorkdata.center.admin.entity;

import java.util.List;

public class Agent2Master {

    private String saltApi;

    private List<AgentName> agentList;

    private String userName;

    private String password;

    public String getSaltApi() {
        return saltApi;
    }

    public void setSaltApi(String saltApi) {
        this.saltApi = saltApi;
    }

    public List<AgentName> getAgentList() {
        return agentList;
    }

    public void setAgentList(List<AgentName> agentList) {
        this.agentList = agentList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
