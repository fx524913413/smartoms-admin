package com.zorkdata.center.admin.rpc.service.Impl;

import com.zorkdata.center.admin.biz.ClusterComputerRelationBiz;
import com.zorkdata.center.admin.entity.ClusterComputerRelation;
import com.zorkdata.center.admin.rpc.service.Ifc.ClusterComputerRelationServiceIfc;
import com.zorkdata.center.admin.vo.ClusterComputerRelationInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClusterComputerRelationServiceImpl implements ClusterComputerRelationServiceIfc {
    @Autowired
    private ClusterComputerRelationBiz clusterComputerRelationBiz;

    @Override
    public ClusterComputerRelationInfo findComputerInCluster(Long clusterid) {
        ClusterComputerRelationInfo info = new ClusterComputerRelationInfo();
        ClusterComputerRelation clusterComputerRelation = clusterComputerRelationBiz.getClusterByClusterId(clusterid);
        BeanUtils.copyProperties(clusterComputerRelation, info);
        return info;
    }

    @Override
    public void insertClusterComputerRelationEntity(ClusterComputerRelation clusterComputerRelation) {
        clusterComputerRelationBiz.insertClusterComputerRelation(clusterComputerRelation);
    }

    @Override
    public void updateClusterComputerRelationEntity(ClusterComputerRelation clusterComputerRelation) {
        clusterComputerRelationBiz.updateClusterComputerRelation(clusterComputerRelation);
    }

    @Override
    public void deleteClusterComputerRelationEntity(Long clusterid) {
        clusterComputerRelationBiz.deleteClusterComputerRelation(clusterid);
    }
}
