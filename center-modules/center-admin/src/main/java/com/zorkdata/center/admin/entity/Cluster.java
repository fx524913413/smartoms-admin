package com.zorkdata.center.admin.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "Cluster")
public class Cluster {
    @Id
    @Column(name = "ClusterID")
    private Long clusterID;

    @Column(name = "ClusterName")
    private String clusterName;

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
}
