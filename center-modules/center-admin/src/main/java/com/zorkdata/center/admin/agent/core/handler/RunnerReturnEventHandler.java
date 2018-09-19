package com.zorkdata.center.admin.agent.core.handler;

import com.zorkdata.center.admin.agent.core.async.Handler;
import com.zorkdata.center.admin.agent.core.async.Looper;
import com.zorkdata.center.admin.agent.core.async.Message;
import com.zorkdata.center.admin.biz.AgentBiz;
import com.zorkdata.center.admin.biz.MasterBiz;
import com.zorkdata.center.admin.entity.Agent;
import com.zorkdata.center.admin.entity.Master;
import com.zorkdata.center.admin.util.SpringUtil;
import com.zorkdata.center.common.salt.netapi.event.RunnerReturnEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.zorkdata.center.admin.agent.core.SaltApi.saltStatusMap;

/**
 * @description: runner返回时的处理器
 * @author: zhuzhigang
 * @create: 2018/5/30 13:26
 */
public class RunnerReturnEventHandler extends Handler {
    private static final Logger logger = LoggerFactory.getLogger(RunnerReturnEventHandler.class);
    public static final String EmptyString = "";

    public RunnerReturnEventHandler(Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
        AgentBiz agentBiz = SpringUtil.getBean(AgentBiz.class);
        Map map = msg.getData();
        String computerIP = String.valueOf(map.get("computerIP"));
        RunnerReturnEvent runnerReturnEvent = (RunnerReturnEvent) map.get("event");
        final RunnerReturnEvent.Data data = runnerReturnEvent.getData();
        if (data.getFun().contentEquals("runner.manage.status")) {

            try {
                if (data.getResult() != null && !data.getResult().equals(EmptyString)) {
                    Map<String, List<String>> obj = (Map<String, List<String>>) data.getResult();
                    List<String> downlist = obj.get("down");
                    List<String> uplist = obj.get("up");
                    List<Map<String, Object>> listupdate = new ArrayList<>();
                    // 2 是离线
                    for (String down : downlist) {
                        Map<String, Object> statusMap = new HashMap<>(16);
                        statusMap.put("state", 2);
                        statusMap.put("agentName", down);
                        listupdate.add(statusMap);
                    }
                    if (listupdate != null && listupdate.size() > 0) {
                        agentBiz.updateAgentBatch(listupdate);
                        agentBiz.updateAgentBackBatch(listupdate);
                    }
                    if (listupdate != null && listupdate.size() > 0) {
                        listupdate.clear();
                    }
                    // 1 是在线
                    for (String up : uplist) {
                        Map<String, Object> statusMap = new HashMap<>(16);
                        statusMap.put("state", 1);
                        statusMap.put("agentName", up);
                        listupdate.add(statusMap);
                    }
                    if (listupdate != null && listupdate.size() > 0) {
                        agentBiz.updateAgentBatch(listupdate);
                        agentBiz.updateAgentBackBatch(listupdate);
                    }
                    if (listupdate != null && listupdate.size() > 0) {
                        listupdate.clear();
                    }

                    Map<String, Object> updateTimeMap = new HashMap<>(16);
                    updateTimeMap.put("lastTime", new Date());
                    updateTimeMap.put("lastModifyTime", new Date());
                    agentBiz.updateAgentState(updateTimeMap);
                }
            } catch (Exception e) {
                logger.error(computerIP + "获取状态报错", e);
                logger.info("返回的结果为：" + data.getResult().toString());
            }
        }
    }
}
