package com.zorkdata.center.admin.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "Agent")
public class Agent {
    @Id
    @Column(name = "AgentID")
    private Long agentID;

    @Column(name = "AgentName")
    private String agentName;

    @Column(name = "State")
    private Integer state;

    @Column(name = "BackState")
    private Integer backState;

    @Column(name = "LastTime")
    private Date lastTime;

    @Column(name = "LoginTime")
    private Date loginTime;

    @Column(name = "ComputerID")
    private Long computerID;

    @Column(name = "Version")
    private String version;

    @Column(name = "MasterID")
    private Long masterID;

    @Column(name = "MasterBackID")
    private Long masterBackID;

    @Column(name = "InstallationType")
    private String installationType;

    @Column(name = "LastModifyTime")
    private Date lastModifyTime;

    public Long getAgentID() {
        return agentID;
    }

    public void setAgentID(Long agentID) {
        this.agentID = agentID;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Long getComputerID() {
        return computerID;
    }

    public void setComputerID(Long computerID) {
        this.computerID = computerID;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getMasterID() {
        return masterID;
    }

    public void setMasterID(Long masterID) {
        this.masterID = masterID;
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

    public Long getMasterBackID() {
        return masterBackID;
    }

    public void setMasterBackID(Long masterBackID) {
        this.masterBackID = masterBackID;
    }

    public Integer getBackState() {
        return backState;
    }

    public void setBackState(Integer backState) {
        this.backState = backState;
    }
}
