package com.zorkdata.center.admin.vo;

import java.util.List;

public class ClusterInfo {

    private Long clusterID;

    private String clusterName;

    private List<Master4AgentDeploymentVo> masterlistOfCluster;

    private Integer numberOfDead;

    private Integer numberOfAlive;

    public Long getClusterID() {
        return clusterID;
    }

    public void setClusterID(Long clusterID) {
        this.clusterID = clusterID;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public List<Master4AgentDeploymentVo> getMasterlistOfCluster() {
        return masterlistOfCluster;
    }

    public void setMasterlistOfCluster(List<Master4AgentDeploymentVo> masterlistOfCluster) {
        this.masterlistOfCluster = masterlistOfCluster;
    }

    public Integer getNumberOfDead() {
        return numberOfDead;
    }

    public void setNumberOfDead(Integer numberOfDead) {
        this.numberOfDead = numberOfDead;
    }

    public Integer getNumberOfAlive() {
        return numberOfAlive;
    }

    public void setNumberOfAlive(Integer numberOfAlive) {
        this.numberOfAlive = numberOfAlive;
    }
}
