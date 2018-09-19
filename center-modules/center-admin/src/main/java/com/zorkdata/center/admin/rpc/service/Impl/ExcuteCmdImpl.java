package com.zorkdata.center.admin.rpc.service.Impl;

import com.alibaba.fastjson.JSON;
//import com.zorkdata.center.admin.rpc.OperationExcuteHistory;
import com.zorkdata.center.admin.agent.core.SaltApi;
import com.zorkdata.center.admin.biz.AgentBiz;
import com.zorkdata.center.admin.biz.JobBiz;
import com.zorkdata.center.admin.biz.SaltJobBiz;
import com.zorkdata.center.admin.biz.SaltJobRetBiz;
import com.zorkdata.center.admin.entity.*;
import com.zorkdata.center.admin.rest.AgentInfoController;
import com.zorkdata.center.admin.rpc.service.Ifc.ExcuteCmdIfc;
import com.zorkdata.center.admin.rpc.service.Ifc.ScriptServiceIfc;
import com.zorkdata.center.admin.util.HttpClientCenterUtil;
import com.zorkdata.center.admin.vo.SaltProperties;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.exception.AuthenticationException;
import com.zorkdata.center.common.exception.EncodingException;
import com.zorkdata.center.common.exception.ExcuteException;
import com.zorkdata.center.common.salt.netapi.AuthModule;
import com.zorkdata.center.common.salt.netapi.calls.LocalCall;
import com.zorkdata.center.common.salt.netapi.calls.modules.Cmd;
import com.zorkdata.center.common.salt.netapi.client.SaltClient;
import com.zorkdata.center.common.salt.netapi.config.ClientConfig;
import com.zorkdata.center.common.salt.netapi.datatypes.target.MinionList;
import com.zorkdata.center.common.salt.netapi.exception.SaltException;
import com.zorkdata.center.common.salt.netapi.results.Result;
import com.zorkdata.center.common.util.NetTelnet;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.*;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/5/24 22:30
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ExcuteCmdImpl implements ExcuteCmdIfc {
    public static final String ZORK_OMSAPI = "zork-omsapi";
    public static final String ZORKDATA_8888 = "zorkdata.8888";
    public static final String REGEX = ":";
    public static final String LANG = "lang";
    public static final String CODE = "code";
    public static final String MINIONS = "minions";
    @Autowired
    AgentBiz agentBiz;

    @Autowired
    private SaltJobBiz saltJobBiz;

    @Autowired
    private SaltJobRetBiz saltJobRetBiz;

    @Autowired
    private JobBiz jobBiz;

    @Autowired
    private ScriptServiceIfc scriptServiceIfc;

    private Logger logger = LoggerFactory.getLogger(ExcuteCmdImpl.class);
    @Override
    public Map<String, String> fastExcute(Map<String, Object> map) {
        Map<String, String> result = new HashMap<>(16);
        Map<String, String> success = new HashMap<>(16);
        Map<String, String> failure = new HashMap<>(16);
        //测试完毕后，这里的saltProperties值要改为从数据库中取
        SaltProperties saltProperties = SaltProperties.getSaltProperties();
        Integer timeout = Integer.parseInt(map.get("timeout").toString());
        Integer socketTime;
        if (timeout != 0) {
            socketTime = timeout * 60 * 1000 + 100 * 1000;
        } else {
            socketTime = saltProperties.getSocketTimeout();
        }

        List<String> targets = JSON.parseArray(map.get("targets").toString(), String.class);

        //根据target所属不同的master,得到saltClient：URI.create(masterIp)
        Map<String, List<String>> masterMap = new HashMap<>(16);
        if (targets != null && targets.size() > 0) {
            //根据targets获得登录认证信息
            try {
                List<Agent2Master> agent2MasterList = new ArrayList<>();
                agent2MasterList = agentBiz.seleteAgent2Master(targets);
                Map<String, List<String>> finalmasterMap = new HashMap<>(16);
                agent2MasterList.forEach(r -> {
                    String api = r.getSaltApi();
                    List<AgentName> agentTemp = r.getAgentList();
                    List<String> agents = new ArrayList<>();
                    agentTemp.forEach(agent -> {
                        agents.add(agent.getName());
                    });
                    finalmasterMap.put(api, agents);
                });
                masterMap = finalmasterMap;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Map<SaltClient, List<String>> masterUriMap = new HashMap<>();
        for (String masterIp : masterMap.keySet()) {
            SaltClient saltClient = new SaltClient(URI.create("http://" + masterIp));
            masterUriMap.put(saltClient, masterMap.get(masterIp));
        }
        Date beginTimeBak = new Timestamp(System.currentTimeMillis());
        Date endTimeBak = null;
        String content;
        try {
            content = URLDecoder.decode(map.get("content").toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            Date errorEndTime = new Timestamp(System.currentTimeMillis());
            e.printStackTrace();
//            saveOperationExcuteHistory("0",map.get("name").toString(),beginTimeBak,errorEndTime,map.get("account").toString(),null,"以utf-8解码错误");
            throw new EncodingException(CodeTable.ENCODING_ERROR, "以utf-8解码错误");
        }
        for (SaltClient keySaltClient : masterUriMap.keySet()) {
            try {
                keySaltClient.login(saltProperties.getUserName(), saltProperties.getPassword(), AuthModule.PAM);
                keySaltClient.getConfig().put(ClientConfig.SOCKET_TIMEOUT, socketTime);
            } catch (SaltException e) {
                e.printStackTrace();
                throw new AuthenticationException(CodeTable.AGENT_AUTHENTICATION_ERROR, "Agent认证失败");
            }
            Map<String, Result<String>> response;

            try {
                content = URLDecoder.decode(map.get("content").toString(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                Date errorEndTime = new Timestamp(System.currentTimeMillis());
                e.printStackTrace();
//                saveOperationExcuteHistory("0",map.get("name").toString(),beginTimeBak,errorEndTime,map.get("account").toString(),null,"以utf-8解码错误");
                throw new EncodingException(CodeTable.ENCODING_ERROR, "以utf-8解码错误");
            }
            try {
                LocalCall<String> call;
                if (timeout == 0) {
                    call = Cmd.execCode(map.get("type").toString(), content);
                } else {
                    call = Cmd.execCode(map.get("type").toString(), content).withTimeouts(Optional.of(timeout * 60), Optional.empty());
                }
                response = call.callSync(keySaltClient, new MinionList(targets));
            } catch (SaltException e) {
                endTimeBak = new Timestamp(System.currentTimeMillis());
                e.printStackTrace();
//                saveOperationExcuteHistory("0",map.get("name").toString(),beginTimeBak,endTimeBak,map.get("account").toString(),content,"Agent执行异常");
                throw new ExcuteException(CodeTable.AGENT_EXCUTE_ERROR, "Agent执行异常");
            }

            if (response == null) {
                response = new HashMap<>(16);
            }
            for (String item : masterUriMap.get(keySaltClient)) {
                Result<String> stringResult = response.get(item);
                if (stringResult == null) {
                    result.put(item, "执行失败 ");
                } else if (stringResult.toString().contains("(null)")) {
                    result.put(item, null);
                } else {
                    result.put(item, stringResult.result().isPresent() ? stringResult.result().get() : "执行失败");
                }
            }
            result.forEach((k, v) -> {
                if (v == null || StringUtils.isBlank(v.toString()) || v.toString().contains("执行失败") || v.toString().toLowerCase().contains("error")) {
                    failure.put(k, v);
                } else {
                    success.put(k, v);
                }
            });
        }
        endTimeBak = new Timestamp(System.currentTimeMillis());
        result = new HashMap<>(16);
        result.put("success", JSON.toJSONString(success));
        result.put("failure", JSON.toJSONString(failure));

//        saveOperationExcuteHistory("1",map.get("name").toString(),beginTimeBak,endTimeBak,map.get("account").toString(),content,JSON.toJSONString(result));
        return result;
    }
    //上面那个saveOperationExcuteHistory的执行暂时需要设计到自动化的表，中台管不了，暂时先注了
//    private void saveOperationExcuteHistory(String status,String name, Date beginTimeBak, Date endTimeBak,String account,String content, Object msg) {
//        OperationExcuteHistory operationExcuteHistory = new OperationExcuteHistory();
//        try {
//            operationExcuteHistory.saveFastExcuteHistory(status, 0, name, beginTimeBak,endTimeBak,"0","2",account,content,msg);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    @Override
    public Map<Long, Map<String, List<String>>> asyncExecCode(Map<String, Object> map) {
        try {
            List<String> minions = (List<String>) map.get(MINIONS);
            String lang = String.valueOf(map.get(LANG));
            String code = String.valueOf(map.get(CODE));
            LinkedHashMap<String, Object> args1 = new LinkedHashMap<>();
            List<Agent2Master> list = agentBiz.seleteAgent2Master(minions);
            if (list != null && list.size() > 0) {
                // jobid -> saltjobid ->agent ret
                Job job = new Job();
                job.setCreatetime(new Date());
                jobBiz.insertJob(job);
                Map<Long, Map<String, List<String>>> result = new HashMap<>();
                for (Agent2Master agent2Master : list) {
                    String[] ip_port = agent2Master.getSaltApi().split(REGEX);
                    String ips = ip_port[0];
                    String ports = ip_port[1];
                    String computerType = ip_port[2];
                    String computerip = NetTelnet.getConnectedIp(ips, computerType);
                    SaltApi saltApi = SaltApi.getSaltApi(computerip, Integer.parseInt(ports), ZORK_OMSAPI, ZORKDATA_8888);
                    try {
                        List<String> agents = new ArrayList<>();
                        List<AgentName> agentTemp = agent2Master.getAgentList();
                        agentTemp.forEach(agent -> {
                            agents.add(agent.getName());
                        });
                        Map<String, List<String>> ret = saltApi.asyncExecCode(agents, lang, code, args1);
                        if (result.containsKey(job.getJobid())) {
                            result.get(job.getJobid()).putAll(ret);
                        } else {
                            result.put(job.getJobid(), ret);
                        }
                        for (String jid : ret.keySet()) {
                            SaltJob saltJob = new SaltJob();
                            saltJob.setJobid(job.getJobid());
                            saltJob.setJid(jid);
                            List<SaltJob> listSaltJob = new ArrayList<>();
                            listSaltJob.add(saltJob);
                            saltJobBiz.insertBatch(listSaltJob);
                            for (String agent1 : agents) {
                                SaltJobRet saltJobRet = new SaltJobRet();
                                saltJobRet.setJid(jid);
                                saltJobRet.setMinionid(agent1);
                                List<SaltJobRet> listSaltJobRet = new ArrayList<>();
                                listSaltJobRet.add(saltJobRet);
                                saltJobRetBiz.insertBatch(listSaltJobRet);
                            }
                        }
                    } catch (InterruptedException e) {
                        logger.error("sleep 异常",e);
                    } catch (SaltException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return result;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
