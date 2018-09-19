package com.zorkdata.center.admin.rpc.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zorkdata.center.admin.rpc.service.Ifc.AppSystemServiceIfc;
import com.zorkdata.center.admin.rpc.service.Ifc.ComputerServiceIfc;
import com.zorkdata.center.admin.util.JsonUtil;
import com.zorkdata.center.admin.vo.*;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class AppSystemServiceImpl implements AppSystemServiceIfc {
    @Autowired
    private ComputerServiceIfc computerService;
    @Autowired
    private BkImpl bkImpl;

    @Override
    public Map<String, List<AppSystemInfoVo>> getNewAgents() {
        return null;
    }

    @Override
    public Map<String, List<AppSystemInfoVo>> getAppSystemInfoVoV3(String userName, String tokenString) throws Exception {
        Map<String, List<AppSystemInfoVo>> appSystemInfoMap = new HashMap<>();
        List<WorkerNode> computerAgentInfo = computerService.getComputerAgentInfo();
        List<AppSystemInfoVo> appSystemInfoVos = new ArrayList<>();

        // 获取业务信息
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> page = new HashMap<>();
        // 每次获取pageCount条数据
        int pageCount = 200;
        page.put("limit", pageCount);
        page.put("start", 0);
        map.put("page", page);
        map.put("username", userName);
        if (StringUtils.isNotBlank(tokenString)) {
            map.put("token", tokenString);
        }
        RespModel bkHostInst = bkImpl.searchBizInst(null, map);

        // 取出所有业务数据
        if ("000000".equals(bkHostInst.getCode())) {
            JSONObject obj = JSON.parseObject(bkHostInst.getData().toString());
            JSONArray jsonArray = (JSONArray) obj.get("info");
            Integer count = jsonArray.size();

            int getDataCount = 0;
            while (true) {
                if (count == pageCount) {
                    Map<String, Object> mapTemp = new HashMap<>();
                    Map<String, Object> pageTemp = new HashMap<>();
                    pageTemp.put("limit", pageCount);
                    pageTemp.put("start", pageCount * (++getDataCount));
                    mapTemp.put("page", pageTemp);
                    mapTemp.put("username", userName);
                    if (StringUtils.isNotBlank(tokenString)) {
                        mapTemp.put("token", tokenString);
                    }
                    RespModel bkHostInstTemp = bkImpl.searchBizInst(null, mapTemp);
                    if ("000000".equals(bkHostInstTemp.getCode())) {
                        JSONObject objTemp = JSON.parseObject(bkHostInstTemp.getData().toString());
                        JSONArray jsonArrayTemp = (JSONArray) objTemp.get("info");
                        count = jsonArrayTemp.size();
                        jsonArray.addAll(jsonArrayTemp);
                    }
                } else {
                    break;
                }
            }
            // 解析数据
            if (jsonArray.size() != 0) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject appSystem = (JSONObject) jsonArray.get(i);
                    if (!haszBizPermission(appSystem, userName)) {
                        continue;
                    }

                    Integer appSystemID = Integer.parseInt(appSystem.getString("bk_biz_id"));
                    String appSystemName = appSystem.getString("bk_biz_name");
                    String sysCode = appSystem.getString("syscode");
                    AppSystemInfoVo appSystemInfoVo = new AppSystemInfoVo();
                    appSystemInfoVo.setAppSystemId(appSystemID);
                    appSystemInfoVo.setAppSystemName(appSystemName);
                    appSystemInfoVo.setSysCode(sysCode);
                    // 获取机器信息
                    Map<String, Object> pages = new HashMap<>();
                    Map<String, Object> map1 = new HashMap<>();
                    pages.put("limlt", 10);
                    pages.put("start", 0);
                    map1.put("page", pages);
                    map1.put("bk_biz_id", appSystemID);
                    map1.put("username", userName);
                    map1.put("token", tokenString);
                    RespModel hostInst = bkImpl.searchHostInst(null, map1);
                    if ("000000".equals(hostInst.getCode())) {
                        JSONObject object = JSON.parseObject(hostInst.getData().toString());
                        JSONArray hostArr = (JSONArray) object.get("info");
                        if (hostArr.size() != 0) {
                            List<WorkerNode> workerNodes = new ArrayList<>();
                            for (int j = 0; j < hostArr.size(); j++) {
                                JSONObject temp = (JSONObject) hostArr.get(j);
                                JSONObject host = (JSONObject) temp.get("host");
                                String ip = host.get("bk_host_innerip").toString();

                                // 该机器必须存在于agent表中
                                if (!ip.equals("127.0.0.1")) {
                                    WorkerNode workerNode = null;
                                    for (WorkerNode workerNodeTemp : computerAgentInfo) {
                                        if (workerNodeTemp.getIp().contains(ip)) {
                                            workerNode = new WorkerNode();
                                            workerNode.setIp(ip);
                                            workerNode.setState(workerNodeTemp.getState());
                                            workerNode.setType(workerNodeTemp.getType());
                                            workerNode.setWorkerNodeName(workerNodeTemp.getWorkerNodeName());
                                            workerNode.setWorkerNodeId(workerNodeTemp.getWorkerNodeId());
                                        }
                                    }
                                    if (null != workerNode) {
                                        workerNodes.add(workerNode);
                                    }
                                }
                            }
                            appSystemInfoVo.setWorkerNodes(workerNodes);
                        }
                    }
                    appSystemInfoVos.add(appSystemInfoVo);
                }
            }
        }
        appSystemInfoMap.put("3.0", appSystemInfoVos);
        return appSystemInfoMap;
    }

    @Override
    public Map<Integer, String> getBizIdWithIpRelation(List<AppSystemInfoVo> appSystemInfoVos) {
        Map<Integer, String> appSystemNameWithIp = new HashMap<>();
        for (AppSystemInfoVo appSystemInfoVo : appSystemInfoVos) {
            Integer bizId = appSystemInfoVo.getAppSystemId();
            List<WorkerNode> workerNodes = appSystemInfoVo.getWorkerNodes();
            if (null == workerNodes) {
                continue;
            }

            for (WorkerNode workerNode : workerNodes) {
                if (null == workerNode) {
                    continue;
                }
                appSystemNameWithIp.put(bizId, workerNode.getIp());
            }
        }
        return appSystemNameWithIp;
    }

    @Override
    public Map<String, Object> getAppSystemInfoAllVersion(String appSystemInfoV2, List<AppSystemInfoVo> appSystemInfoVo3V) {
        JSONObject appSystemInfoVo2VJson = JSON.parseObject(appSystemInfoV2);
        JSONArray arrayV2 = (JSONArray) appSystemInfoVo2VJson.get("2.2");
        Map<String, Object> appSystemInfoMap = new HashMap<>();
        Map<Integer, String> appSystemNameWithIpRelation = getBizIdWithIpRelation(appSystemInfoVo3V);

        if (arrayV2.size() != 0) {
            for (int m = 0; m < arrayV2.size(); m++) {
                JSONObject appSystem2V = (JSONObject) arrayV2.get(m);
                int appSystemId = Integer.parseInt(appSystem2V.getString("appSystemId"));
                if (appSystemNameWithIpRelation.size() > 0) {
                    if (appSystemNameWithIpRelation.containsKey(appSystemId)) {
                        JSONArray nodes = (JSONArray) appSystem2V.get("workerNodes");
                        for (int n = 0; n < nodes.size(); n++) {
                            JSONObject workNode = (JSONObject) arrayV2.get(n);
                            String ipv2 = workNode.getString("ip");

                            //若同一个业务存在同一台机器拥有两个版本，则在3.0中返回该机器，2.2中作移除
                            if (appSystemNameWithIpRelation.get(appSystemId).equals(ipv2)) {
                                nodes.remove(workNode);
                            }
                        }
                    }
                }
            }
        }
        appSystemInfoMap.put("2.2", arrayV2);
        appSystemInfoMap.put("3.0", appSystemInfoVo3V);
        return appSystemInfoMap;
    }

    @Override
    public Map<String, List<AppClusterVo>> getClusterBySysV3(String bk_biz_id, String userName, String tokenString)
            throws Exception {
        Map<String, List<AppClusterVo>> appClusterVoMap = new HashMap<>();

        Map<String, Object> param = new HashMap<>();
        param.put("username", userName);
        if (tokenString != null && StringUtils.isNotBlank(tokenString)) {
            param.put("token", tokenString);
        }
        RespModel bkTopoInst = bkImpl.searchTopoInst(null, bk_biz_id + "", param);
        List<AppClusterVo> listapp = new ArrayList<>();
        if ("000000".equals(bkTopoInst.getCode())) {
            System.out.println(bkTopoInst.getData().toString());
            JSONArray objs = JSON.parseArray(bkTopoInst.getData().toString());
            if (objs.size() != 0) {
                for (int i = 0; i < objs.size(); i++) {
                    JSONObject appSystem = (JSONObject) objs.get(i);
                    String appsystemId = appSystem.get("bk_inst_id").toString();
                    String appsystemName = appSystem.get("bk_inst_name").toString();
                    JSONArray sets = (JSONArray) appSystem.get("child");
                    for (int seti = 0; seti < sets.size(); seti++) {
                        JSONObject set = (JSONObject) sets.get(seti);
                        String setId = set.get("bk_inst_id").toString();
                        String setName = set.get("bk_inst_name").toString();

                        AppClusterVo appClusterVo = new AppClusterVo();
                        appClusterVo.setAppSystemId(Integer.parseInt(appsystemId));
                        appClusterVo.setAppSystemName(appsystemName);
                        appClusterVo.setClusterId(Integer.parseInt(setId));
                        appClusterVo.setClusterName(setName);
                        listapp.add(appClusterVo);
                    }
                }
            }
        }

        appClusterVoMap.put("3.0", listapp);
        return appClusterVoMap;
    }

    @Override
    public Map<String, Object> getClusterBySysAllVersion(String appClusterV2, List<AppClusterVo> appClusterVoList) {
        JSONObject appClusterVo2VJson = JSON.parseObject(appClusterV2);
        JSONArray arrayV2 = null;
        if (appClusterVo2VJson != null) {
            arrayV2 = (JSONArray) appClusterVo2VJson.get("2.2");
        }

        Map<String, Object> appClusterMap = new HashMap<>();
        appClusterMap.put("2.2", arrayV2);
        appClusterMap.put("3.0", appClusterVoList);
        return appClusterMap;
    }

    private boolean haszBizPermission(JSONObject appSystem, String userName) {
        String developer = appSystem.getString("bk_biz_developer");
        String maintainer = appSystem.getString("bk_biz_maintainer");
        String productor = appSystem.getString("bk_biz_productor");
        String tester = appSystem.getString("bk_biz_tester");

        if (StringUtils.isNotBlank(developer)) {
            return judgeRoleContainsUser(developer, userName);
        }
        if (StringUtils.isNotBlank(maintainer)) {
            return judgeRoleContainsUser(maintainer, userName);
        }
        if (StringUtils.isNotBlank(productor)) {
            return judgeRoleContainsUser(productor, userName);
        }
        if (StringUtils.isNotBlank(tester)) {
            return judgeRoleContainsUser(tester, userName);
        }
        return false;
    }

    private boolean judgeRoleContainsUser(String nameList, String userName){
        String names[] = nameList.split(",");
        if (names.length <= 1){
            if (nameList.equals(userName)){
                return true;
            }
        } else {
            for(String name : names){
                if (StringUtils.isNotBlank(name) && name.equals(userName)){
                    return true;
                }
            }
        }
        return false;
    }
}
