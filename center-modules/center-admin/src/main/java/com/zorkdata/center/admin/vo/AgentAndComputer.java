package com.zorkdata.center.admin.vo;

import com.zorkdata.center.admin.entity.Agent;
import com.zorkdata.center.admin.entity.Computer;

public class AgentAndComputer {
    private Long appInstanceID;
    private Computer computer;
    private Agent agent;

    public Long getAppInstanceID() {
        return appInstanceID;
    }

    public void setAppInstanceID(Long appInstanceID) {
        this.appInstanceID = appInstanceID;
    }

    public Computer getComputer() {
        return computer;
    }


    public void setComputer(Computer computer) {
        this.computer = computer;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}
