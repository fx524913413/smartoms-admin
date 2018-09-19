package com.zorkdata.center.admin.vo;

import java.util.List;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/6/5 14:47
 */
public class AppSystemInfoVo {
    private int appSystemId;
    private String appSystemName;
    private String sysCode;
    private List<WorkerNode> workerNodes;

    public int getAppSystemId() {
        return appSystemId;
    }

    public void setAppSystemId(int appSystemId) {
        this.appSystemId = appSystemId;
    }

    public String getAppSystemName() {
        return appSystemName;
    }

    public void setAppSystemName(String appSystemName) {
        this.appSystemName = appSystemName;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public List<WorkerNode> getWorkerNodes() {
        return workerNodes;
    }

    public void setWorkerNodes(List<WorkerNode> workerNodes) {
        this.workerNodes = workerNodes;
    }

}
