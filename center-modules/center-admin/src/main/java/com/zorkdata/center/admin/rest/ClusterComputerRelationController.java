package com.zorkdata.center.admin.rest;

import com.zorkdata.center.admin.entity.ClusterComputerRelation;
import com.zorkdata.center.admin.mapper.ClusterComputerRelationMapper;
import com.zorkdata.center.admin.rpc.service.Ifc.ClusterComputerRelationServiceIfc;
import com.zorkdata.center.admin.vo.ClusterComputerRelationInfo;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("clusterComputerRelation")
public class ClusterComputerRelationController {
    @Autowired
    ClusterComputerRelationServiceIfc clusterComputerRelationServiceIfc;
    @Autowired
    ClusterComputerRelationMapper clusterComputerRelationMapper;

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getClusterComputerRelationinfo(Long clusterID) {
        ClusterComputerRelationInfo clusterComputerRelationInfo = clusterComputerRelationServiceIfc.findComputerInCluster(clusterID);
        if (clusterComputerRelationInfo == null) {
            return RespTools.getRespMsgModel(CodeTable.FAILED, null);
        } else {
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, clusterComputerRelationInfo);
        }
    }

    @RequestMapping(value = "/addClusterComputerRelationinfo", method = RequestMethod.POST)
    @ResponseBody
    public RespModel addClusterComputerRelationinfo(@RequestBody ClusterComputerRelation clusterComputerRelation) throws Exception {
        clusterComputerRelationServiceIfc.insertClusterComputerRelationEntity(clusterComputerRelation);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/updataClusterComputerRelationinfo", method = RequestMethod.POST)
    @ResponseBody
    public RespModel updataClusterComputerRelationinfo(@RequestBody ClusterComputerRelation clusterComputerRelation) throws Exception {
        clusterComputerRelationServiceIfc.updateClusterComputerRelationEntity(clusterComputerRelation);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/delteClusterComputerRelationinfo", method = RequestMethod.POST)
    @ResponseBody
    public RespModel deleteClusterComputerRelationinfo(Long clusterid) throws Exception {
        clusterComputerRelationServiceIfc.deleteClusterComputerRelationEntity(clusterid);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }
}
