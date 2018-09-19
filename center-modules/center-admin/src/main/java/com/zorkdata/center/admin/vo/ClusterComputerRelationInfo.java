package com.zorkdata.center.admin.vo;


public class ClusterComputerRelationInfo {
    private Long id;

    private Long clusterID;

    private Long computerID;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClusterID() {
        return clusterID;
    }

    public void setClusterID(Long clusterID) {
        this.clusterID = clusterID;
    }

    public Long getComputerID() {
        return computerID;
    }

    public void setComputerID(Long computerID) {
        this.computerID = computerID;
    }
}
