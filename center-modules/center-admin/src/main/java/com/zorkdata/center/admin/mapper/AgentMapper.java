package com.zorkdata.center.admin.mapper;

import com.zorkdata.center.admin.entity.*;
import com.zorkdata.center.admin.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface AgentMapper extends Mapper<Agent> {
    void deleteByComputerID(@Param("computerID") Long computerID);

    Agent selectByComputerID(@Param("computerID") Long computerID);

    List<Agent> selectAllAgent();

    List<Agent> getMasterBackIDNull();

    List<Agent> getAllAgent();

    List<Agent> selectAgentByMasterID(@Param("masterID") Long masterID);

    List<Agent2Master> seleteAgent2Master(@Param("minionIDList") List minionIDList);

    Long insertAgent(Agent agent);

    void insertBatch(Agent agent);

    void deleteByComputerIds(@Param("computerIDlist") Set<Long> computerIDlist);

    List<AgentStateSum> countAgentState(@Param("computerIDlist") Set<Long> computerIDlist);

    Agent selectByAgentName(@Param("agentName") String agentName);

    void updateAgent(Map<String, Object> map);

    void updateAgentBatch(List<Map<String, Object>> list);

    AgentAndComputer getAgentsAndComputerByAgentID(@Param("agentID") Long agentID);

    void updateAgentBack(Map<String, Object> map);

    void updateAgentBackBatch(List<Map<String, Object>> list);

    void updateAgentState(Map<String, Object> map);

    List<ClusterMaster4AgentVo> getClusterMaster4Agent();


    /**
     * 通过agentname获取agent的信息
     *
     * @param agentName
     * @return
     */
    Integer getAgentStateByAgentName(@Param("agentName") String agentName);

    List<AgentNameAndCiCode> getAgentNameByHostName(@Param("ciCodeList") List<String> ciCodeList);

    AgentNameAndCiCode getCiCodeByAgentName(@Param("agentName") String agentName);
}
