package com.zorkdata.center.admin.rpc.service.Impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.zorkdata.center.admin.agent.core.SaltApi;
import com.zorkdata.center.admin.biz.AgentBiz;
import com.zorkdata.center.admin.biz.MasterBiz;
import com.zorkdata.center.admin.config.FileConfiguration;
import com.zorkdata.center.admin.config.GitConfigration;
import com.zorkdata.center.admin.entity.FileDir;
import com.zorkdata.center.admin.rpc.service.Ifc.BeatServiceIfc;
import com.zorkdata.center.admin.rpc.service.Ifc.FileServiceIfc;
import com.zorkdata.center.admin.util.CommonEnum;
import com.zorkdata.center.admin.util.GitProperties;
import com.zorkdata.center.admin.util.GitUtil;
import com.zorkdata.center.admin.util.ResultInfo;
import com.zorkdata.center.admin.vo.ComputerVo;
import com.zorkdata.center.admin.vo.MasterInfoVo;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import com.zorkdata.center.common.salt.netapi.calls.modules.State;
import com.zorkdata.center.common.salt.netapi.results.Result;
import com.zorkdata.center.common.util.NetTelnet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.text.DecimalFormat;
import java.util.*;


/**
 * =
 *
 * @author: huziyue
 * @create: 2018/6/8 14:40
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BeatServiceImpl implements BeatServiceIfc {
    private static final Logger logger = LoggerFactory.getLogger(BeatServiceImpl.class);
    public static final String ZORK_OMSAPI = "zork-omsapi";
    public static final String ZORKDATA_8888 = "zorkdata.8888";
    @Autowired
    private AgentBiz agentBiz;
    @Autowired
    private MasterBiz masterBiz;
    @Autowired
    private FileConfiguration fileConfiguration;
    @Autowired
    private FileServiceIfc fileServiceIfc;
    @Autowired
    private GitUtil gitUtil;
    @Autowired
    private GitConfigration gitConfigration;

    @Override
    public RespModel stopService(String typeName, String agentName) {
        // 获取Agent在线状态,返回在线的节点号
        Integer agentState = agentBiz.getAgentStateByAgentName(agentName);
        if (agentState == null) {
            return RespTools.getRespMsgModel(CodeTable.UNKNOWN_ERROR, String.format("agent获取异常,请检查agentName是否正确!"));
        }
        if (!agentState.equals(1)) {
            return RespTools.getRespMsgModel(CodeTable.UNKNOWN_ERROR, String.format("节点%s离线或状态异常", agentName));
        }
        Boolean flag = stopScript(typeName, agentName);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, flag);
    }

    /***
     * 调用Agent的脚本执行器
     *
     * @param agentName agent名
     * @param typeName 脚本名
     * @return
     */
    public boolean startScript(String typeName, String agentName) {
        boolean stateData = false;
        try {
            MasterInfoVo master=masterBiz.getMasterIPAndOstypeByAgentName(agentName);
            SaltApi saltApi = SaltApi.getSaltApi(NetTelnet.getConnectedIp(master.getMasterIP(), master.getMasterOstype()), 8000, ZORK_OMSAPI, ZORKDATA_8888);
            List<String> minions = new ArrayList<>();
            List<String> mods = new ArrayList<>();
            minions.add(agentName);
            mods.add(typeName+"_run");
            Map<String, Result<Map<String, State.ApplyResult>>> resultMap = saltApi.stateApply(minions, mods);
            if (resultMap != null) {
                Result<Map<String, State.ApplyResult>> result = resultMap.get(agentName);
                if(result.result().isPresent()){
                    Map<String, State.ApplyResult> applyResultMap = result.result().get();
                    for (Map.Entry<String, State.ApplyResult> entry:applyResultMap.entrySet()) {
                        State.ApplyResult applyResult = entry.getValue();
                        JsonElement changes = applyResult.getChanges();
                        Map<String, Boolean> map = JSON.parseObject(changes.toString(), new TypeReference<Map<String, Boolean>>() {
                        });
                        return map.get(typeName);
                    }
                }
            }
        } catch (Exception e) {
            stateData = false;
            System.out.println("Error:" + e.getMessage());
        }
        return stateData;
    }

    /***
     * 调用Agent的脚本执行器
     *
     * @param agentName agent名字
     * @param typeName 脚本文件名
     * @return
     */
    private boolean stopScript(String typeName, String agentName) {
        boolean stateData = false;
        try {
            MasterInfoVo master = masterBiz.getMasterIPAndOstypeByAgentName(agentName);
            SaltApi saltApi = SaltApi.getSaltApi(NetTelnet.getConnectedIp(master.getMasterIP(), master.getMasterOstype()), 8000, ZORK_OMSAPI, ZORKDATA_8888);
            List<String> minions = new ArrayList<>();
            List<String> mods = new ArrayList<>();
            minions.add(agentName);
            mods.add(typeName+"_stopservice");
            Map<String, Result<Map<String, State.ApplyResult>>> resultMap = saltApi.stateApply(minions, mods);
            if (resultMap != null) {
                Result<Map<String, State.ApplyResult>> result = resultMap.get(agentName);
                if(result.result().isPresent()){
                    Map<String, State.ApplyResult> applyResultMap = result.result().get();
                    for (Map.Entry<String, State.ApplyResult> entry:applyResultMap.entrySet()) {
                        State.ApplyResult applyResult = entry.getValue();
                        JsonElement changes = applyResult.getChanges();
                        Map<String, Object> map = JSON.parseObject(changes.toString(), new TypeReference<Map<String, Object>>() {
                        });
                        Integer retcode = Integer.parseInt(map.get("retcode").toString());
                        logger.info("执行返回code为："+retcode);
//                        if(retcode.equals(0)){
                            return true;
//                        }else {
//                            return false;
//                        }
                    }
                }
            }
        } catch (Exception e) {
            stateData = false;
            System.out.println("Error:" + e.getMessage());
        }
        return stateData;
    }

    /***
     * 上传yml文件到目录
     *
     * @param
     * @return
     */
