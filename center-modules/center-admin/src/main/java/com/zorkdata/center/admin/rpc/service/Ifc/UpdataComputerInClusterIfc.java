package com.zorkdata.center.admin.rpc.service.Ifc;

import com.zorkdata.center.admin.entity.Cluster;
import com.zorkdata.center.admin.entity.Computer;
import com.zorkdata.center.admin.vo.ComputerAndComputerSaltType;

import java.util.List;

public interface UpdataComputerInClusterIfc {
    void deleteComputerInCluster(Long clusterID, List<ComputerAndComputerSaltType> computerList);

    void addComputerInCluster(Long clusterID, List<ComputerAndComputerSaltType> computerList);
}
