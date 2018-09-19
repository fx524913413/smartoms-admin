package com.zorkdata.center.admin.biz;

import com.zorkdata.center.admin.entity.Agent;
import com.zorkdata.center.admin.entity.Agent2Master;
import com.zorkdata.center.admin.mapper.AgentMapper;
import com.zorkdata.center.admin.vo.*;
import com.zorkdata.center.common.biz.BaseBiz;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional(rollbackFor = Exception.class)
public class AgentBiz extends BaseBiz<AgentMapper, Agent> {
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public Agent getAgentByAgentId(Long agentId) {
        Agent agent = new Agent();
        agent.setAgentID(agentId);
        return mapper.selectOne(agent);
    }

    public List<Agent> getAgentList() {
        return mapper.selectAll();
    }

    public List<Agent> getMasterBackIDNull() {
        return mapper.getMasterBackIDNull();
    }

    public void updateAgent(Agent agent) {
        super.updateSelectiveById(agent);
    }

    public List<Agent> getAllAgent() {
        return mapper.getAllAgent();
    }

    public void deleteAgent(Long agentid) {
        super.deleteById(agentid);
    }

    public void deleteAgentByComputerID(Long computerID) {
        mapper.deleteByComputerID(computerID);
    }

    public Agent selectAgentByComputerID(Long computerID) {
        return mapper.selectByComputerID(computerID);
    }

    public List<Agent> selectAllAgent() {
        return mapper.selectAllAgent();
    }

    public List<Agent> selectAgentByMasterID(Long masterID) {
        return mapper.selectAgentByMasterID(masterID);
    }

    public List<Agent2Master> seleteAgent2Master(List minionIDList) {
        return mapper.seleteAgent2Master(minionIDList);
    }

    public List<AgentAndComputer> getAgentsAndComputerByAgentID(List<AppInstanceIDAndAgentID> agentIDList) {
        List<AgentAndComputer> agentAndComputersList = new ArrayList<>(16);
        agentIDList.forEach(appInstanceIDAndAgentID -> {
            Long appInstanceID = appInstanceIDAndAgentID.getAppInstanceID();
            Long agentID = appInstanceIDAndAgentID.getAgentID();
            //TODO 此处需要处理NPE
            AgentAndComputer agentAndComputer = mapper.getAgentsAndComputerByAgentID(agentID);
            agentAndComputer.setAppInstanceID(appInstanceID);
            agentAndComputersList.add(agentAndComputer);
        });
        return agentAndComputersList;
    }


    public void insertAgent(Agent agent) {
        mapper.insertAgent(agent);
    }

    public void deleteByComputerIds(Set<Long> computerIDlist) {
        mapper.deleteByComputerIds(computerIDlist);
    }

    public List<AgentStateSum> countAgentState(Set<Long> computerIds) {
        return mapper.countAgentState(computerIds);
    }

    public void updateAgent(Map<String, Object> map) {
        mapper.updateAgent(map);
    }

    public void updateAgentBack(Map<String, Object> map) {
        mapper.updateAgentBack(map);
    }

    public void updateAgentBatch(List<Map<String, Object>> list) {
        mapper.updateAgentBatch(list);
    }

    public void updateAgentBackBatch(List<Map<String, Object>> list) {
        mapper.updateAgentBackBatch(list);
    }

    public void updateAgentState(Map<String, Object> map) {
        mapper.updateAgentState(map);
    }

    public Agent selectByAgentName(String agentName) {
        return mapper.selectByAgentName(agentName);
    }

    public List<ClusterMaster4AgentVo> getClusterMaster4Agent() {
        return mapper.getClusterMaster4Agent();
    }

    public void insertBatch(List<Agent> agents) {
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        AgentMapper mapper = sqlSession.getMapper(AgentMapper.class);
        for (Agent agent : agents) {
            mapper.insertBatch(agent);
        }
        sqlSession.commit();
    }

    public Integer getAgentStateByAgentName(String agentName) {
        return mapper.getAgentStateByAgentName(agentName);
    }

    public List<AgentNameAndCiCode> getAgentNameByHostName(@Param("ciCodeList") List<String> ciCodeList) {
        return mapper.getAgentNameByHostName(ciCodeList);
    }

    public AgentNameAndCiCode getCiCodeByAgentName(String agentName) {
        return mapper.getCiCodeByAgentName(agentName);
    }
}