//    public boolean upYml(String typeName, InputStream fileInputStream, String agentName) {
//        Boolean result = false;
//        String targetPath = fileConfiguration.getYmlPath() + File.separator + "beats" + File.separator + agentName + File.separator;
//        System.out.println(targetPath);
//        File targetFile = new File(targetPath);
//        if (!targetFile.exists()) {
//            targetFile.mkdirs();
//        }
//        if (fileInputStream != null) {
//            String filePath = targetPath + typeName+ ".yml";
//            result = writeToFile(fileInputStream, filePath);// 保存文件信息到磁盘
//        }
//        return result;
//    }

    public static Boolean writeToFile(InputStream is, String uploadedFileLocation) {
        // TODO Auto-generated method stub
        File file = new File(uploadedFileLocation);
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int read = 0;
            byte buffer[] = new byte[1024];
            while ((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(os!=null){
                    os.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(String.format("Path：%s 大小%s字节", file.toPath(), file.length()));
        if (file.length() < 1) {
            file.delete();
            return false;
        }
        return true;
    }

    @Override
    public RespModel ymlUpload(String typeName, MultipartFile fis, String agentName) {
        RespModel retData = null;
        boolean flag = false;
        System.out.println("接收到agentName" + agentName);
        if (fis == null) { //判断文件是否存在
            return RespTools.getRespMsgModel(CodeTable.UNKNOWN_ERROR, "未接受到文件");
        }
        try {
            Boolean ret = fastFileUpload(fis,typeName,agentName);
            if (!ret) {
                return RespTools.getRespMsgModel(CodeTable.UNKNOWN_ERROR, "上传配置文件到git失败！");
            }
            flag = sendftp(typeName, agentName);
            if (!flag) {
                return RespTools.getRespMsgModel(CodeTable.UNKNOWN_ERROR, "下发配置文件失败！");
            }
            Integer agentState = agentBiz.getAgentStateByAgentName(agentName);
            if (agentState == null) {
                return RespTools.getRespMsgModel(CodeTable.UNKNOWN_ERROR, String.format("WorkNode获取异常,请检查worknodeid是否正确!"));
            }
            if (!agentState.equals(1)) {
                return RespTools.getRespMsgModel(CodeTable.UNKNOWN_ERROR, String.format("节点%s离线或状态异常", agentName));
            }
            //LogHelper.info(String.format("调用节点%s在线", agentName), true);
            flag = stopScript(typeName, agentName);
            if (!flag) {
                return RespTools.getRespMsgModel(CodeTable.UNKNOWN_ERROR, String.format("节点%s: 关闭失败", agentName));
            }
            flag = startScript(typeName, agentName);
            if (!flag) {
                return RespTools.getRespMsgModel(CodeTable.UNKNOWN_ERROR, String.format("节点%s: 启动失败", agentName));
            }
            retData = RespTools.getRespMsgModel(CodeTable.SUCCESS, String.format("节点%s: 启动%s", agentName, flag));
        } catch (Exception e) {
            retData = RespTools.getRespMsgModel(CodeTable.UNKNOWN_ERROR, String.format("Error:%s", e.getMessage()));
        }
        return retData;
    }



    /**
     * 上传文件git
     * @param fis 文件二进制流
     * @param typeName 上传的文件名
     * @param agentName 下发的节点号名
     * @return
     */
    public Boolean fastFileUpload(MultipartFile fis,String typeName, String agentName) {
        Boolean result=false;
        String dirPath = gitConfigration.getLocalRepositoryDir() + File.separator+"beats"+File.separator+ agentName+File.separator;
        File file1 = new File(dirPath);
        if (!file1.exists()){
            file1.mkdirs();
        }
        File targetFile = new File(dirPath + typeName+".yml");
        if (fis != null) {
            String filePath = dirPath + typeName+ ".yml";
            Boolean isTransferTo = transferTo(fis, targetFile);
            if(!isTransferTo){
                return isTransferTo;
            }
        }
        String fileUploadResult = gitUtil.commitPullAndPush();
        if (CommonEnum.SUCCESS.getValue().equals(fileUploadResult)) {
           result=true;
        }else {
            //如果上传失败就删除本地文件
            targetFile.delete();
        }
        return result;
    }

    public boolean sendftp(String typeName, String agentName) {
        boolean retData = false;
        if ("".equals(agentName) || agentName == null) {
            return false;
        }
        Integer agentState = agentBiz.getAgentStateByAgentName(agentName);
        if (agentState != null) {
            if (!agentState.equals(1)) {
                retData = false;
            } else {
                String classpath = "salt://beats/"+ agentName + "/";
                String filepath = classpath + typeName + ".yml";
                //savepath部署完全路径，还需要到下下一个方法拼出全路径
                //String savepath = "3rdplugins/"+typeName+"/"+ typeName + ".yml";
                String savepath = typeName;
                String state = sendFtpClient(agentName, filepath, savepath);
                System.out.println("beatserviceimpl");
                if (state.equals("Success")) {
                    retData = true;
                } else {
                    retData = false;
                }
            }
        } else {
            retData = false;
        }
        return retData;
    }

    /**
     * 发送配置文件到Ftp
     *
     * @param node
     * @param filepath
     * @param savepath
     * @return
     */
    private String sendFtpClient(String node, String filepath, String savepath) {
        System.out.println("sendftpclient");
        Map<String, Object> map = new HashMap<>();
        List<String> minions = new ArrayList<>();
        minions.add(node);
        map.put("minions", minions);
        map.put("scrPath", filepath);
        map.put("destPath", savepath);
        Map<String, String> result = fileServiceIfc.syncSendFile(map);
        if(result.get(node).equals("")||result.get(node).equals("Result()")){
            return "Faild";
        }
        return "Success";
    }



    public static String captureName(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }

    public Boolean transferTo(MultipartFile sourFile, File tarFile) {

        try {
            sourFile.transferTo(tarFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
