package com.zorkdata.center.admin.rpc.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zorkdata.center.admin.rpc.service.Ifc.AppProgramServiceIfc;
import com.zorkdata.center.admin.rpc.service.Ifc.ComputerServiceIfc;
import com.zorkdata.center.admin.util.JsonUtil;
import com.zorkdata.center.admin.vo.AppProgramVo;
import com.zorkdata.center.admin.vo.AppSystemInfoVo;
import com.zorkdata.center.admin.vo.WorkerNode;
import com.zorkdata.center.common.core.RespModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AppProgramServiceImpl implements AppProgramServiceIfc {
    @Autowired
    private ComputerServiceIfc computerService;
    @Autowired
    private BkImpl bkImpl;

    @Override
    public Map<String, List<AppProgramVo>> getAppProgramVoV3(String bk_biz_id, String userName, String tokenString) throws Exception {
        Map<String, List<AppProgramVo>> appAppProgramVoMap = new HashMap<>();
        List<WorkerNode> computerAgentInfo = computerService.getComputerAgentInfo();
        List<AppSystemInfoVo> appSystemInfoVos = new ArrayList<>();


        Map<String, Object> param = new HashMap<>();
        param.put("username", userName);
        if (tokenString != null && StringUtils.isNotBlank(tokenString)) {
            param.put("token", tokenString);
        }
        RespModel bkTopoInst = bkImpl.searchTopoInst(null, bk_biz_id + "", param);
        List<AppProgramVo> listapp = new ArrayList<>();
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
                        JSONArray modules = (JSONArray) set.get("child");
                        for (int modulei = 0; modulei < modules.size(); modulei++) {
                            JSONObject module = (JSONObject) modules.get(modulei);
                            String moduleId = module.get("bk_inst_id").toString();
                            String moduleName = module.get("bk_inst_name").toString();
                            AppProgramVo appProgramVo = new AppProgramVo();
                            appProgramVo.setAppInstanceId(moduleId);
                            appProgramVo.setAppSystemName(appsystemName);
                            appProgramVo.setAppProgramName(moduleName);
                            appProgramVo.setClusterInstanceId(setId);
                            appProgramVo.setClusterName(setName);
                            listapp.add(appProgramVo);
                        }
                    }
                }
            }
        }

        // 获取业务信息
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> page = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        condition.put("bk_biz_id", Integer.parseInt(bk_biz_id));
        page.put("limit", 200);
        page.put("start", 0);
        map.put("page", page);
        map.put("bk_biz_id", bk_biz_id);
        map.put("username", userName);
        map.put("condition", condition);

        if (tokenString != null && StringUtils.isNotBlank(tokenString)) {
            map.put("token", tokenString);
        }
        RespModel bkHostInst = bkImpl.searchBizInst(null, map);
        if ("000000".equals(bkHostInst.getCode())) {
            System.out.println(bkHostInst.getData().toString());
            JSONObject obj = JSON.parseObject(bkHostInst.getData().toString());
            JSONArray jsonArray = (JSONArray) obj.get("info");
            if (jsonArray.size() != 0) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject appSystem = (JSONObject) jsonArray.get(i);
                    if (!haszBizPermission(appSystem, userName)) {
                        continue;
                    }

                    Integer appSystemID = Integer.parseInt(appSystem.get("bk_biz_id").toString());
                    String appSystemName = appSystem.get("bk_biz_name").toString();

                    AppProgramVo appProgramVo = new AppProgramVo();
                    appProgramVo.setAppSystemName(appSystemName);

                    // 获取机器信息
                    Map<String, Object> pages = new HashMap<>();
                    Map<String, Object> map1 = new HashMap<>();
                    pages.put("limit", 200);
                    pages.put("start", 0);
                    map1.put("page", pages);
                    map1.put("bk_biz_id", appSystemID);
                    map1.put("username", userName);
                    map1.put("token", tokenString);
                    List<Map<String, Object>> conditions = new ArrayList<>();
                    Map<String, Object> paramHostMap = new HashMap<>();
                    paramHostMap.put("bk_obj_id", "host");
                    paramHostMap.put("fields", new ArrayList<>());
                    paramHostMap.put("condition", new ArrayList<>());
                    conditions.add(paramHostMap);
                    Map<String, Object> paramModuleMap = new HashMap<>();
                    paramModuleMap.put("bk_obj_id", "module");
                    paramModuleMap.put("fields", new ArrayList<>());
                    paramModuleMap.put("condition", new ArrayList<>());
                    conditions.add(paramModuleMap);
                    Map<String, Object> paramSetMap = new HashMap<>();
                    paramSetMap.put("bk_obj_id", "set");
                    paramSetMap.put("fields", new ArrayList<>());
                    paramSetMap.put("condition", new ArrayList<>());
                    conditions.add(paramSetMap);
                    Map<String, Object> paramBizMap = new HashMap<>();
                    paramBizMap.put("bk_obj_id", "biz");
                    paramBizMap.put("fields", new ArrayList<>());
                    paramBizMap.put("condition", new ArrayList<>());
                    conditions.add(paramBizMap);
                    map1.put("condition", conditions);
                    map1.put("pattern", "");
                    Map<String, Object> ipParam = new HashMap<>();
                    ipParam.put("data", null);
                    ipParam.put("exact", 0);
                    ipParam.put("flag", "bk_host_innerip|bk_host_outerip");
                    map1.put("ip", ipParam);
                    RespModel hostInst = bkImpl.searchHostInst(null, map1);
                    System.out.println(JSON.toJSONString(map1));
                    if ("000000".equals(hostInst.getCode())) {
                        JSONObject object = JSON.parseObject(hostInst.getData().toString());
                        JSONArray hostArr = (JSONArray) object.get("info");
                        if (hostArr.size() != 0) {
                            List<WorkerNode> workerNodes = new ArrayList<>();
                            //模块中存在多个机器暂存在tempAppPros
                            List<AppProgramVo> tempAppPros = new ArrayList<>();
                            for (int j = 0; j < hostArr.size(); j++) {
                                JSONObject temp = (JSONObject) hostArr.get(j);
                                JSONObject host = (JSONObject) temp.get("host");
                                JSONObject module = (JSONObject) temp.get("module");
                                JSONObject set = (JSONObject) temp.get("set");
                                String ip = host.get("bk_host_innerip").toString();
                                String hostid = host.get("bk_host_id").toString();
                                String hostName = host.get("bk_host_name").toString();
                                String sets = "";
                                String modules = "";
                                if (set != null){
                                    sets = set.getString("bk_set_name");
                                }
                                if (module != null){
                                    modules = module.getString("bk_module_name");
                                }

                                // 该机器必须存在于agent表中
                                if (!ip.equals("127.0.0.1")) {
                                    for (WorkerNode workerNodeTemp : computerAgentInfo) {
                                        if (workerNodeTemp.getIp().contains(ip)) {
                                            for (AppProgramVo appProgramVo1 : listapp) {
                                                if (sets != null && sets.indexOf(appProgramVo1.getClusterName()) >= 0) {
                                                    if (modules != null && modules.indexOf(appProgramVo1.getAppProgramName()) >= 0) {
                                                            appProgramVo1.setComputerId(hostid);
                                                            appProgramVo1.setHostName(hostName);
                                                            appProgramVo1.setIPAddress(ip);
                                                            appProgramVo1.setOsType(workerNodeTemp.getType());
                                                            AppProgramVo appProgramVo2 = new AppProgramVo();
                                                            transferData(appProgramVo1, appProgramVo2);
                                                            tempAppPros.add(appProgramVo2);
                                                    }
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                            if (tempAppPros.size() > 1){
                                listapp.addAll(tempAppPros);
                                listapp.remove(0);
                            }
                        }
                    }
                }
            }
        }
        appAppProgramVoMap.put("3.0", listapp);
        return appAppProgramVoMap;
    }

    @Override
    public Map<String, Object> getAppProgramAllVersion(String appProgramV2, List<AppProgramVo> appProgramVoList) {
        JSONObject appProgtamVo2VJson = JSON.parseObject(appProgramV2);
        Map<String, Object> appAppProgramVoMap = new HashMap<>();
        JSONArray arrayV2 = null;
        if (appProgtamVo2VJson != null){
            arrayV2 = (JSONArray) appProgtamVo2VJson.get("2.2");
        }

        appAppProgramVoMap.put("2.2", arrayV2);
        appAppProgramVoMap.put("3.0", appProgramVoList);
        return appAppProgramVoMap;
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

    private void transferData(AppProgramVo appProgramVo1, AppProgramVo appProgramVo2){
        appProgramVo2.setAppInstanceId(appProgramVo1.getAppInstanceId());
        appProgramVo2.setAppSystemName(appProgramVo1.getAppSystemName());
        appProgramVo2.setAppProgramName(appProgramVo1.getAppProgramName());
        appProgramVo2.setClusterInstanceId(appProgramVo1.getClusterInstanceId());
        appProgramVo2.setClusterName(appProgramVo1.getClusterName());
        appProgramVo2.setComputerId(appProgramVo1.getComputerId());
        appProgramVo2.setHostName(appProgramVo1.getHostName());
        appProgramVo2.setIPAddress(appProgramVo1.getIPAddress());
        appProgramVo2.setOsType(appProgramVo1.getOsType());
    }
}
