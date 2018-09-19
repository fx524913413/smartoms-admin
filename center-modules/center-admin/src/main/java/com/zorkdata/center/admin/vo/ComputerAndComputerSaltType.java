package com.zorkdata.center.admin.vo;

import com.zorkdata.center.admin.entity.Computer;

public class ComputerAndComputerSaltType {
    private Computer computer;
    private String computerSaltType;
    private Long masterID;
    private Long agentID;

    public Computer getComputer() {
        return computer;
    }

    public void setComputer(Computer computer) {
        this.computer = computer;
    }

    public String getComputerSaltType() {
        return computerSaltType;
    }

    public void setComputerSaltType(String computerSaltType) {
        this.computerSaltType = computerSaltType;
    }

    public Long getMasterID() {
        return masterID;
    }

    public void setMasterID(Long masterID) {
        this.masterID = masterID;
    }

    public Long getAgentID() {
        return agentID;
    }

    public void setAgentID(Long agentID) {
        this.agentID = agentID;
    }
}
