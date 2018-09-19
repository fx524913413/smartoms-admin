package com.zorkdata.center.admin.vo;

import java.util.Date;

public class MasterInfo {
    private Long masterID;

    private Long clusterID;

    private Long computerID;

    private Integer port;

    private Integer state;

    private Date lastModifyTime;

    private String installationType;

    public Long getMasterID() {
        return masterID;
    }

    public void setMasterID(Long masterID) {
        this.masterID = masterID;
    }

    public Long getClusterID() {
        return clusterID;
    }

    public void setClusterID(Long clusterID) {
        this.clusterID = clusterID;
    }

    public Long getComputerID() {
        return computerID;
    }

    public void setComputerID(Long computerID) {
        this.computerID = computerID;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getInstallationType() {
        return installationType;
    }

    public void setInstallationType(String installationType) {
        this.installationType = installationType;
    }
}
