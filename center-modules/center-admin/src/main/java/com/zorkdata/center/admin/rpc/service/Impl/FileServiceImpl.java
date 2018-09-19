package com.zorkdata.center.admin.rpc.service.Impl;

import com.netflix.discovery.converters.Auto;
import com.zorkdata.center.admin.agent.core.SaltApi;
import com.zorkdata.center.admin.biz.AgentBiz;
import com.zorkdata.center.admin.biz.JobBiz;
import com.zorkdata.center.admin.biz.SaltJobBiz;
import com.zorkdata.center.admin.biz.SaltJobRetBiz;
import com.zorkdata.center.admin.config.FileConfiguration;
import com.zorkdata.center.admin.config.GitConfigration;
import com.zorkdata.center.admin.entity.*;
import com.zorkdata.center.admin.rpc.service.Ifc.FileServiceIfc;
import com.zorkdata.center.admin.util.CommonEnum;
import com.zorkdata.center.admin.util.GitUtil;
import com.zorkdata.center.admin.util.ResultInfo;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.salt.netapi.exception.SaltException;
import com.zorkdata.center.common.util.NetTelnet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/27 17:04
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FileServiceImpl implements FileServiceIfc {
    public static final String ZORK_OMSAPI = "zork-omsapi";
    public static final String ZORKDATA_8888 = "zorkdata.8888";
    public static final String REGEX = ":";
    @Autowired
    private GitConfigration gitConfigration;

    @Autowired
    private FileConfiguration fileConfiguration;

    @Autowired
    AgentBiz agentBiz;

    @Autowired
    private SaltJobBiz saltJobBiz;

    @Autowired
    private SaltJobRetBiz saltJobRetBiz;

    @Autowired
    private JobBiz jobBiz;

    @Autowired
    private GitUtil gitUtil;

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public void downloadFile(HttpServletRequest request, HttpServletResponse response, HttpSession session, String filename) throws Exception {
//        String path=request.getServletContext().getRealPath("/");
        String filefullName = fileConfiguration.getFilepath() + File.separator + filename;
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + filename);
        ServletOutputStream out;
        // 通过文件路径获得File对象(假如此路径中有一个download.pdf文件)
        File file = new File(filefullName);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            FileInputStream inputStream = fileInputStream;
            // 3.通过response获取ServletOutputStream对象(out)
            out = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            inputStream.close();
            out.flush();
            out.close();
        } catch (IOException e) {
			e.printStackTrace();
        }finally {
            if(fileInputStream!=null){
                fileInputStream.close();
            }
        }
    }

    @Override
    public Map<Long, Map<String, List<String>>> asyncSendFile(Map<String, Object> map) {
        try {
            List<String> minions = (List<String>) map.get("minions");
            String scrPath = String.valueOf(map.get("scrPath"));
            String destPath = String.valueOf(map.get("destPath"));
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
                        Map<String, List<String>> ret = saltApi.asyncSendFile(scrPath, destPath, agents);
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
                        logger.error("线程中断异常",e);
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


    @Override
    public Map<String, String> syncSendFile(Map<String, Object> map) {
        System.out.println("syncsendfile");
        List<String> minions = (List<String>) map.get("minions");
        String scrPath = String.valueOf(map.get("scrPath"));
        String destPath = String.valueOf(map.get("destPath"));
        List<Agent2Master> list = agentBiz.seleteAgent2Master(minions);
        Map<String, String> result = new HashMap<>();
        List<String> agentNameList = new ArrayList<>();
        try {
            if (list != null && list.size() > 0) {
                // jobid -> saltjobid ->agent ret
                Job job = new Job();
                job.setCreatetime(new Date());
                jobBiz.insertJob(job);
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
                        for(String agent:agents){
                            if(!agentNameList.contains(agent)&&minions.contains(agent)){
                                String destPathTure = new String();
                                List<String> agents4beats = new ArrayList<>();
                                agents4beats.add(agent);
                                destPathTure = saltApi.getAgentPath(agent,destPath);
                                result.putAll(saltApi.sendFile(scrPath, destPathTure, agents4beats));
                                agentNameList.add(agent);
                            }
                        }
//                        result.putAll(saltApi.sendFile(scrPath, destPath, agents));
                    }catch (InterruptedException e) {
                        logger.error("线程中断异常",e);
                    }catch (SaltException e) {
                        e.printStackTrace();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, String> syncSendFile4AnyLocation(Map<String, Object> map) {
        List<String> minions = (List<String>) map.get("minions");
        String scrPath = String.valueOf(map.get("scrPath"));
        String destPath = String.valueOf(map.get("destPath"));
        List<Agent2Master> list = agentBiz.seleteAgent2Master(minions);
        Map<String, String> result = new HashMap<>();
        List<String> agentNameList = new ArrayList<>();
        try {
            if (list != null && list.size() > 0) {
                // jobid -> saltjobid ->agent ret
                Job job = new Job();
                job.setCreatetime(new Date());
                jobBiz.insertJob(job);
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
                        for(String agent:agents){
                            if(!agentNameList.contains(agent)){
                                String destPathTure = new String();
                                List<String> agents4beats = new ArrayList<>();
                                agents4beats.add(agent);
//                                destPathTure = saltApi.getAgentPath(agent,destPath);
                                result.putAll(saltApi.sendFile(scrPath, destPath, agents4beats));
                                agentNameList.add(agent);
                            }
                        }
                    }catch (InterruptedException e) {
                        logger.error("线程中断异常",e);
                    }catch (SaltException e) {
                        e.printStackTrace();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultInfo fastFileUpload(MultipartFile file, String isOverride, String name, String folder) {
        ResultInfo resultInfo = new ResultInfo();
        Map<String, String> result = new HashMap<>(16);
        String fileName = file.getOriginalFilename();
        String dirPath = gitConfigration.getLocalRepositoryDir() + File.separator + folder+File.separator;
        File file1 = new File(dirPath);
        if (!file1.exists()){
            boolean isMkdir = file1.mkdirs();
        }
        if (CommonEnum.RENAME.getValue().equals(isOverride)){
            fileName = name;
        }
        File targetFile = new File(dirPath + fileName);
        Boolean isTransferTo = transferTo(file, targetFile);
        if (!isTransferTo){
            resultInfo.setCode(CodeTable.FAILED);
            resultInfo.setMsg("写入文件到本地失败");
            return resultInfo;
        }

        String fileUploadResult = gitUtil.commitPullAndPush();
        if (CommonEnum.SUCCESS.getValue().equals(fileUploadResult)) {
            DecimalFormat df = new DecimalFormat("######0.00");
            String fileSize = df.format((double) targetFile.length() / 1024.00) + "KB";
            resultInfo.setCode(CodeTable.SUCCESS);
            FileDir fileDir = new FileDir();
            fileDir.setName(fileName);
            fileDir.setSaltPath(fileConfiguration.getSaltURL()+"fastFile/"+folder+"/"+fileName);
            resultInfo.setData(fileDir);
            Map<String, String> addition = new HashMap<>(16);
            addition.put("execAccount", "无");
            addition.put("isOverride", isOverride);
            addition.put("serverAddress", "本地文件" + "【" + fileSize + "】");
            resultInfo.setAddition(addition);
        }else {
            //如果上传失败就删除本地文件
            targetFile.delete();
            resultInfo.setMsg(fileUploadResult);
            resultInfo.setCode(CodeTable.FAILED);
        }
        return resultInfo;
    }

    @Override
    public Boolean transferTo(MultipartFile sourFile, File tarFile) {

        try {
            sourFile.transferTo(tarFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

//    public RespModel ymlUpload(String typeName, InputStream fis, String agentName) throws IOException {
//        RespModel retData = null;
//        boolean flag = false;
//        System.out.println("接收到agentName" + agentName);
//        if (fis == null) { //判断文件是否存在
//            return RespTools.getRespMsgModel(CodeTable.UNKNOWN_ERROR, "未接受到文件");
//        }
//        MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
//        try {
//            Boolean ret = upYml(typeName, fis, agentName);
//            if (!ret) {
//                return RespTools.getRespMsgModel(CodeTable.UNKNOWN_ERROR, "上传配置文件失败！");
//            }
//            flag = sendftp(typeName, fis, agentName);
//            if (!flag) {
//                return RespTools.getRespMsgModel(CodeTable.UNKNOWN_ERROR, "下发配置文件失败！");
//            }
//            Integer agentState = agentBiz.getAgentStateByAgentName(agentName);
//            if (agentState == null) {
//                return RespTools.getRespMsgModel(CodeTable.UNKNOWN_ERROR, String.format("WorkNode获取异常,请检查worknodeid是否正确!"));
//            }
//            if (!agentState.equals(1)) {
//                return RespTools.getRespMsgModel(CodeTable.UNKNOWN_ERROR, String.format("节点%s离线或状态异常", agentName));
//            }
//            //LogHelper.info(String.format("调用节点%s在线", agentName), true);
//            flag = stopScript(typeName, agentName);
//            if (!flag) {
//                return RespTools.getRespMsgModel(CodeTable.UNKNOWN_ERROR, String.format("节点%s: 关闭失败", agentName));
//            }
//            flag = startScript(typeName, agentName);
//            if (!flag) {
//                return RespTools.getRespMsgModel(CodeTable.UNKNOWN_ERROR, String.format("节点%s: 启动失败", agentName));
//            }
//            retData = RespTools.getRespMsgModel(CodeTable.SUCCESS, String.format("节点%s: 启动%s", agentName, flag));
//        } catch (Exception e) {
//            retData = RespTools.getRespMsgModel(CodeTable.UNKNOWN_ERROR, String.format("Error:%s", e.getMessage()));
//        }
//        return retData;
//    }
}
