package com.zorkdata.center.admin.rpc.service.Impl;

import com.jcraft.jsch.*;
import com.zorkdata.center.admin.biz.*;
import com.zorkdata.center.admin.config.FileConfiguration;
import com.zorkdata.center.admin.entity.*;
import com.zorkdata.center.admin.rpc.service.Ifc.AgentServiceIfc;
import com.zorkdata.center.admin.rpc.service.Ifc.FindAllComputersInClusterIfc;
import com.zorkdata.center.admin.util.PermissionCode;
import com.zorkdata.center.admin.util.PermissionUtil;
import com.zorkdata.center.admin.vo.*;
import com.zorkdata.center.common.util.NetTelnet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class AgentServiceImpl implements AgentServiceIfc {
    @Autowired
    private AgentBiz agentBiz;
    @Autowired
    private FileConfiguration fileConfiguration;
    @Autowired
    private ComputerBiz computerBiz;
    @Autowired
    private MasterBiz masterBiz;
    @Autowired
    private ClusterBiz clusterBiz;
    @Autowired
    private FindAllComputersInClusterIfc findAllComputersInClusterIfc;
    @Autowired
    private ClusterComputerRelationBiz clusterComputerRelationBiz;
    @Autowired
    private PermissionUtil permissionUtil;

    private Logger logger = LoggerFactory.getLogger(AgentServiceImpl.class);

    @Override
    public AgentInfo findAgentinfo(Long agentinfo) {
        AgentInfo info = new AgentInfo();
        Agent agent = agentBiz.getAgentByAgentId(agentinfo);
        BeanUtils.copyProperties(agent, info);
        return info;
    }

    @Override
    public void insertAgentEntity(Agent agent) {
        agent.setLastModifyTime(new Date());
        agentBiz.insertAgent(agent);
    }

    @Override
    public void updateAgentEntity(Agent agent) {
        agentBiz.updateAgent(agent);
    }

    @Override
    public void deleteAgentEntity(Long agentid) {
        agentBiz.deleteAgent(agentid);
    }

    @Override
    public String insAgent(AgentVo agentVo, Long userId, Long clusterId, String productCode) {
        Boolean flags = permissionUtil.haveActionRole(PermissionCode.AGENT_CONTROL, userId, productCode);
        if (flags) {
            // 获取用户可使用的机器
            List<Computer> computerList = computerBiz.getComputerList(userId);
            // 根据域ID获取域下的所有机器
            List<ComputerAndComputerSaltType> computerincluster = findAllComputersInClusterIfc.getComputerAndComputerTypeList(clusterId);
            List<MasterVo3> masterVo3List = new ArrayList<>();
            String masterIP = new String();
            String installFileLocation = new String();
            String masterPassword = new String();
            String masterUserName = new String();
            // 根据cluster下的所有机器遍历此用户的所有机器，找出master
            for (ComputerAndComputerSaltType computerAndComputerSaltType : computerincluster) {
                if (computerAndComputerSaltType.getMasterID() != null) {
                    for (Computer computer : computerList) {
                        if (computer.getComputerID().equals(computerAndComputerSaltType.getComputer().getComputerID())) {
                            MasterVo3 masterVo1 = new MasterVo3();
                            Long masterID = computerAndComputerSaltType.getMasterID();
                            masterVo1.setMasterID(masterID);
                            masterVo1.setMasterIP(NetTelnet.getConnectedIp(computer.getIp(), computer.getComputerType()));
                            masterVo1.setPassword(computer.getPassword());
                            masterVo1.setUserName(computer.getUserName());
                            masterVo3List.add(masterVo1);
                        }
                    }
                }
            }
            masterIP = checkMasterIP(masterVo3List, masterIP);
            installFileLocation = masterVo3List.get(0).getMasterIP();
            masterPassword = masterVo3List.get(0).getPassword();
            masterUserName = masterVo3List.get(0).getUserName();
//            FindCharacter findCharacter = new FindCharacter();
            int port = 22;
            String result = "";
//            boolean flag = false;
//            boolean flag2 = false;
//            boolean flag3 = false;
            boolean flag4 = false;
            boolean flag5 = false;
            boolean flag6 = false;
            //
            List<ComputerAndComputerSaltType> computerAndComputerSaltTypeList = agentVo.getComputerAndComputerSaltTypeList();
            // master列表
//            List<Map> masterList = agentVo.getMasterList();
//            // agent所在主机的用户名
//            String user = agentVo.getUsername();
//            // agent所在主机的密码
//            String password = agentVo.getPassword();
//            //这里有两个masterIP,因为一个域中有且只有的两个master
//            Long master1ID = 0L;
//            Long master2ID = 0L;
//            Long masterID1 = masterVo3List.get(0).getMasterID();
//            Long masterID2 = masterVo3List.get(1).getMasterID();
            //安装agent前，删除可能有的前agent
            String sh03 = "service zork-omsagent uninstall\n";
            String sh031 = "y\n";
            for (MasterVo3 masterVo3computer : masterVo3List) {
                flag5 = false;
                flag6 = false;
                // 根据master所在机器的IP获取master的Computer实体
                Computer mastercomputer = computerBiz.getComputerByComputerIP(masterVo3computer.getMasterIP());
                // 判断这个master需不需要安装agent且此master的账号密码需要存在此master所在computer上面
                String userNameofmaster = mastercomputer.getUserName();
                String passwardofmaster = mastercomputer.getPassword();
                // Master机器IP
                String tarhost = mastercomputer.getIp();
                // Master机器主机名
                String hostname = mastercomputer.getHostName();
                Agent agentComputer = null;
                // 根据机器ID获取Master所在机器的Agent实体
                agentComputer = agentBiz.selectAgentByComputerID(mastercomputer.getComputerID());
                // 如果master上无Agent
                if (agentComputer == null) {
                    // 目标文件名
                    String dst = "/";
                    //执行脚本命令
                    String sh3 = "cd ..\n";
                    String sh5 = "MASTER_IP=" + masterIP + "    DOWNLOAD_ADDR=" + mastercomputer.getIp() + ":8000" + "    " + " bash -c \"$(curl -s http://" + masterVo3List.get(0).getMasterIP() + ":8000/download/install_agent.sh)\"\n";
                    PipedInputStream pipedInputStream = null;
                    FileOutputStream fileOutputStream = null;
                    try {
                        JSch jsch = new JSch();
                        Session session = jsch.getSession(userNameofmaster, tarhost, port);
                        session.setConfig("StrictHostKeyChecking", "no");
                        session.setPassword(passwardofmaster);
                        try {
                            session.connect();
                        } catch (Exception e) {
                            return "账号或者密码错误";
                        }
                        ChannelShell channelShell = (ChannelShell) session.openChannel("shell");
                        pipedInputStream = new PipedInputStream();
                        PipedInputStream pipeIn = pipedInputStream;
                        PipedOutputStream pipeOut = new PipedOutputStream(pipeIn);
                        //日志输出文件名jschchannelshellmaster
                        fileOutputStream = new FileOutputStream("jschchannelshellmaster");
                        FileOutputStream fileOut = fileOutputStream;
                        channelShell.setInputStream(pipeIn);
                        channelShell.setOutputStream(fileOut);
                        channelShell.connect(10000);
                        //执行命令
                        pipeOut.write(sh03.getBytes());
                        Thread.sleep(1500);
                        pipeOut.write(sh031.getBytes());
                        Thread.sleep(3500);
                        pipeOut.write(sh3.getBytes());
                        Thread.sleep(1500);
                        pipeOut.write(sh5.getBytes());
                        Thread.sleep(35000);
                        pipeOut.write(sh031.getBytes());
                        Thread.sleep(500);
                        flag6 = true;
//                        int count2 = 0;
//                        while (true) {
//                            Thread.sleep(2000);
//                            if (flag6 == true || count2 == 20) {
//                                break;
//                            }
//                            ///disk01/center/smartoms-admin/release/admin/jschchannelshellagent linux生产目录下的路径
//                            //D:\\ideaworkspace\\smartoms-admin\\jschchannelshellagent windows下的路径
//                            flag6 = findCharacter.f(new File(fileConfiguration.getJschfilemaster()), "Install Agent done", flag5);
//                            count2++;
//                        }
                        pipeOut.close();
                        pipeIn.close();
                        fileOut.close();
                        channelShell.disconnect();
                        session.disconnect();
                    } catch (JSchException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        logger.error("睡眠中断", e);
                    } finally {
                        if (pipedInputStream != null) {
                            try {
                                pipedInputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if (flag6) {
                    // 如果master上的agnet安装成功
                    Agent agentinmaster1 = new Agent();
                    checkmasterBackID(masterVo3List, agentinmaster1);
                    //1表示正在运行 2表示未运行
                    agentinmaster1.setState(2);
                    agentinmaster1.setBackState(2);
                    agentinmaster1.setAgentName(mastercomputer.getIp());
                    agentinmaster1.setVersion("3.0");
                    agentinmaster1.setComputerID(mastercomputer.getComputerID());
                    //2表示手动安装 1表示自动安装
                    agentinmaster1.setInstallationType("1");
                    agentinmaster1.setLastModifyTime(new Date());
                    agentBiz.insertAgent(agentinmaster1);
                }
            }
            // 给裸机安装agent
            for (ComputerAndComputerSaltType computerAndComputerSaltType : computerAndComputerSaltTypeList) {
                flag4 = false;
                Computer computer = computerAndComputerSaltType.getComputer();
                // agent和cluster关系入库
                ClusterComputerRelation clusterComputerRelation = new ClusterComputerRelation();
                clusterComputerRelation.setClusterID(clusterId);
                clusterComputerRelation.setComputerID(computer.getComputerID());
                Agent agent1 = new Agent();
                agent1.setState(2);
                agent1.setBackState(2);
                agent1.setAgentName(computer.getIp());
                agent1.setVersion("3.0");
                agent1.setComputerID(computer.getComputerID());
                checkmasterBackID(masterVo3List, agent1);
                agent1.setInstallationType("1");
                agent1.setLastModifyTime(new Date());
                // 这里很难办，无法知道agent哪个ip是活的
                String tarhost = NetTelnet.getConnectedIp(computer.getIp(), computer.getComputerType());
                String hostname = computer.getHostName();
                //执行脚本命令
                String sh2 = "ssh -o StrictHostKeychecking=no root@" + tarhost + "\n";
                String sh3 = "cd ..\n";
                String sh3_1 = computer.getPassword() + "\n";
                String sh5 = "MASTER_IP=" + masterIP + "    DOWNLOAD_ADDR=" + masterVo3List.get(0).getMasterIP() + ":8000" + "  " + " bash -c \"$(curl -s http://" + masterVo3List.get(0).getMasterIP() + ":8000/download/install_agent.sh)\"\n";
                PipedInputStream pipedInputStream = null;
                FileOutputStream fileOutputStream = null;
                try {
                    JSch jsch = new JSch();
                    Session session = jsch.getSession(masterUserName, installFileLocation, port);
                    session.setConfig("StrictHostKeyChecking", "no");
                    session.setPassword(masterPassword);
                    session.connect();
                    ChannelShell channelShell = (ChannelShell) session.openChannel("shell");
                    pipedInputStream = new PipedInputStream();
                    PipedInputStream pipeIn = pipedInputStream;
                    PipedOutputStream pipeOut = new PipedOutputStream(pipeIn);
                    //日志输出文件名jschchannelshellagent
                    fileOutputStream = new FileOutputStream("jschchannelshellagent");
                    FileOutputStream fileOut = fileOutputStream;
                    channelShell.setInputStream(pipeIn);
                    channelShell.setOutputStream(fileOut);
                    channelShell.connect(10000);
                    //执行命令
                    pipeOut.write(sh2.getBytes());
                    Thread.sleep(1500);
                    pipeOut.write(sh3_1.getBytes());
                    Thread.sleep(1500);
//                    pipeOut.write(sh03.getBytes());
//                    Thread.sleep(2500);
//                    pipeOut.write(sh031.getBytes());
//                    Thread.sleep(3500);
                    pipeOut.write(sh3.getBytes());
                    Thread.sleep(1500);
                    pipeOut.write(sh5.getBytes());
                    Thread.sleep(35000);
                    pipeOut.write(sh031.getBytes());
                    Thread.sleep(500);
                    flag4 = true;
//                    int count = 0;
//                    while (true) {
//                        Thread.sleep(2000);
//                        if (flag2 == true || count == 20) {
//                            break;
//                        }
//                        ///disk01/center/smartoms-admin/release/admin/jschchannelshellagent linux生产目录下的路径
//                        flag2 = findCharacter.f(new File(fileConfiguration.getJschfile()), "100%  119MB", flag);
//                        count++;
//                    }
//                    pipeOut.write(sh4.getBytes());
//                    Thread.sleep(1500);
//                    pipeOut.write(sh5.getBytes());
//                    int count2 = 0;
//                    while (true) {
//                        Thread.sleep(2000);
//                        if (flag4 == true || count2 == 20) {
//                            break;
//                        }
//                        ///disk01/center/smartoms-admin/release/admin/jschchannelshellagent linux生产目录下的路径
//                        flag4 = findCharacter.f(new File(fileConfiguration.getJschfile()), "Install Agent done", flag3);
//                        count2++;
//                    }
                    pipeOut.close();
                    pipeIn.close();
                    fileOut.close();
                    channelShell.disconnect();
                    session.disconnect();
                } catch (JSchException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    logger.error("sleep 异常", e);
                } finally {
                    if (pipedInputStream != null) {
                        try {
                            pipedInputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (flag4) {
                    boolean flag4clustercomputer = true;
                    for (ComputerAndComputerSaltType computerAndComputerSaltType1 : computerincluster) {
                        if (computerAndComputerSaltType1.getComputer().getComputerID().equals(computer.getComputerID())) {
                            flag4clustercomputer = false;
                        }
                    }
                    if (flag4clustercomputer) {
                        clusterComputerRelationBiz.insertClusterComputerRelation(clusterComputerRelation);
                    }
                    agentBiz.insertAgent(agent1);
                }
            }
            if (flag4) {
                result = "1";
            } else {
                result = "0";
            }
            return result;
        }
        return "2";
    }

    private String checkMasterIP(List<MasterVo3> masterVo3List, String masterIP) {
        if (masterVo3List.size() == 1) {
            masterIP = masterVo3List.get(0).getMasterIP();
        } else if (masterVo3List.size() == 2) {
            masterIP = "[" + masterVo3List.get(0).getMasterIP() + "," + masterVo3List.get(1).getMasterIP() + "]";
        } else {
            logger.error("此域下的master个数大于2或者等于0");
        }
        return masterIP;
    }

    private String checkMasterIP1(List<Computer> computerList) {
        String masterIP = null;
        if (computerList.size() == 1) {
            masterIP = NetTelnet.getConnectedIp(computerList.get(0).getIp(), computerList.get(0).getComputerType());
        } else if (computerList.size() == 2) {
            masterIP = "[" + NetTelnet.getConnectedIp(computerList.get(0).getIp(), computerList.get(0).getComputerType()) + "," + NetTelnet.getConnectedIp(computerList.get(1).getIp(), computerList.get(1).getComputerType()) + "]";
        } else {
            logger.error("此域下的master个数大于2或者等于0");
        }
        return masterIP;
    }

    private void checkmasterBackID(List<MasterVo3> masterVo3List, Agent agent1) {
        if (masterVo3List.size() == 2) {
            agent1.setMasterID(masterVo3List.get(0).getMasterID());
            agent1.setMasterBackID(masterVo3List.get(1).getMasterID());
        } else if (masterVo3List.size() == 1) {
            agent1.setMasterID(masterVo3List.get(0).getMasterID());
        }
    }

    @Override
    public List<Long> handInsAgent(AgentVo agentVo, Long userId, Long clusterId) {
        //本接口暂时不用，功能合并到getcommand接口
        List<Long> agentIDList = new ArrayList<>();
        return agentIDList;
    }

    @Override
    public AgentCommandVo getCommand2(Long masterID, Long agentID, Long userId, Long clusterID) {
        List<Computer> computerList = masterBiz.getMasterComputerByClusterID(clusterID);
        String masterIP = checkMasterIP1(computerList);
        AgentCommandVo agentCommandVo = new AgentCommandVo();
        String command = "MASTER_IP=" + masterIP + "    DOWNLOAD_ADDR=" + computerList.get(0).getIp() + ":8000" + "  " + " bash -c \"$(curl -s http://" + computerList.get(0).getIp() + ":8000/download/install_agent.sh)\"\n";
        agentCommandVo.setCommandstr(command);
        agentCommandVo.setAgentIP(" ");
        return agentCommandVo;
    }

    @Override
    public List<Agent> getAllAgent() {
        return agentBiz.getAllAgent();
    }

    //TODO 做优化 蔡瑞翔 5.28
    @Override
    public List<AgentVo2> findAllAgent(Long userId) {
        List<ClusterMaster4AgentVo> clusterMaster4AgentVoList = agentBiz.getClusterMaster4Agent();
        List<Computer> computerList = computerBiz.getComputerList(userId);
        if (computerList == null) {
            List<AgentVo2> agentVo2List = new ArrayList<>();
            return agentVo2List;
        }
        List<AgentVo2> agentVo2List = new ArrayList<>();

        for (ClusterMaster4AgentVo clusterMaster4AgentVo : clusterMaster4AgentVoList) {
            Agent agent = clusterMaster4AgentVo.getAgent();
            if (agent == null) {
                agent = new Agent();
                //如果agent为null那之后这个agent所属的master cluster也不用判断了
                break;
            }
            Master master = clusterMaster4AgentVo.getMaster();
            if (master == null) {
                master = new Master();
            }
            Cluster cluster = clusterMaster4AgentVo.getCluster();
            if (cluster == null) {
                cluster = new Cluster();
            }
            Computer computer4master = new Computer();
            Computer computer4agent = new Computer();
            MasterVo2 masterVo2 = new MasterVo2();
            AgentVo2 agentVo2 = new AgentVo2();
            masterVo2.setCluster(cluster);
            masterVo2.setMaster(master);
            masterVo2.setComputer(computer4master);
            agentVo2.setComputer(computer4agent);
            for (Computer computer : computerList) {
                if (master != null && master.getComputerID() != null) {
                    if (computer.getComputerID().equals(master.getComputerID())) {
                        masterVo2.setComputer(computer);
                        break;
                    }
                }
            }
            for (Computer computer : computerList) {
                if (agent != null && agent.getComputerID() != null) {
                    if (computer.getComputerID().equals(agent.getComputerID())) {
                        agentVo2.setAgent(agent);
                        agentVo2.setComputer(computer);
                        agentVo2.setMasterVo2(masterVo2);
                        break;
                    }
                }
            }
            if (agentVo2.getAgent() != null && agentVo2.getComputer() != null && agentVo2.getMasterVo2() != null) {
                agentVo2List.add(agentVo2);
            }
        }
        return agentVo2List;
    }

    @Override
    public List<AgentStateSum> countAgentState(Long userId) {
        List<Computer> computerList = computerBiz.getComputerList(userId);
        if (computerList == null) {
            List<AgentStateSum> agentStateSum = new ArrayList<>();
            return agentStateSum;
        }
        Set<Long> computerIds = new HashSet<>();
        if (computerList == null) {
            return null;
        }
        for (Computer computer : computerList) {
            computerIds.add(computer.getComputerID());
        }
        List<AgentStateSum> agentStateSumList = agentBiz.countAgentState(computerIds);
        boolean state1 = false;
        boolean state2 = false;
        boolean state3 = false;
        boolean state4 = false;
        for (AgentStateSum agentStateSumL : agentStateSumList) {
            if (agentStateSumL.getState().equals("1")) {
                state1 = true;
            }
            if (agentStateSumL.getState().equals("2")) {
                state2 = true;
            }
            if (agentStateSumL.getState().equals("3")) {
                state3 = true;
            }
            if (agentStateSumL.getState().equals("4")) {
                state4 = true;
            }
        }
        if (state1 == false) {
            AgentStateSum agentStateSumstate1 = new AgentStateSum();
            agentStateSumstate1.setState("1");
            agentStateSumstate1.setCount("0");
            agentStateSumList.add(agentStateSumstate1);
        }
        if (state2 == false) {
            AgentStateSum agentStateSumstate1 = new AgentStateSum();
            agentStateSumstate1.setState("2");
            agentStateSumstate1.setCount("0");
            agentStateSumList.add(agentStateSumstate1);
        }
        if (state3 == false) {
            AgentStateSum agentStateSumstate1 = new AgentStateSum();
            agentStateSumstate1.setState("3");
            agentStateSumstate1.setCount("0");
            agentStateSumList.add(agentStateSumstate1);
        }
        if (state4 == false) {
            AgentStateSum agentStateSumstate1 = new AgentStateSum();
            agentStateSumstate1.setState("4");
            agentStateSumstate1.setCount("0");
            agentStateSumList.add(agentStateSumstate1);
        }
        return agentStateSumList;
    }

    @Override
    public List<AgentCommandVo> getCommand(AgentVo agentVo, Long userId, Long clusterId, String productCode) {
        Boolean flags = permissionUtil.haveActionRole(PermissionCode.AGENT_CONTROL, userId, productCode);
        if (flags) {
            List<ComputerAndComputerSaltType> computerAndComputerSaltTypeList = new ArrayList<>();
            List<ComputerAndComputerSaltType> saltTypeList = agentVo.getComputerAndComputerSaltTypeList();
            for (ComputerAndComputerSaltType cs : saltTypeList) {
                computerAndComputerSaltTypeList.add(cs);
            }
            List<ComputerAndComputerSaltType> computerAndComputerSaltTypeList2 = agentVo.getComputerAndComputerSaltTypeList();
//            ComputerAndComputerSaltType agent = computerAndComputerSaltTypeList2.get(0);
            List<ComputerAndComputerSaltType> mastercomputer = new ArrayList<>();
            List<Computer> computerList = computerBiz.getComputerList(userId);
            List<ComputerAndComputerSaltType> computerincluster = findAllComputersInClusterIfc.getComputerAndComputerTypeList(clusterId);
            List<MasterVo3> masterVo3List = new ArrayList<>();
            String masterIP = new String();
            for (ComputerAndComputerSaltType computerAndComputerSaltType : computerincluster) {
                //下面这句是判断出此cluster中的master,将其提出，进行操作
                if (computerAndComputerSaltType.getMasterID() != null) {
                    for (Computer computer : computerList) {
                        if (computer.getComputerID().equals(computerAndComputerSaltType.getComputer().getComputerID())) {
                            //最终应该是只有两个master
                            MasterVo3 masterVo1 = new MasterVo3();
                            Long masterID = computerAndComputerSaltType.getMasterID();
                            masterVo1.setMasterID(masterID);
                            masterVo1.setMasterIP(NetTelnet.getConnectedIp(computer.getIp(), computer.getComputerType()));
                            masterVo3List.add(masterVo1);
                            //添加到下面个列表用于生成安装命令
                            computerAndComputerSaltTypeList.add(computerAndComputerSaltType);
                            //添加到下面列表用于进行入库操作
                            mastercomputer.add(computerAndComputerSaltType);
                        }
                    }
                }
            }
            masterIP = checkMasterIP(masterVo3List, masterIP);
            String command = "MASTER_IP=" + masterIP + "    DOWNLOAD_ADDR=" + masterVo3List.get(0).getMasterIP() + ":8000" + "  " + " bash -c \"$(curl -s http://" + masterVo3List.get(0).getMasterIP() + ":8000/download/install_agent.sh)\"\n";
            List<AgentCommandVo> agentCommandVoList = new ArrayList<>();
            AgentCommandVo agentCommandVo = new AgentCommandVo();
            agentCommandVo.setAgentIP(" ");
            agentCommandVo.setCommandstr(command);
            agentCommandVoList.add(agentCommandVo);
            //命令生成完毕后，对master内的agent进行入库操作
//            Long masterID1 = masterVo3List.get(0).getMasterID();
//            Long masterID2 = masterVo3List.get(1).getMasterID();
            List<Long> agentIDList = new ArrayList<>();
            for (ComputerAndComputerSaltType computerAndComputerSaltType : mastercomputer) {
                //判断这个master需不需要安装agent且此master的账号密码需要存在此master所在computer上面
                if (computerAndComputerSaltType.getComputerSaltType().equals("1")) {
                    Agent agentinmaster1 = new Agent();
                    checkmasterBackID(masterVo3List, agentinmaster1);
                    //1表示正在运行 2表示未运行
                    agentinmaster1.setState(2);
                    agentinmaster1.setBackState(2);
                    agentinmaster1.setAgentName(computerAndComputerSaltType.getComputer().getIp());
                    agentinmaster1.setVersion("3.0");
                    agentinmaster1.setComputerID(computerAndComputerSaltType.getComputer().getComputerID());
                    //2表示手动安装 1表示自动安装
                    agentinmaster1.setInstallationType("2");
                    agentinmaster1.setLastModifyTime(new Date());
                    agentBiz.insertAgent(agentinmaster1);
                    agentIDList.add(agentinmaster1.getAgentID());
                }

            }
            //命令生成完毕后，对裸机进行agent入库操作，这里重新获取了一个裸机列表，因为前一个列表已经插入了master机器，同时将agent和cluster的关系入库
            for (ComputerAndComputerSaltType computerAndComputerSaltType : computerAndComputerSaltTypeList2) {
                Computer computer = computerAndComputerSaltType.getComputer();
                Agent agent1 = new Agent();
                agent1.setState(2);
                agent1.setBackState(2);
                agent1.setAgentName(computer.getIp());
                agent1.setVersion("3.0");
                agent1.setComputerID(computer.getComputerID());
                checkmasterBackID(masterVo3List, agent1);
                agent1.setInstallationType("1");
                agent1.setLastModifyTime(new Date());
                agentBiz.insertAgent(agent1);
                ClusterComputerRelation clusterComputerRelation = new ClusterComputerRelation();
                clusterComputerRelation.setClusterID(clusterId);
                clusterComputerRelation.setComputerID(computer.getComputerID());
                boolean flag4clustercomputer = true;
                for (ComputerAndComputerSaltType computerAndComputerSaltType1 : computerincluster) {
                    if (computerAndComputerSaltType1.getComputer().getComputerID().equals(computer.getComputerID())) {
                        flag4clustercomputer = false;
                    }
                }
                if (flag4clustercomputer) {
                    clusterComputerRelationBiz.insertClusterComputerRelation(clusterComputerRelation);
                }
            }
            return agentCommandVoList;
        }
        return null;
    }

    @Override
    public Integer getAgentStateByAgentName(String agentName) {
        return agentBiz.getAgentStateByAgentName(agentName);
    }
}
