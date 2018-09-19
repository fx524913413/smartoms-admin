package com.zorkdata.center.admin.vo;

import java.util.Date;

public class AgentInfo {
    private Long agentID;

    private String agentName;

    private Integer state;

    private Date lastTime;

    private Date loginTime;

    private Long computerID;

    private String version;

    private Long masterID;

    private String installationType;

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
}
