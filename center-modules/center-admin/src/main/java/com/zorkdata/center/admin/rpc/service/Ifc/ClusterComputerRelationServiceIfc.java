package com.zorkdata.center.admin.rpc.service.Ifc;

import com.zorkdata.center.admin.entity.ClusterComputerRelation;
import com.zorkdata.center.admin.vo.ClusterComputerRelationInfo;

public interface ClusterComputerRelationServiceIfc {
    ClusterComputerRelationInfo findComputerInCluster(Long clusterid);

    void insertClusterComputerRelationEntity(ClusterComputerRelation clusterComputerRelation);

    void updateClusterComputerRelationEntity(ClusterComputerRelation clusterComputerRelation);

    void deleteClusterComputerRelationEntity(Long clusterid);
}
