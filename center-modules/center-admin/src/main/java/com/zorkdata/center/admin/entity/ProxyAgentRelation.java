package com.zorkdata.center.admin.entity;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "proxyAgentRelation")
public class ProxyAgentRelation {
    @Id
    private Integer id;

    private String agentId;

    private String proxyId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getProxyId() {
        return proxyId;
    }

    public void setProxyId(String proxyId) {
        this.proxyId = proxyId;
    }
}
