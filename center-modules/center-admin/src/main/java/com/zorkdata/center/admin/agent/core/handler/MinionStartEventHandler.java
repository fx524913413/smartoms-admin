package com.zorkdata.center.admin.agent.core.handler;

import com.zorkdata.center.admin.agent.core.SaltApi;
import com.zorkdata.center.admin.agent.core.async.Handler;
import com.zorkdata.center.admin.agent.core.async.Looper;
import com.zorkdata.center.admin.agent.core.async.Message;
import com.zorkdata.center.admin.biz.AgentBiz;
import com.zorkdata.center.admin.entity.Agent;
import com.zorkdata.center.admin.util.SpringUtil;
import com.zorkdata.center.common.salt.netapi.event.MinionStartEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description: Minion启动时Handler处理器
 * @author: zhuzhigang
 * @create: 2018/5/30 13:26
 */
public class MinionStartEventHandler extends Handler {
    private static final Logger logger = LoggerFactory.getLogger(MinionStartEventHandler.class);

    public MinionStartEventHandler(Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
        AgentBiz agentBiz = SpringUtil.getBean(AgentBiz.class);
        Map map = msg.getData();
        String computerIP = String.valueOf(map.get("computerIP"));
        int ports = Integer.parseInt(String.valueOf(map.get("ports")));
        String username = String.valueOf(map.get("username"));
        String password = String.valueOf(map.get("password"));
        MinionStartEvent minionStartEvent = (MinionStartEvent) map.get("event");
        String minionId = minionStartEvent.getMinionId();
        List<String> minionList = new ArrayList<>();
        minionList.add(minionId);
        // 首先 查询获取agent表中的数据，如果不存在 则插入
        Agent agent = new Agent();
        Date date = new Date();
        agent.setAgentName(minionId);
        agent.setState(1);
        agent.setBackState(2);
        agent.setLoginTime(date);
        agent.setLastTime(date);
        agent.setLastModifyTime(date);
        agent.setInstallationType("2");
        agent.setVersion("3.0");
        List<Agent> listAgent = new ArrayList<>();
        listAgent.add(agent);
        agentBiz.insertBatch(listAgent);

        // 获取所有的grains.items
        try {
            SaltApi saltApi = SaltApi.getSaltApi(computerIP, ports, username, password);
            // 获取Agent端所有的grains.items
            saltApi.asyncGrainsItems(minionList);
        } catch (Exception e) {
            logger.error("GrainsItems出错", e);
        }
    }
}
