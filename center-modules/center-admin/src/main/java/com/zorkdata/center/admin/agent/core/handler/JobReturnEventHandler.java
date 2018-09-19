package com.zorkdata.center.admin.agent.core.handler;


import com.alibaba.fastjson.JSON;
import com.zorkdata.center.admin.agent.core.SaltApi;
import com.zorkdata.center.admin.agent.core.async.Handler;
import com.zorkdata.center.admin.agent.core.async.Looper;
import com.zorkdata.center.admin.agent.core.async.Message;
import com.zorkdata.center.admin.biz.*;
import com.zorkdata.center.admin.entity.*;
import com.zorkdata.center.admin.rpc.MyWebSocket;
import com.zorkdata.center.admin.util.SpringUtil;
import com.zorkdata.center.common.salt.netapi.event.JobReturnEvent;
import com.zorkdata.center.common.util.NetTelnet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: saltJob返回事件处理
 * @author: zhuzhigang
 * @create: 2018/5/30 13:26
 */
public class JobReturnEventHandler extends Handler {
    private static final Logger logger = LoggerFactory.getLogger(JobReturnEventHandler.class);
    private static final String CMD_EXEC_CODE = "cmd.exec_code";
    private static final String CP_GET_FILE = "cp.get_file";
    private static final String WINDOWSCOMPUTER = "windowscomputer";
    private static final String LINUXCOMPUTER = "linuxcomputer";
    private static final String WINDOWS = "Windows";
    private static final String USERNAME = "username";
    private static final String MASTER = "master";
    private static final String STRING = "[";
    private static final String EVENT = "event";
    private static final String PORTS = "ports";
    private static final String PASSWORD = "password";
    private static final String GRAINS_ITEMS = "grains.items";
    private static final String FQDN_IP_4 = "fqdn_ip4";
    private static final String HOST = "host";
    private static final String OS = "os";
    private static final String COMPUTER_TYPE = "ComputerType";
    private static final String IP = "IP";
    private static final String STATE = "State";
    private static final String VERSION = "3.0";
    private static final String STRING1 = "";
    private static final String STRING2 = ",";
    private static final String EVENT_SEND = "event.send";
    private static final String UNINSTALL_AGENT = "[\"uninstall_agent\"]";
    private static final String STRING127 = "127.0.0.1";
    private static final String IPV_4 = "ipv4";
    private static final String RexStr = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
    private SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

