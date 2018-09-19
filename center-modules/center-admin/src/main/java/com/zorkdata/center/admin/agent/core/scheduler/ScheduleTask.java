package com.zorkdata.center.admin.agent.core.scheduler;

import com.alibaba.fastjson.JSON;
import com.zorkdata.center.admin.agent.core.SaltApi;
import com.zorkdata.center.admin.biz.AgentBiz;
import com.zorkdata.center.admin.biz.MasterBiz;
import com.zorkdata.center.admin.entity.Agent;
import com.zorkdata.center.common.util.NetTelnet;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


/**
 * @author zhuzhigang
 */
@Component
public class ScheduleTask implements Job {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleTask.class);
    private static final String MINIONS = "minions";

    @Autowired
    private MasterBiz masterBiz;

    @Autowired
    private AgentBiz agentBiz;


    /**
     * 1、首先查询salt-api 有没有 接受的key，根据key去获取状态
     * 1.1 获取master -> salt-api
     * 1.2 查询 所有 状态
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("查询状态定时任务");
        List<Map<String, Object>> apis = masterBiz.getApiInfo();
        logger.info(JSON.toJSONString(apis));
        Map<String, List<Long>> minionMasterMap = new HashMap<>(16);
        for (Map<String, Object> api : apis) {
            String computerType = String.valueOf(api.get("ComputerType"));
            String computerip = NetTelnet.getConnectedIp(String.valueOf(api.get("IP")), computerType);
            String apiPort = String.valueOf(api.get("ApiPort"));
            Long masterID = Long.parseLong(String.valueOf(api.get("MasterID")));
            int state = Integer.parseInt(String.valueOf(api.get("State")));
            String username = "zork-omsapi";
            String password = "zorkdata.8888";
            logger.info("api的ip为:" + computerip);
            if (computerip != null) {
                try {
                    SaltApi saltApi = SaltApi.getSaltApi(computerip, Integer.parseInt(apiPort), username, password);
                    saltApi.manageStatus();
                    List<String> listminions = saltApi.listAllKey(MINIONS);
                    listminions.forEach(item -> {
                        if (minionMasterMap.containsKey(item)) {
                            minionMasterMap.get(item).add(masterID);
                        } else {
                            List<Long> listMasterID = new ArrayList<>();
                            listMasterID.add(masterID);
                            minionMasterMap.put(item, listMasterID);
                        }
                    });
                    Map<String, Object> map = new HashMap<>();
                    map.put("state", 1);
                    map.put("lastModifyTime", new Date());
                    map.put("masterID", masterID);
                    masterBiz.updateMaster(map);
                } catch (Exception e) {
                    logger.error("调度获取agent列表失败,更新master和agent状态",e);
                    updateMasterAndAgentStates(masterID);
                }
            } else {
                logger.error(api.get("IP") + " 8000端口连不上，更新agent和master状态");
                updateMasterAndAgentStates(masterID);
            }
        }
        // 简单处理备masterID的情况
        List<Agent> masterbackIDNullList = agentBiz.getMasterBackIDNull();
        masterbackIDNullList.forEach(item -> {
            if (minionMasterMap.containsKey(item.getAgentName()) && minionMasterMap.get(item.getAgentName()).size() > 1) {
                List<Long> listmasters = minionMasterMap.get(item.getAgentName());
                if (listmasters.get(0).equals(item.getMasterID())) {
                    item.setMasterBackID(listmasters.get(1));
                } else {
                    item.setMasterBackID(listmasters.get(0));
                }
                agentBiz.updateAgent(item);
            }
        });

    }

    private void updateMasterAndAgentStates(Long masterID) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("state", 2);
        map.put("masterID", masterID);
        map.put("lastModifyTime", new Date());
        masterBiz.updateMaster(map);
        Map<String, Object> statusMap = new HashMap<>(16);
        statusMap.put("state", 2);
        statusMap.put("masterID", masterID);
        agentBiz.updateAgentBack(statusMap);
        agentBiz.updateAgent(statusMap);
    }
}