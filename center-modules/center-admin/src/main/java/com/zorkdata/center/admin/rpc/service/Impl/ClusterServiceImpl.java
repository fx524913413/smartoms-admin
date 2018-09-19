package com.zorkdata.center.admin.rpc.service.Impl;

import com.zorkdata.center.admin.biz.*;
import com.zorkdata.center.admin.config.FileConfiguration;
import com.zorkdata.center.admin.entity.*;
import com.zorkdata.center.admin.rpc.service.Ifc.ClusterServiceIfc;
import com.zorkdata.center.admin.rpc.service.Ifc.ComputerServiceIfc;
import com.zorkdata.center.admin.rpc.service.Ifc.FindAllComputersInClusterIfc;
import com.zorkdata.center.admin.vo.ClusterInfo;
import com.zorkdata.center.admin.vo.ComputerAndComputerSaltType;
import com.zorkdata.center.admin.vo.Master4AgentDeploymentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/24 11:24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ClusterServiceImpl implements ClusterServiceIfc {
    public static final String REGEX = ",";
    @Autowired
    ClusterBiz clusterBiz;

    @Autowired
    ComputerBiz computerBiz;

    @Autowired
    MasterBiz masterBiz;

    @Autowired
    ClusterComputerRelationBiz clusterComputerRelationBiz;

    @Autowired
    AgentBiz agentBiz;

    @Autowired
    ComputerServiceIfc computerServiceIfc;

    @Autowired
    FindAllComputersInClusterIfc findAllComputersInClusterIfc;

    @Autowired
    FileConfiguration fileConfiguration;

    @Override
    public List<Cluster> selectList() {
        return clusterBiz.selectListAll();
    }

    @Override
    public Cluster selectOne(Long id) {
        return clusterBiz.selectById(id);
    }

    @Override
    public void insertSelective(Cluster cluser) {
        clusterBiz.insertSelective(cluser);
    }

    @Override
    public void updateSelective(Cluster cluster) {
        clusterBiz.updateById(cluster);
    }

    @Override
    public void deleteClusterById(Set<Long> ids) {
//        List<Master> masterList = masterBiz.getMasterList();
//        List<Computer> computerList = computerBiz.getComputerList();
//        for(Long masterID:ids){
//            for(Master master:masterList){
//                if(master.getMasterID().equals(masterID)){
//                    Long computerID = master.getComputerID();
//                    for(Computer computer:computerList){
//                        if(computer.getComputerID().equals(computerID)){
//                            try{
//                                String sh1 = "service zork-omsmaster uninstall\n";
//                                String sh0 ="yes\n";
//                                String sh2 = "service zork-omsagent uninstall\n";
//                                String sh3 = "service zork-omsapi uninstal\nl";
//                                JSch jsch = new JSch();
//                                String user = computer.getUserName();
//                                String tarip = computer.getIp();
//                                int port = 22;
//                                String password = computer.getPassword();
//                                Session session = jsch.getSession(user, tarip, port);
//                                session.setConfig("StrictHostKeyChecking", "no");
//                                session.setPassword(password);
//                                session.connect();
//                                ChannelShell channelShell = (ChannelShell)session.openChannel( "shell" );
//                                PipedInputStream pipeIn = new PipedInputStream();
//                                PipedOutputStream pipeOut = new PipedOutputStream( pipeIn );
//                                //日志输出文件名jschchannelshell
//                                FileOutputStream fileOut = new FileOutputStream( "jschchannelshell" );
//                                channelShell.setInputStream( pipeIn );
//                                channelShell.setOutputStream( fileOut );
//                                channelShell.connect( 10000 );
//                                //执行命令sh_0
//                                pipeOut.write( sh1.getBytes() );
//                                Thread.sleep(1500);
//                                pipeOut.write( sh0.getBytes() );
//                                Thread.sleep(1500);
//                                pipeOut.write( sh2.getBytes() );
//                                Thread.sleep(1500);
//                                pipeOut.write( sh0.getBytes() );
//                                Thread.sleep(1500);
//                                pipeOut.write( sh3.getBytes() );
//                                Thread.sleep(1500);
//                                pipeOut.write( sh0.getBytes() );
//                                Thread.sleep(1500);
//                                pipeOut.close();
//                                pipeIn.close();
//                                fileOut.close();
//                                channelShell.disconnect();
//                                session.disconnect();
//                            } catch (JSchException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
//            }
//        }
        clusterBiz.deleteByIds(ids);
    }

    @Override
    public List<Computer> getComputersByClusterID(Long clusterID) {
        return clusterBiz.getComputersByClusterID(clusterID);
    }

    @Override
    public void addComputer2Cluster(Long clusterID, String computers) {
        System.out.println(computers);
        if (computers != null) {
            for (String computerID : computers.split(REGEX)) {
                if (computerID != null && !computerID.equals("")) {
                    ClusterComputerRelation clusterComputerRelation = new ClusterComputerRelation();
                    clusterComputerRelation.setClusterID(clusterID);
                    clusterComputerRelation.setComputerID(Long.parseLong(computerID));
                    clusterComputerRelationBiz.insertSelective(clusterComputerRelation);
                }
            }
        }
    }

    /**
     * 获取首页所有cluster以及cluster内部此USERID能看到的所有机器信息
     *
     * @param userId
     * @return
     */
    @Override
    public List<ClusterInfo> getClusters(Long userId) {
        List<Computer> computerList = computerBiz.getComputerList(userId);
        List<Cluster> clusterList = clusterBiz.getClusterList();
        List<ClusterInfo> clusterList4User = new ArrayList<>();
        if (computerList == null) {
            for (Cluster cluster : clusterList) {
                ClusterInfo emptyClusterInfo = new ClusterInfo();
                emptyClusterInfo.setNumberOfDead(0);
                emptyClusterInfo.setNumberOfAlive(0);
                emptyClusterInfo.setClusterName(cluster.getClusterName());
                emptyClusterInfo.setClusterID(cluster.getClusterID());
                List<Master4AgentDeploymentVo> master4AgentDeploymentVoList = new ArrayList<>();
                emptyClusterInfo.setMasterlistOfCluster(master4AgentDeploymentVoList);
                clusterList4User.add(emptyClusterInfo);
            }
            return clusterList4User;
        }
        Set<Long> computerIds = new HashSet<>();
        for (Computer computer : computerList) {
            Long computerID = computer.getComputerID();
            computerIds.add(computerID);
        }
        List<ClusterInfo> clusterInfoList = clusterBiz.getMasterAndAgentInfoInCluster(computerIds);
        for (Cluster cluster : clusterList) {
            boolean flag = false;
            for (ClusterInfo clusterInfo : clusterInfoList) {
                if (clusterInfo.getClusterID().equals(cluster.getClusterID())) {
                    flag = true;
                    break;
                }
            }
            if (flag == false) {
                ClusterInfo emptyClusterInfo = new ClusterInfo();
                emptyClusterInfo.setNumberOfDead(0);
                emptyClusterInfo.setNumberOfAlive(0);
                emptyClusterInfo.setClusterName(cluster.getClusterName());
                emptyClusterInfo.setClusterID(cluster.getClusterID());
                List<Master4AgentDeploymentVo> master4AgentDeploymentVoList = new ArrayList<>();
                emptyClusterInfo.setMasterlistOfCluster(master4AgentDeploymentVoList);
                clusterInfoList.add(emptyClusterInfo);
            }
        }

        return clusterInfoList;
    }
}
