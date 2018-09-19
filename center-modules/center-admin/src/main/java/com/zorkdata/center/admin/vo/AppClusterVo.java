package com.zorkdata.center.admin.vo;

public class AppClusterVo {
    private int appSystemId;
    private String appSystemName;
    private int clusterId;
    private String clusterName;

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

    public int getClusterId() {
        return clusterId;
    }

    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }
}
