package com.zorkdata.center.admin.rpc.service.Impl;

import com.zorkdata.center.admin.biz.AgentBiz;
import com.zorkdata.center.admin.biz.ClusterBiz;
import com.zorkdata.center.admin.biz.ClusterComputerRelationBiz;
import com.zorkdata.center.admin.biz.MasterBiz;
import com.zorkdata.center.admin.entity.*;
import com.zorkdata.center.admin.rpc.service.Ifc.UpdataComputerInClusterIfc;
import com.zorkdata.center.admin.vo.ComputerAndComputerSaltType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UpdataComputerInClusterImpl implements UpdataComputerInClusterIfc {
    @Autowired
    ClusterComputerRelationBiz clusterComputerRelationBiz;
    @Autowired
    MasterBiz masterBiz;
    @Autowired
    AgentBiz agentBiz;
    @Autowired
    ClusterBiz clusterBiz;

    @Override
    public void deleteComputerInCluster(Long clusterID, List<ComputerAndComputerSaltType> computerList) {
        for (ComputerAndComputerSaltType computerAndComputerSaltType : computerList) {
            Computer computer;
            ClusterComputerRelation clusterComputerRelation = new ClusterComputerRelation();
            String computerSaltType = computerAndComputerSaltType.getComputerSaltType();
            computer = computerAndComputerSaltType.getComputer();
            clusterComputerRelation.setClusterID(clusterID);
            clusterComputerRelation.setComputerID(computer.getComputerID());
            //在computer和cluster关系表中删除此电脑数据
            clusterComputerRelationBiz.delete(clusterComputerRelation);
            //判断Master列表中是否有此机器有就删除
            if (computerSaltType.equals("1")) {
                masterBiz.deleteMasterByComputerID(computer.getComputerID());
            }
            //判断Agent列表中是否有此机器有就删除
            if (computerSaltType.equals("2")) {
                agentBiz.deleteAgentByComputerID(computer.getComputerID());
            }
            //从Agent和Master中删除
            if (computerSaltType.equals("3")) {
                masterBiz.deleteMasterByComputerID(computer.getComputerID());
                agentBiz.deleteAgentByComputerID(computer.getComputerID());
            }
        }
    }

    @Override
    public void addComputerInCluster(Long clusterID, List<ComputerAndComputerSaltType> computerList) {
        for (ComputerAndComputerSaltType computerAndComputerSaltType : computerList) {
            Computer computer;
            ClusterComputerRelation clusterComputerRelation = new ClusterComputerRelation();
            computer = computerAndComputerSaltType.getComputer();
            clusterComputerRelation.setClusterID(clusterID);
            clusterComputerRelation.setComputerID(computer.getComputerID());
            //在computer和cluster关系表中添加此电脑数据
            clusterComputerRelationBiz.insert(clusterComputerRelation);
        }
    }

}
