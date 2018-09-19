package com.zorkdata.center.admin.rest;

import com.zorkdata.center.admin.biz.AgentBiz;
import com.zorkdata.center.admin.biz.ComputerBiz;
import com.zorkdata.center.admin.biz.MasterBiz;
import com.zorkdata.center.admin.entity.Agent;
import com.zorkdata.center.admin.entity.Agent2Master;
import com.zorkdata.center.admin.entity.Computer;
import com.zorkdata.center.admin.entity.Master;
import com.zorkdata.center.admin.rpc.service.Ifc.AgentServiceIfc;
import com.zorkdata.center.admin.vo.*;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import com.zorkdata.center.common.util.NetTelnet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("agent")
public class AgentInfoController {
    @Autowired
    private AgentServiceIfc agentServiceIfc;
    @Autowired
    private ComputerBiz computerBiz;
    @Autowired
    private MasterBiz masterBiz;
    @Autowired
    private AgentBiz agentBiz;

    @RequestMapping(value = "/agentinfo", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getAgentinfo(Long agentID) {
        AgentInfo agentInfo = agentServiceIfc.findAgentinfo(agentID);
        if (agentInfo == null) {
            return RespTools.getRespMsgModel(CodeTable.FAILED, null);
        } else {
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, agentInfo);
        }
    }


    @RequestMapping(value = "/addagentinfo", method = RequestMethod.POST)
    @ResponseBody
    public RespModel addAgentinfo(Agent agent) throws Exception {
        agentServiceIfc.insertAgentEntity(agent);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/updataagentinfo", method = RequestMethod.POST)
    @ResponseBody
    public RespModel updataAgentinfo(Agent agent) throws Exception {
        agentServiceIfc.updateAgentEntity(agent);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/delteagentinfo", method = RequestMethod.POST)
    @ResponseBody
    public RespModel deleteAgentinfo(Long agentid) throws Exception {
        agentServiceIfc.deleteAgentEntity(agentid);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/insAgent", method = RequestMethod.POST)
    @ResponseBody
    public RespModel insAgent(@RequestBody AgentVo agentVo, Long userId, Long clusterID, String productCode) {
        String reslut;
//        Long clusteridLongtype = Long.parseLong(clusterId);
        reslut = agentServiceIfc.insAgent(agentVo, userId, clusterID, productCode);
        if (reslut.equals("0")) {
            return RespTools.getRespMsgModel(CodeTable.FAILED, "安装失败");
        } else if (reslut.equals("账号或者密码错误")) {
            return RespTools.getRespMsgModel(CodeTable.FAILED, "账号或者密码错误");
        } else if (reslut.equals("2")) {
            return RespTools.getRespMsgModel(CodeTable.NOT_ALLOW, null);
        } else {
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, "安装成功");
        }
    }

    @RequestMapping(value = "/getAllAgent", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getAllAgent() {
        List<Agent> agentList = agentServiceIfc.getAllAgent();
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, agentList);
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ResponseBody
    public RespModel findAllAgent(Long userId) {
        List<AgentVo2> agentVo2List = agentServiceIfc.findAllAgent(userId);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, agentVo2List);
    }

    @RequestMapping(value = "/handInsAgent", method = RequestMethod.POST)
    @ResponseBody
    public RespModel handInsAgent(@RequestBody AgentVo agentVo, Long userId, Long clusterID) {
        List<Long> agentList = agentServiceIfc.handInsAgent(agentVo, userId, clusterID);
        if (agentList == null) {
            return RespTools.getRespMsgModel(CodeTable.FAILED, null);
        }
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, agentList);
    }

    /**
     * getCommand2是在Agent管理中获取已经安装好的Agent的接口
     *
     * @param masterID
     * @param agentID
     * @param userId
     * @param clusterID
     * @return
     */
    @RequestMapping(value = "/getCommand2", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getCommand2(Long masterID, Long agentID, Long userId, Long clusterID) {
        AgentCommandVo agentCommandVo = agentServiceIfc.getCommand2(masterID, agentID, userId, clusterID);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, agentCommandVo);
    }

    /**
     * getCommand是agent手动安装接口
     *
     * @param userId
     * @param agentVo
     * @param clusterID
     * @param productCode
     * @return
     */
    @RequestMapping(value = "/getCommand", method = RequestMethod.POST)
    @ResponseBody
    public RespModel getCommand(Long userId, @RequestBody AgentVo agentVo, Long clusterID, String productCode) {
        List<AgentCommandVo> agentCommandVoList = agentServiceIfc.getCommand(agentVo, userId, clusterID, productCode);
        if (agentCommandVoList == null) {
            return RespTools.getRespMsgModel(CodeTable.NOT_ALLOW, null);
        }
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, agentCommandVoList);
    }

    @RequestMapping(value = "/getCountAgentState", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getCountAgentState(Long userId) {
        List<AgentStateSum> agentStateSumList = agentServiceIfc.countAgentState(userId);
        if (agentStateSumList == null) {
            return RespTools.getRespMsgModel(CodeTable.FAILED, "This user don't have any computer");
        }
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, agentStateSumList);
    }

    @RequestMapping(value = "/getAgents", method = RequestMethod.POST)
    @ResponseBody
    public RespModel getAgents(@RequestBody List<String> minionIDList) {
        List<Agent2Master> agent2Masters = agentBiz.seleteAgent2Master(minionIDList);
//        for(Agent2Master agent2Master:agent2Masters){
//            String masterIp = agent2Master.getSaltApi();
//
//        }
        for (Agent2Master agent2Master : agent2Masters) {
            String[] ip_port = agent2Master.getSaltApi().split(":");
            String ips = ip_port[0];
            String port = ip_port[1];
            String computerType = ip_port[2];
            String connectedIp = NetTelnet.getConnectedIp(ips, computerType);
            String saltApi = connectedIp + ":" + port;
            agent2Master.setSaltApi(saltApi);
        }
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, agent2Masters);
    }

    @RequestMapping(value = "/getAgentsAndComputerByAgentID", method = RequestMethod.POST)
    @ResponseBody
    public RespModel getAgentsAndComputerByAgentID(@RequestBody List<AppInstanceIDAndAgentID> agentIDList) {
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, agentBiz.getAgentsAndComputerByAgentID(agentIDList));
    }

    @RequestMapping(value = "/getAgentNameByCiCode", method = RequestMethod.POST)
    @ResponseBody
    public RespModel getAgentNameByCiCode(@RequestBody List<String> ciCodeList) {
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, agentBiz.getAgentNameByHostName(ciCodeList));
    }

    @RequestMapping(value = "/getCiCodeByAgentName", method = RequestMethod.POST)
    @ResponseBody
    public RespModel getAgentNameByCiCode(@RequestBody String agentName) {
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, agentBiz.getCiCodeByAgentName(agentName));
    }
}
