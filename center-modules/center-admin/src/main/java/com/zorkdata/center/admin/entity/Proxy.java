package com.zorkdata.center.admin.entity;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "proxy")
public class Proxy {
    @Id
    private Integer id;

    private String cluserID;

    private Integer computerID;

    private String proxyPort;

    private String hostName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCluserID() {
        return cluserID;
    }

    public void setCluserID(String cluserID) {
        this.cluserID = cluserID;
    }

    public Integer getComputerID() {
        return computerID;
    }

    public void setComputerID(Integer computerID) {
        this.computerID = computerID;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
