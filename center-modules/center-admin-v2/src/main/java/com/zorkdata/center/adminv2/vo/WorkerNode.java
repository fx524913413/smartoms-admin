package com.zorkdata.center.adminv2.vo;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/6/5 15:02
 */
public class WorkerNode {
    private int workerNodeId;
    private String worknode;
    private String workerNodeName;
    private String ip;
    private String type;
    private int state;

    public int getWorkerNodeId() {
        return workerNodeId;
    }

    public void setWorkerNodeId(int workerNodeId) {
        this.workerNodeId = workerNodeId;
    }

    public String getWorknode() {
        return worknode;
    }

    public void setWorknode(String worknode) {
        this.worknode = worknode;
    }

    public String getWorkerNodeName() {
        return workerNodeName;
    }

    public void setWorkerNodeName(String workerNodeName) {
        this.workerNodeName = workerNodeName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
