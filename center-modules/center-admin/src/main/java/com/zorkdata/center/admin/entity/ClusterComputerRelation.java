package com.zorkdata.center.admin.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/24 14:45
 */
@Table(name = "ClusterComputerRelation")
public class ClusterComputerRelation {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "ClusterID")
    private Long clusterID;


    @Column(name = "ComputerID")
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
