package com.zorkdata.center.admin.rest;

import com.zorkdata.center.admin.rpc.service.Ifc.FindAllComputersInClusterIfc;
import com.zorkdata.center.admin.vo.ComputerAndComputerSaltType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("clusterandcomputer")
public class ComputersInClusterController {
    @Autowired
    FindAllComputersInClusterIfc findAllComputersInClusterIfc;

    @RequestMapping(value = "/findcomputerincluster", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
    @ResponseBody
    public List<ComputerAndComputerSaltType> getComputerAndComputerType(Long clusterId) {
        List<ComputerAndComputerSaltType> computerAndComputerSaltTypeList = new ArrayList<ComputerAndComputerSaltType>();
        computerAndComputerSaltTypeList = findAllComputersInClusterIfc.getComputerAndComputerTypeList(clusterId);
        return computerAndComputerSaltTypeList;
    }
}
