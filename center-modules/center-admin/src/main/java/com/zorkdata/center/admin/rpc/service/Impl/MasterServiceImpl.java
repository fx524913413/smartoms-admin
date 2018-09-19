package com.zorkdata.center.admin.rpc.service.Impl;

import com.jcraft.jsch.*;
import com.zorkdata.center.admin.biz.ClusterBiz;
import com.zorkdata.center.admin.biz.ClusterComputerRelationBiz;
import com.zorkdata.center.admin.biz.ComputerBiz;
import com.zorkdata.center.admin.biz.MasterBiz;
import com.zorkdata.center.admin.config.FileConfiguration;
import com.zorkdata.center.admin.entity.*;
import com.zorkdata.center.admin.rpc.service.Ifc.FindAllComputersInClusterIfc;
import com.zorkdata.center.admin.rpc.service.Ifc.MasterServiceIfc;
import com.zorkdata.center.admin.util.PermissionCode;
import com.zorkdata.center.admin.util.PermissionUtil;
import com.zorkdata.center.admin.vo.ComputerAndComputerSaltType;
import com.zorkdata.center.admin.vo.MasterInfo;
import com.zorkdata.center.admin.vo.MasterVo;
import com.zorkdata.center.admin.vo.MasterVo2;
import com.zorkdata.center.common.util.FileProgressMonitor;
import com.zorkdata.center.common.util.FindCharacter;
import com.zorkdata.center.common.util.NetTelnet;
import com.zorkdata.center.common.util.SftpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MasterServiceImpl implements MasterServiceIfc {
    @Autowired
    private MasterBiz masterBiz;
    @Autowired
    private FileConfiguration fileConfiguration;
    @Autowired
    private ClusterBiz clusterBiz;
    @Autowired
    private ComputerBiz computerBiz;
    @Autowired
    private ClusterComputerRelationBiz clusterComputerRelationBiz;
    @Autowired
    private FindAllComputersInClusterIfc findAllComputersInClusterIfc;
    @Autowired
    PermissionUtil permissionUtil;

    private Logger logger = LoggerFactory.getLogger(MasterServiceImpl.class);
    @Override
    public MasterInfo findMasterinfo(Long masterinfo) {
        MasterInfo info = new MasterInfo();
        Master master = masterBiz.getMasterByMasterId(masterinfo);
        BeanUtils.copyProperties(master, info);
        return info;
    }

    @Override
    public void insertMasterEntity(Master master) {
        masterBiz.insertMaster(master);
    }

    @Override
    public void updateMasterEntity(Master master) {
        masterBiz.updateMaster(master);
    }

    @Override
    public void deleteMasterEntity(Long masterid) {
        masterBiz.deleteMaster(masterid);
    }

    @Override
    public Boolean insMaster(MasterVo masterVo, Long clusterID, Long userID, String productCode) {
        Boolean flags = permissionUtil.haveActionRole(PermissionCode.MASTER_CONTROL, userID, productCode);
        if (flags) {
            FindCharacter findCharacter = new FindCharacter();
            boolean havemaster = false;
            String Ismasterip = new String();
            String result = "";
            List<ComputerAndComputerSaltType> computerIncluster = findAllComputersInClusterIfc.getComputerAndComputerTypeList(clusterID);
            //之前安装的master机器作为主master
            if (computerIncluster != null) {
                for (ComputerAndComputerSaltType computerAndComputerSaltType : computerIncluster) {
                    if (computerAndComputerSaltType.getComputerSaltType().equals("1") || computerAndComputerSaltType.getComputerSaltType().equals("3")) {
                        havemaster = true;
                        Ismasterip = NetTelnet.getConnectedIp(computerAndComputerSaltType.getComputer().getIp(), computerAndComputerSaltType.getComputer().getComputerType());
                    }
                }
            }
            List<Computer> computerList = masterVo.getComputerList();
            int port = masterVo.getPort();
            String password = masterVo.getPassword();
            String user = masterVo.getUsername();
            for (Computer computer : computerList) {
                boolean flag1 = false;
                boolean flag2 = false;
                boolean flag3 = false;
                boolean flag4 = false;
                //这个flag用于指定是否此master需要和主master进行私钥同步
                boolean flag = false;
                ClusterComputerRelation clusterComputerRelation = new ClusterComputerRelation();
                clusterComputerRelation.setClusterID(clusterID);
                clusterComputerRelation.setComputerID(computer.getComputerID());
                Master master = new Master();
                master.setClusterID(clusterID);
                master.setState(2);
                master.setComputerID(computer.getComputerID());
                master.setPort(port);
                master.setInstallationType("1");
                master.setLastModifyTime(new Date());
                master.setApiPort(8000);
                //此域中是否已经有master,如果没有，则第一个在此域入库的master为主master
                if (havemaster) {
                    master.setIsMaster(0);
                    flag = true;
                } else {
                    master.setIsMaster(1);
                    havemaster = true;
                    Ismasterip = NetTelnet.getConnectedIp(computer.getIp(), computer.getComputerType());
                }
                String tarhost = NetTelnet.getConnectedIp(computer.getIp(), computer.getComputerType());
                //不使用数据库中computer中的账号密码 因为用户可能不给
                SftpUtil sftpminon = new SftpUtil(tarhost, port, user, password);
                if (sftpminon == null) {
                    return null;
                }
                ChannelSftp channelSftp = sftpminon.getSftp();
                String tarfoldername = "zrok";
                // 本地文件名install_master.sh用于在此机器上安装master install_agent.sh zork-omsagent-linux64.zip用于到时候Agent机器向master机器索取安装所需的文件
                String src0 = fileConfiguration.getFilepath() + File.separator + "zork-omsagent-linux-x86_64.zip";
                String src1 = fileConfiguration.getFilepath() + File.separator + "install_agent.sh";
                String src2 = fileConfiguration.getFilepath() + File.separator + "install_master.sh";
                String src3 = fileConfiguration.getFilepath() + File.separator + "install_agent4master.sh";
                // agent安装文件放置位置
                String dst = "/zork";
                // master安装脚本放置位置
                String dst4master = "/";
                //执行脚本命令
                String sh0 = "cd ..\n";
                String sh01 = "service zork-omsmaster uninstall\n";
                String sh011 = "y\n";
                String sh02 = "service zork-omsapi uninstall\n";
                String sh021 = "y\n";
                String sh03 = "mkdir /zork/\n";
                String sh1 = "chmod 755 install_master.sh\n";
                String sh2 = fileConfiguration.getShcommand() + "\n";
                File file0 = new File(src0);
                long fileSize0 = file0.length();
                File file1 = new File(src1);
                long fileSize1 = file1.length();
                File file2 = new File(src2);
                long fileSize2 = file2.length();
                File file3 = new File(src3);
                long fileSize3 = file3.length();
                //先把master安装脚本传送过去用于master安装
                try {
                    channelSftp.put(src2, dst4master, new FileProgressMonitor(fileSize2), ChannelSftp.OVERWRITE);
                } catch (SftpException e) {
                    e.printStackTrace();
                }
                PipedInputStream pipedInputStream = null;
                FileOutputStream fileOutputStream = null;
                try {
                    JSch jsch = new JSch();
                    Session session = jsch.getSession(user, tarhost, port);
                    session.setConfig("StrictHostKeyChecking", "no");
                    session.setPassword(password);
                    session.connect();
                    ChannelShell channelShell = (ChannelShell) session.openChannel("shell");
                    pipedInputStream = new PipedInputStream();
                    PipedInputStream pipeIn = pipedInputStream;
                    PipedOutputStream pipeOut = new PipedOutputStream(pipeIn);
                    //日志输出文件名jschchannelshell
                    fileOutputStream = new FileOutputStream("jschchannelshell");
                    FileOutputStream fileOut = fileOutputStream;
                    channelShell.setInputStream(pipeIn);
                    channelShell.setOutputStream(fileOut);
                    channelShell.connect(10000);
                    //执行命令sh_0
                    pipeOut.write(sh03.getBytes());
                    Thread.sleep(500);
                    pipeOut.write(sh01.getBytes());
                    Thread.sleep(1500);
                    pipeOut.write(sh011.getBytes());
                    Thread.sleep(1500);
                    pipeOut.write(sh02.getBytes());
                    Thread.sleep(1500);
                    pipeOut.write(sh021.getBytes());
                    Thread.sleep(1500);
                    pipeOut.write(sh0.getBytes());
                    Thread.sleep(500);
                    pipeOut.write(sh1.getBytes());
                    Thread.sleep(500);
                    pipeOut.write(sh2.getBytes());
                    Thread.sleep(20000);
                    flag4=true;
                    flag1=true;
                    //检测功能暂时有点问题 6.12
//                    int count = 0;
//                    int count2 = 0;
//                    while (true) {
//                        Thread.sleep(2000);
//                        if (flag4 == true || count == 20) {
//                            break;
//                        }
//                        ///disk01/center/smartoms-admin/release/admin/jschchannelshellagent linux生产目录下的路径
//                        flag4 = findCharacter.f(new File(fileConfiguration.getJschfile2()), "Install master done", flag3);
//                        count++;
//                    }
//                    while (true) {
//                        Thread.sleep(2000);
//                        if (flag1 == true || count2 == 20) {
//                            break;
//                        }
//                        ///disk01/center/smartoms-admin/release/admin/jschchannelshellagent linux生产目录下的路径
//                        flag1 = findCharacter.f(new File(fileConfiguration.getJschfile2()), "Install api done", flag2);
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
                    logger.error("睡眠中断", e);
                }finally {
                    if(pipedInputStream!=null){
                        try {
                            pipedInputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(fileOutputStream!=null){
                        try {
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //从主机传送agent安装文件、脚本 master安装文件直接在安装命令中通过连接获取
                try {
                    channelSftp.put(src0, dst, new FileProgressMonitor(fileSize0), ChannelSftp.OVERWRITE);
                    channelSftp.put(src1, dst, new FileProgressMonitor(fileSize1), ChannelSftp.OVERWRITE);
                    channelSftp.put(src3, dst, new FileProgressMonitor(fileSize3), ChannelSftp.OVERWRITE);
                } catch (SftpException e) {
                    e.printStackTrace();
                }
                if (flag4 == true && flag1 == true) {
                    boolean flag2inscluster = true;
                    masterBiz.insertMaster(master);
                    for (ComputerAndComputerSaltType computerAndComputerSaltType : computerIncluster) {
                        if (computerAndComputerSaltType.getComputer().getComputerID().equals(computer.getComputerID())) {
                            flag2inscluster = false;
                        }
                    }
                    if (flag2inscluster) {
                        clusterComputerRelationBiz.insertClusterComputerRelation(clusterComputerRelation);
                    }
                    //判断是否有主msater如果有，则进行主备私钥同步
                    if (flag) {

                        System.out.println(synPrivateKey(masterVo, Ismasterip, NetTelnet.getConnectedIp(computer.getIp(), computer.getComputerType())));
                    }
                    result = "1";
                } else if (flag4 == true && flag1 != true) {
                    //api安装失败
                    result = "2";
                } else if (flag4 != true && flag1 == true) {
                    //master安装失败
                    result = "3";
                } else {
                    //api master 安装都失败
                    result = "4";
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public List<Long> batchInsMas(MasterVo masterVo, Long clusterID, Long userID, String productCode) {
        Boolean flags = permissionUtil.haveActionRole(PermissionCode.MASTER_CONTROL, userID, productCode);
        if (flags) {
            boolean havemaster = false;
            List<Computer> computerList = masterVo.getComputerList();
            int port = masterVo.getPort();
            List<Long> masterIDlist = new ArrayList<>();
            List<ComputerAndComputerSaltType> computerIncluster = findAllComputersInClusterIfc.getComputerAndComputerTypeList(clusterID);
            for(ComputerAndComputerSaltType computerAndComputerSaltType:computerIncluster){
                if(computerAndComputerSaltType.getComputerSaltType().equals("1")||computerAndComputerSaltType.getComputerSaltType().equals("3")){
                    havemaster = true;
                }
            }
            for (Computer computer : computerList) {
                boolean flag = false;
                ClusterComputerRelation clusterComputerRelation = new ClusterComputerRelation();
                clusterComputerRelation.setClusterID(clusterID);
                clusterComputerRelation.setComputerID(computer.getComputerID());
                Master master1 = masterBiz.selectMasterByComputerID(computer.getComputerID());
                if (master1 != null) {
                    return null;
                }
                Master master = new Master();
                master.setClusterID(clusterID);
                //1表示正在运行 2表示未运行
                master.setState(2);
                master.setComputerID(computer.getComputerID());
                master.setPort(port);
                master.setLastModifyTime(new Date());
                master.setApiPort(8000);
                //2表示手动安装 1表示自动安装
                master.setInstallationType("2");
                //此域中是否已经有master,如果没有，则第一个在此域入库的master为主master
                if (havemaster) {
                    master.setIsMaster(0);
                    flag = true;
                } else {
                    master.setIsMaster(1);
                    havemaster = true;
                }
                masterBiz.insertMaster(master);
                boolean flag2inscluster = true;
                for (ComputerAndComputerSaltType computerAndComputerSaltType : computerIncluster) {
                    if (computerAndComputerSaltType.getComputer().getComputerID().equals(computer.getComputerID())) {
                        flag2inscluster = false;
                    }
                }
                if (flag2inscluster) {
                    clusterComputerRelationBiz.insertClusterComputerRelation(clusterComputerRelation);
                }
                Long masterID = master.getMasterID();
                masterIDlist.add(masterID);
            }
            return masterIDlist;
        }
        return null;
    }

    @Override
    public void changeMasterState(Long masterid, Integer state) {
        masterBiz.updataMasterState(masterid, state);
    }

//    @Override
//    public List<MasterVo2> findAllMaster(Long userId){
//        List<MasterVo2> masterVo2List = new ArrayList<>();
//        List<Master> masterList = masterBiz.getMasterList();
//        List<Cluster> clusterList = clusterBiz.getClusterList();
//        List<Computer> computerList = computerBiz.getComputerList(userId);
//        for(Computer computer:computerList){
//            for(Master master:masterList){
//                if(computer.getComputerID().equals(master.getComputerID())){
//                    MasterVo2 masterVo2 = new MasterVo2();
//                    masterVo2.setComputer(computer);
//                    masterVo2.setMaster(master);
//                    for(Cluster clusterinlist:clusterList){
//                        if(clusterinlist.getClusterID().equals(master.getClusterID())){
//                            masterVo2.setCluster(clusterinlist);
//                        }
//                    }
//                    masterVo2List.add(masterVo2);
//                }
//            }
//        }
//        return masterVo2List;
//    }

    @Override
    public List<MasterVo2> findAllMaster(Long userId) {
        List<MasterVo2> masterVo2List = new ArrayList<>();
        masterVo2List = masterBiz.getMasterVo2();
        List<Computer> computerList = computerBiz.getComputerList(userId);
        if (computerList == null) {
            return null;
        }
        List<MasterVo2> computerOfMasterList = new ArrayList<>();
        for (MasterVo2 masterVo2 : masterVo2List) {
            if (masterVo2 != null) {
                for (Computer computer : computerList) {
                    if (computer.getComputerID().equals(masterVo2.getComputer().getComputerID())) {
                        computerOfMasterList.add(masterVo2);
                        masterVo2.setComputer(computer);
                        break;
                    }
                }
            }
        }
        return computerOfMasterList;
    }


    @Override
    public String getCommand() {
        String command = fileConfiguration.getShcommand();
        return command;
    }

    @Override
    public void uninstallMaster(MasterVo masterVo) {

    }

    public String synPrivateKey(MasterVo masterVo, String masterip, String assistip) {
        List<Computer> computerList = masterVo.getComputerList();
        String password = masterVo.getPassword();
        String user = masterVo.getUsername();
        String master_ip = masterip;
        String assist_ip = assistip;
        int port = 22;
        //for(Computer computer:computerList){
        //先找到主master，获取主master相关信息
        // if(computer.getComputerID().equals("1")){
//        master_ip="192.168.1.93";
        //computerList.get(0).getIp();

        // }
        //}
//        assist_ip="192.168.1.94";
//        SftpUtil sftpminon = new SftpUtil(assist_ip, port, user, password);
//        ChannelSftp channelSftp = sftpminon.getSftp();
//        if (sftpminon == null) {
//            return "账号或者密码错误";
//        }
        String sh2 = "ssh -o StrictHostKeychecking=no root@" + assist_ip + "\n";
        String sh3_1 = password + "\n";
        String sh4 = "scp -o StrictHostKeychecking=no  root@" + master_ip + ":/zork/zork-omsmaster/var/pki/master.pem /zork/zork-omsmaster/var/pki" + "\n";
        String sh5 = "scp -o StrictHostKeychecking=no  root@" + master_ip + ":/zork/zork-omsmaster/var/pki/master.pub /zork/zork-omsmaster/var/pki" + "\n";
        String sh6 = "service zork-omsmaster restart\n";
        String sh7 = "service zork-omsapi restart\n";
        PipedInputStream pipedInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, assist_ip, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect();
            pipedInputStream = new PipedInputStream();
            PipedInputStream pipeIn = pipedInputStream;
            PipedOutputStream pipeOut = new PipedOutputStream(pipeIn);
            ChannelShell channelShell = (ChannelShell) session.openChannel("shell");
            fileOutputStream = new FileOutputStream("replacemasterconfigfile");
            FileOutputStream fileOut = fileOutputStream;
            channelShell.setInputStream(pipeIn);
            channelShell.setOutputStream(fileOut);
            channelShell.connect(10000);
            pipeOut.write(sh2.getBytes());
            Thread.sleep(2500);
            pipeOut.write(sh3_1.getBytes());
            Thread.sleep(1500);
            pipeOut.write(sh4.getBytes());
            Thread.sleep(1500);
            pipeOut.write(sh3_1.getBytes());
            Thread.sleep(5000);
            pipeOut.write(sh5.getBytes());
            Thread.sleep(1500);
            pipeOut.write(sh3_1.getBytes());
            Thread.sleep(5000);
            pipeOut.write(sh6.getBytes());
            Thread.sleep(5500);
            pipeOut.write(sh7.getBytes());
            Thread.sleep(3500);
            pipeOut.close();
            pipeIn.close();
            channelShell.disconnect();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
            return "同步失败";
        } catch (IOException e) {
            e.printStackTrace();
            return "同步失败";
        } catch (InterruptedException e) {
            logger.error("睡眠中断", e);
            return "同步失败";
        }finally {
            if(pipedInputStream!=null){
                try {
                    pipedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return "同步完成";
    }
}
