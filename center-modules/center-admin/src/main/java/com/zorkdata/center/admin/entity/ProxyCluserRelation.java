package com.zorkdata.center.admin.entity;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "proxyCluserRelation")
public class ProxyCluserRelation {
    @Id
    private Integer id;

    private String proxyId;

    private String cluserId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProxyId() {
        return proxyId;
    }

    public void setProxyId(String proxyId) {
        this.proxyId = proxyId;
    }

    public String getCluserId() {
        return cluserId;
    }

    public void setCluserId(String cluserId) {
        this.cluserId = cluserId;
    }
}
