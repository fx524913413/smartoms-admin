package com.zorkdata.center.admin.vo;

import com.zorkdata.center.admin.entity.Agent;
import com.zorkdata.center.admin.entity.Computer;

public class AgentVo2 {
    private MasterVo2 masterVo2;

    private Agent agent;

    private Computer computer;

    public MasterVo2 getMasterVo2() {
        return masterVo2;
    }

    public void setMasterVo2(MasterVo2 masterVo2) {
        this.masterVo2 = masterVo2;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Computer getComputer() {
        return computer;
    }

    public void setComputer(Computer computer) {
        this.computer = computer;
    }
}
