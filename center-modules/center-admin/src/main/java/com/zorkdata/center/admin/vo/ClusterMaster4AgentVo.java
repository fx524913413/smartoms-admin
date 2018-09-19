package com.zorkdata.center.admin.vo;

import com.zorkdata.center.admin.entity.Agent;
import com.zorkdata.center.admin.entity.Cluster;
import com.zorkdata.center.admin.entity.Master;

public class ClusterMaster4AgentVo {
    private Agent agent;

    private Cluster cluster;

    private Master master;

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }
}