    public JobReturnEventHandler(Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
        try {
            ComputerBiz computerBiz = SpringUtil.getBean(ComputerBiz.class);
            MasterBiz masterBiz = SpringUtil.getBean(MasterBiz.class);
            AgentBiz agentBiz = SpringUtil.getBean(AgentBiz.class);
            ClusterComputerRelationBiz clusterComputerRelationBiz = SpringUtil.getBean(ClusterComputerRelationBiz.class);
            SaltJobRetBiz saltJobRetBiz = SpringUtil.getBean(SaltJobRetBiz.class);
            SaltJobBiz saltJobBiz = SpringUtil.getBean(SaltJobBiz.class);
            Map map = msg.getData();
            JobReturnEvent jobReturnEvent = (JobReturnEvent) map.get(EVENT);
            int ports = Integer.parseInt(String.valueOf(map.get(PORTS)));
            String username = String.valueOf(map.get(USERNAME));
            String password = String.valueOf(map.get(PASSWORD));
            String jid = jobReturnEvent.getJobId();
            String minionId = jobReturnEvent.getMinionId();
            JobReturnEvent.Data data = jobReturnEvent.getData();
            if (data.getFun().contentEquals(GRAINS_ITEMS)) {
                if (data.getRetcode() == 0) {
                    Map<String, Object> mapResult = (Map<String, Object>) data.getResult();
                    List<String> ips = (List<String>) mapResult.get(FQDN_IP_4);
                    if (ips == null || ips.size() == 0) {
                        ips = (List<String>) mapResult.get(IPV_4);
                    }
                    String hostname = String.valueOf(mapResult.get(HOST));
                    String osType = String.valueOf(mapResult.get(OS));
                    if (osType.startsWith(WINDOWS)) {
                        osType = WINDOWSCOMPUTER;
                    } else {
                        osType = LINUXCOMPUTER;
                    }
                    String username1 = String.valueOf(mapResult.get(USERNAME));
                    String master = String.valueOf(mapResult.get(MASTER)).trim();
                    if (master.contains(STRING)) {
                        List<String> listmaster = (List<String>) mapResult.get(MASTER);
                        updateAgentComputers(computerBiz, masterBiz, agentBiz, clusterComputerRelationBiz, ips, hostname, osType, username1, listmaster.get(0), minionId, false);
                        updateAgentComputers(computerBiz, masterBiz, agentBiz, clusterComputerRelationBiz, ips, hostname, osType, username1, listmaster.get(1), minionId, true);
                    } else {
                        updateAgentComputers(computerBiz, masterBiz, agentBiz, clusterComputerRelationBiz, ips, hostname, osType, username1, master, minionId, false);
                    }
                    logger.info(JSON.toJSONString(mapResult));
                }
            } else if (data.getFun().contentEquals(CMD_EXEC_CODE) || data.getFun().contentEquals(CP_GET_FILE)) {
                SaltJobRet saltJobRet;
                saltJobRet = new SaltJobRet();
                saltJobRet.setJid(jid);
                saltJobRet.setMinionid(minionId);
                saltJobRet = saltJobRetBiz.selectOne(saltJobRet);
                if (saltJobRet != null) {
                    if (saltJobRet.getFun() == null) {
                        synchronized (saltJobRet) {
                            saltJobRet.setCmd(data.getCmd());
                            saltJobRet.setFun(data.getFun());
                            saltJobRet.setFun_args(JSON.toJSONString(data.getFunArgs()));
                            saltJobRet.setRetcode(data.getRetcode());
                            saltJobRet.setReturns(String.valueOf(data.getResult()));
                            saltJobRet.setSuccess(data.getRetcode());
                            Date date;
                            try {
                                date = format0.parse(data.getTimestamp());
                            } catch (ParseException e) {
                                e.printStackTrace();
                                date = new Date();
                            }

                            saltJobRet.setStamp(date);
                            List<SaltJobRet> saltJobRetArrayList = new ArrayList<>();
                            saltJobRetArrayList.add(saltJobRet);

                            saltJobRetBiz.insertBatch(saltJobRetArrayList);
                            SaltJob saltJob = new SaltJob();
                            saltJob.setJid(jid);
                            saltJob = saltJobBiz.selectOne(saltJob);
                            try {
                                logger.info("发送websocket：" + saltJob.getJobid());
                                MyWebSocket.sendInfo(String.valueOf(saltJob.getJobid()), JSON.toJSONString(saltJobRet));
                            } catch (IOException e) {
                                e.printStackTrace();
                                logger.error("发送websocket：" + saltJob.getJobid(), e);
                            }
                        }
                    } else {
                        logger.info("注意,minionId：" + minionId + "启动了多个");
                    }
                }
            } else if (data.getFun().contentEquals(EVENT_SEND)) {
                if (JSON.toJSONString(data.getFunArgs()).equals(UNINSTALL_AGENT)) {
                    Agent agent = new Agent();
                    agent.setAgentName(minionId);
                    agentBiz.delete(agent);
                    List<Map<String, Object>> apis = masterBiz.getApiInfo();
                    for (Map<String, Object> api : apis) {
                        String computerType = String.valueOf(api.get(COMPUTER_TYPE));
                        String computerip1 = NetTelnet.getConnectedIp(String.valueOf(api.get(IP)), computerType);

                        int state = Integer.parseInt(String.valueOf(api.get(STATE)));
                        if (computerip1 != null && state == 1) {
                            try {
                                SaltApi saltApi = SaltApi.getSaltApi(computerip1, ports, username, password);
                                saltApi.deleteKey(minionId);
                            } catch (Exception e) {
                                logger.error("删除key：" + minionId + "失败", e);
                            }

                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("JobReturnEventHandler异常", e);
        }
    }

    private void updateAgentComputers(ComputerBiz computerBiz, MasterBiz masterBiz, AgentBiz agentBiz, ClusterComputerRelationBiz clusterComputerRelationBiz, List<String> ips, String hostname, String osType, String username, String masterip, String minionID, boolean isBackMaster) {
        // 查询master的主机
        // TODO 这个地方如果Computer有多个重复的IP可能会有问题，需要删除重复IP地址记录
        Computer mastercomputer = computerBiz.getComputerByComputerIP(masterip);
        // 根据master的主机查询 master
        Master master = masterBiz.selectMasterByComputerID(mastercomputer.getComputerID());
        // 查询这个agent是否在Agent表中存在
        if (ips.contains(STRING127)) {
            ips.remove(STRING127);
        }
        if (ips.size() == 0 && isIP(minionID)) {
            ips.add(minionID);
        }
        List<Computer> computers = computerBiz.getComputersByIps(ips);
        if (computers.size() > 1) {
            logger.info("ip为:" + ips + ",查询出computer表存在多条记录");
            return;
        }


        // 根据master ip去computer 和 master关联查询出masterID和域的ID，如果有备master则同样查询出备的masterID和域的ID，如果域的ID不一致，则可能有问题
        // 根据 ip去查询 有没有这个主机，没有则插入返回computerID
        // 更新Agent表，更新ClusterComputerRelation表
        if (computers != null && computers.size() > 0) {
            for (Computer computer : computers) {
                if (computer.getIp().equals(STRING127)) {
                    logger.info("主机的ip为127.0.0.1，minionid为：" + minionID + "需要管理员 删除computer表中的数据");
                    continue;
                }
                Agent agent = new Agent();
                agent.setAgentName(minionID);
                agent.setComputerID(computer.getComputerID());
                agent.setMasterID(master.getMasterID());
                agent.setState(1);
                agent.setBackState(2);
                agent.setVersion(VERSION);
                agent.setLastModifyTime(new Date());
                agent.setLastTime(new Date());
                List<Agent> listAgents = new ArrayList<>();
                List<ClusterComputerRelation> clusterComputerRelationList = new ArrayList<>();
                ClusterComputerRelation clusterComputerRelation = new ClusterComputerRelation();
                clusterComputerRelation.setClusterID(master.getClusterID());
                clusterComputerRelation.setComputerID(master.getComputerID());
                ClusterComputerRelation clusterComputerRelation1 = new ClusterComputerRelation();
                clusterComputerRelation1.setClusterID(master.getClusterID());
                clusterComputerRelation1.setComputerID(agent.getComputerID());
                clusterComputerRelationList.add(clusterComputerRelation);
                clusterComputerRelationList.add(clusterComputerRelation1);
                listAgents.add(agent);
                agentBiz.insertBatch(listAgents);
                clusterComputerRelationBiz.insertBatch(clusterComputerRelationList);
                // 更新主机computer类型和主机名
                computer.setComputerType(osType);
                computer.setHostName(hostname);
                computer.setLastModifyTime(new Date());
                computerBiz.updateComputer(computer);
            }
        } else {
            Computer computer = new Computer();
            computer.setComputerType(osType);
            computer.setHostName(hostname);
            Collections.sort(ips);
            String ipss = STRING1;
            for (int i = 0; i < ips.size(); i++) {
                if (i == ips.size() - 1) {
                    ipss += ips.get(i);
                } else {
                    ipss += ips.get(i) + STRING2;
                }
            }
            if (!STRING127.equals(ipss)) {
                computer.setIp(ipss);
                computer.setUserName(username);
                // 插入computer表 返回ID
                computerBiz.insertSelective(computer);

                Agent agent = new Agent();
                agent.setAgentName(minionID);
                agent.setComputerID(computer.getComputerID());
                if (isBackMaster) {
                    agent.setMasterBackID(master.getMasterID());
                    agent.setBackState(1);
                    agent.setState(1);
                } else {
                    agent.setMasterID(master.getMasterID());
                    agent.setState(1);
                    agent.setBackState(2);
                }
                agent.setVersion(VERSION);
                agent.setLastModifyTime(new Date());
                agent.setLastTime(new Date());

                List<Agent> listAgents = new ArrayList<>();
                List<ClusterComputerRelation> clusterComputerRelationList = new ArrayList<>();
                ClusterComputerRelation clusterComputerRelation = new ClusterComputerRelation();
                clusterComputerRelation.setClusterID(master.getClusterID());
                clusterComputerRelation.setComputerID(master.getComputerID());
                ClusterComputerRelation clusterComputerRelation1 = new ClusterComputerRelation();
                clusterComputerRelation1.setClusterID(master.getClusterID());
                clusterComputerRelation1.setComputerID(agent.getComputerID());
                clusterComputerRelationList.add(clusterComputerRelation);
                clusterComputerRelationList.add(clusterComputerRelation1);
                listAgents.add(agent);
                agentBiz.insertBatch(listAgents);
                clusterComputerRelationBiz.insertBatch(clusterComputerRelationList);
            } else {
                logger.info("主机的ip为127.0.0.1，不插入库，minionid为：" + minionID + "需要手动插入库");
            }
        }
    }

    public boolean isIP(String addr) {
        if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
        String rexp = RexStr;
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(addr);
        boolean ipAddress = mat.find();
        return ipAddress;
    }
}
