package com.zorkdata.center.admin.rpc.service.Impl;

import com.zorkdata.center.admin.biz.*;
import com.zorkdata.center.admin.entity.Agent;
import com.zorkdata.center.admin.entity.Computer;
import com.zorkdata.center.admin.entity.Master;
import com.zorkdata.center.admin.rpc.service.Ifc.FindAllComputersInClusterIfc;
import com.zorkdata.center.admin.vo.ComputerAndComputerSaltType;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.salt.netapi.calls.LocalCall;
import com.zorkdata.center.common.salt.netapi.calls.modules.Cmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.codec.EncodingException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FindAllComputersInClusterImpl implements FindAllComputersInClusterIfc {
    @Autowired
    ClusterComputerRelationBiz clusterComputerRelationBiz;
    @Autowired
    MasterBiz masterBiz;
    @Autowired
    AgentBiz agentBiz;
    @Autowired
    ClusterBiz clusterBiz;
    @Autowired
    ComputerBiz computerBiz;

    @Override
    public List<ComputerAndComputerSaltType> getComputerAndComputerTypeList(Long clusterID) {
        List<ComputerAndComputerSaltType> computerAndComputerSaltTypeList = new ArrayList<ComputerAndComputerSaltType>();
        List<Computer> computerList = clusterBiz.getComputersByClusterID(clusterID);
        List<Master> masterList = masterBiz.getMasterList();
        List<Agent> agentList = agentBiz.getAgentList();
        for (Computer computerList1 : computerList) {
            boolean flag = false;
            ComputerAndComputerSaltType computerAndComputerSaltType = new ComputerAndComputerSaltType();
            //判断Master列表中是否有此机器
            Master master = null;
            for (Master master1 : masterList) {
                if (master1.getComputerID() != null && computerList1.getComputerID() != null) {
                    if (master1.getComputerID().equals(computerList1.getComputerID())) {
                        master = master1;
                    }
                }
            }
            //判断Agent列表中是否有此机器
            Agent agent = null;
            for (Agent agent1 : agentList) {
                if (agent1.getComputerID() != null && computerList1.getComputerID() != null) {
                    if (agent1.getComputerID().equals(computerList1.getComputerID())) {
                        agent = agent1;
                    }
                }
            }
            if (master != null && agent == null) {
                computerAndComputerSaltType.setComputer(computerList1);
                //约定1为master
                computerAndComputerSaltType.setComputerSaltType("1");
                computerAndComputerSaltType.setMasterID(master.getMasterID());
                computerAndComputerSaltType.setAgentID(null);
            } else if (master == null && agent != null) {
                computerAndComputerSaltType.setComputer(computerList1);
                //约定2为agent
                computerAndComputerSaltType.setComputerSaltType("2");
                computerAndComputerSaltType.setMasterID(null);
                computerAndComputerSaltType.setAgentID(agent.getAgentID());
            } else if (master != null && agent != null) {
                computerAndComputerSaltType.setComputer(computerList1);
                //约定3为即为Master也为Agent
                computerAndComputerSaltType.setComputerSaltType("3");
                computerAndComputerSaltType.setMasterID(master.getMasterID());
                computerAndComputerSaltType.setAgentID(agent.getAgentID());
            } else if (master == null && agent == null) {
                computerAndComputerSaltType.setComputer(computerList1);
                //约定4为此机器未被分配角色
                computerAndComputerSaltType.setComputerSaltType("4");
                computerAndComputerSaltType.setMasterID(null);
                computerAndComputerSaltType.setAgentID(null);
            }
            computerAndComputerSaltTypeList.add(computerAndComputerSaltType);
        }
        return computerAndComputerSaltTypeList;
    }

    @Override
    public ComputerAndComputerSaltType getComputerAndComputerType(Computer computer) {
        ComputerAndComputerSaltType computerAndComputerSaltType = new ComputerAndComputerSaltType();
        //判断Master表中是否有此机器
        Master master = null;
        master = masterBiz.selectMasterByComputerID(computer.getComputerID());
        //判断Agent表中是否有此机器
        Agent agent = null;
        agent = agentBiz.selectAgentByComputerID(computer.getComputerID());
        if (master != null && agent == null) {
            computerAndComputerSaltType.setComputer(computer);
            //约定1为master
            computerAndComputerSaltType.setComputerSaltType("1");
            computerAndComputerSaltType.setMasterID(master.getMasterID());
            computerAndComputerSaltType.setAgentID(null);
        } else if (master == null && agent != null) {
            computerAndComputerSaltType.setComputer(computer);
            //约定2为agent
            computerAndComputerSaltType.setComputerSaltType("2");
            computerAndComputerSaltType.setMasterID(null);
            computerAndComputerSaltType.setAgentID(agent.getAgentID());
        } else if (master != null && agent != null) {
            computerAndComputerSaltType.setComputer(computer);
            //约定3为即为Master也为Agent
            computerAndComputerSaltType.setComputerSaltType("3");
            computerAndComputerSaltType.setMasterID(master.getMasterID());
            computerAndComputerSaltType.setAgentID(agent.getAgentID());
        } else if (master == null && agent == null) {
            computerAndComputerSaltType.setComputer(computer);
            //约定4为此机器未被分配角色
            computerAndComputerSaltType.setComputerSaltType("4");
            computerAndComputerSaltType.setMasterID(null);
            computerAndComputerSaltType.setAgentID(null);
        }
        return computerAndComputerSaltType;
    }

    @Override
    public List<ComputerAndComputerSaltType> getComputerAndComputerTypeListByUserID(Long userId) {
        List<ComputerAndComputerSaltType> computerAndComputerSaltTypeList = new ArrayList<ComputerAndComputerSaltType>();
        List<Computer> computerList = computerBiz.getComputerList(userId);
        if (computerList == null) {
            return computerAndComputerSaltTypeList;
        }
        List<Master> masterList = masterBiz.getMasterList();
        List<Agent> agentList = agentBiz.getAgentList();
        for (Computer computerList1 : computerList) {
            boolean flag = false;
            ComputerAndComputerSaltType computerAndComputerSaltType = new ComputerAndComputerSaltType();
            //判断Master列表中是否有此机器
            Master master = null;
            for (Master master1 : masterList) {
                if (master1.getComputerID() != null && computerList1.getComputerID() != null) {
                    if (master1.getComputerID().equals(computerList1.getComputerID())) {
                        master = master1;
                    }
                }
            }
            //判断Agent列表中是否有此机器
            Agent agent = null;
            for (Agent agent1 : agentList) {
                if (agent1.getComputerID() != null && computerList1.getComputerID() != null) {
                    if (agent1.getComputerID().equals(computerList1.getComputerID())) {
                        agent = agent1;
                    }
                }
            }
            if (master != null && agent == null) {
                computerAndComputerSaltType.setComputer(computerList1);
                //约定1为master
                computerAndComputerSaltType.setComputerSaltType("1");
                computerAndComputerSaltType.setMasterID(master.getMasterID());
                computerAndComputerSaltType.setAgentID(null);
            } else if (master == null && agent != null) {
                computerAndComputerSaltType.setComputer(computerList1);
                //约定2为agent
                computerAndComputerSaltType.setComputerSaltType("2");
                computerAndComputerSaltType.setMasterID(null);
                computerAndComputerSaltType.setAgentID(agent.getAgentID());
            } else if (master != null && agent != null) {
                computerAndComputerSaltType.setComputer(computerList1);
                //约定3为即为Master也为Agent
                computerAndComputerSaltType.setComputerSaltType("3");
                computerAndComputerSaltType.setMasterID(master.getMasterID());
                computerAndComputerSaltType.setAgentID(agent.getAgentID());
            } else if (master == null && agent == null) {
                computerAndComputerSaltType.setComputer(computerList1);
                //约定4为此机器未被分配角色
                computerAndComputerSaltType.setComputerSaltType("4");
                computerAndComputerSaltType.setMasterID(null);
                computerAndComputerSaltType.setAgentID(null);
            }
            computerAndComputerSaltTypeList.add(computerAndComputerSaltType);
        }
        return computerAndComputerSaltTypeList;
    }

}
