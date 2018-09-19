package com.zorkdata.center.admin.rpc.service.Ifc;

import com.zorkdata.center.admin.entity.Computer;
import com.zorkdata.center.admin.vo.ComputerAndComputerSaltType;

import java.util.List;

public interface FindAllComputersInClusterIfc {
    List<ComputerAndComputerSaltType> getComputerAndComputerTypeList(Long clusterID);

    ComputerAndComputerSaltType getComputerAndComputerType(Computer computer);

    List<ComputerAndComputerSaltType> getComputerAndComputerTypeListByUserID(Long userId);
}
