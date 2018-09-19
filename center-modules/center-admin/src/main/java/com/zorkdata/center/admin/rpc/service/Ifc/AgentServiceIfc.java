package com.zorkdata.center.admin.rpc.service.Ifc;

import com.zorkdata.center.admin.entity.Agent;
import com.zorkdata.center.admin.vo.*;

import java.util.List;

public interface AgentServiceIfc {
    AgentInfo findAgentinfo(Long agentinfo);

    void insertAgentEntity(Agent agent);

    void updateAgentEntity(Agent agent);

    void deleteAgentEntity(Long agentid);

    String insAgent(AgentVo agentVo, Long userId, Long clusterId, String productCode);

    List<AgentVo2> findAllAgent(Long userId);

    List<Long> handInsAgent(AgentVo agentVo, Long userId, Long clusterID);

    AgentCommandVo getCommand2(Long masterID, Long agentID, Long userId,Long clusterID);

    List<Agent> getAllAgent();

    List<AgentStateSum> countAgentState(Long userId);

    List<AgentCommandVo> getCommand(AgentVo agentVo, Long userId, Long clusterID, String productCode);

    /**
     * 通过agentName查询agent信息
     *
     * @param agentName
     * @return
     */
    Integer getAgentStateByAgentName(String agentName);
}
