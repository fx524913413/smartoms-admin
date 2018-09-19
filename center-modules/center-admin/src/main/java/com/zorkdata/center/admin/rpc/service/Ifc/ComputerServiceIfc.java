package com.zorkdata.center.admin.rpc.service.Ifc;

import com.zorkdata.center.admin.entity.Cluster;
import com.zorkdata.center.admin.entity.Computer;
import com.zorkdata.center.admin.vo.ComputerInfo;
import com.zorkdata.center.admin.vo.ComputerTagVo;
import com.zorkdata.center.admin.vo.ComputersSortByProductCode;
import com.zorkdata.center.admin.vo.WorkerNode;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface ComputerServiceIfc {
    ComputerInfo findcomputerinfo(Long computerID);

    Computer getComputerByAgentID(Long agentID);

    List<WorkerNode> getComputerAgentInfo();

    boolean insertComputerEntity(Computer computer);

    boolean updateComputerEntity(Computer computer);

    void deleteComputerEntity(Long computerid);

    void uploadExcelComputerinfo(MultipartFile multipartFiles);

    List<Computer> getComputerList(Long userId);

    void deleteComputers(Set<Long> computerIDlist);

    List<Computer> findAllComputer();

    List<ComputerTagVo> findComputerAndTag(Long userID);

    List<ComputersSortByProductCode> getComputersSortByProductCode(String productCode,Long userID);
}
