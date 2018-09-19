package com.zorkdata.center.admin.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "Master")
public class Master {
    @Id
    @Column(name = "MasterID")
    private Long masterID;

    @Column(name = "ClusterID")
    private Long clusterID;

    @Column(name = "ComputerID")
    private Long computerID;

    @Column(name = "Port")
    private Integer port;

    @Column(name = "State")
    private Integer state;

    @Column(name = "InstallationType")
    private String installationType;

    @Column(name = "LastModifyTime")
    private Date lastModifyTime;

    @Column(name = "IsMaster")
    private Integer isMaster;

    @Column(name = "ApiPort")
    private Integer ApiPort;

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

    public String getInstallationType() {
        return installationType;
    }

    public void setInstallationType(String installationType) {
        this.installationType = installationType;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public Integer getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(Integer isMaster) {
        this.isMaster = isMaster;
    }

    public Integer getApiPort() {
        return ApiPort;
    }

    public void setApiPort(Integer apiPort) {
        ApiPort = apiPort;
    }
}
