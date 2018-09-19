package com.zorkdata.datasynchro.biz.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zorkdata.center.common.util.HttpClientUtil;
import com.zorkdata.datasynchro.biz.AppProgramBiz;
import com.zorkdata.datasynchro.biz.ComputerBiz;
import com.zorkdata.datasynchro.entity.BKToken;
import com.zorkdata.datasynchro.entity.Computer;
import com.zorkdata.datasynchro.login.BKLoginUtil;
import com.zorkdata.datasynchro.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CenterDataSynchroLinstener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private AppProgramBiz appProgramBiz;
    @Autowired
    private ComputerBiz computerBiz;

    private Map<Integer, String> userAppSysMap;

    private Map<String, String> headerParams;

    @Value("${cmdb.openSource.apiserver}")
    public String cmdbApiServer;

    @Value("${getToken.userName}")
    public String userName;

    @Value("${getToken.password}")
    public String password;

    @Value("${loginURL}")
    public String loginURL;

    private Logger logger = LoggerFactory.getLogger(CenterDataSynchroLinstener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() != null) {
            String passwd = DigestUtils.md5DigestAsHex(password.getBytes());
            String token = getTokenWithLogin(userName, passwd);
            if (StringUtils.isBlank(token)) {
                logger.error("token不合法，token=" + token);
                return;
            }
            headerParams = new HashMap<>();
            headerParams.put("BK_User", "admin");
            headerParams.put("Cookie", "bk_token=" + token);

            List<UserAppSystem> userAppSys = appProgramBiz.getUserAppSystem();
            userAppSysMap = new HashMap<>();
            for (UserAppSystem userApp : userAppSys) {
                if (userAppSysMap.containsKey(userApp.getAppSystemId())) {
                    userAppSysMap.put(userApp.getAppSystemId(), userAppSysMap.get(userApp.getAppSystemId()) + "," + userApp.getUserName());
                } else {
                    userAppSysMap.put(userApp.getAppSystemId(), userApp.getUserName());
                }
            }
            logger.info("开始同步数据...");
            List<AppSystemVo> lists = appProgramBiz.getBizToModule();
            // 合并集群下的相同模块
            for (AppSystemVo appSystemVo : lists) {
                Integer appSystemID = createAppSystem(appSystemVo);
                // 如果为-1需要查询出id
                if (appSystemID == -1) {
                    appSystemID = search_business(appSystemVo.getAppSystemName());
                    String userNames = userAppSysMap.get(appSystemVo.getAppSystemId());
                    if (StringUtils.isBlank(userNames)) {
                        userNames = "admin";
                    }
                    if (appSystemID < 0) {
                        logger.error(String.format("同步业务失败， 业务名=%s", appSystemVo.getAppSystemName()));
                        continue;
                    }
                    // 更新用户,测试用
                    // update_business(appSystemID, appSystemVo.getAppSystemName(), userNames);
                    logger.info(">>>> 业务="+appSystemVo.getAppSystemName()+",集群数="+appSystemVo.getSets().size());
                }
                for (SetVo set : appSystemVo.getSets()) {
                    if (null == set.getSetName()) {
                        set.setSetName("default");
                    }
                    int setId = createSet(appSystemID, set);
                    if (setId == -1) {
                        setId = search_set(appSystemID, set.getSetName());
                    }
                    if (setId < 0) {
                        logger.error(String.format("同步集群失败， 业务名=%s, 集群名=%s", appSystemVo.getAppSystemName(), set.getSetName()));
                        setId = search_set(appSystemID, set.getSetName());
                        continue;
                    }
                    List<ModuleVo> modules = set.getModules();
                    logger.info("模块数:"+set.getModules().size());
                    for (int i = 0; i < modules.size(); i++) {
                        String moduleName = modules.get(i).getModuleName();
                        System.out.println("创建模块 业务Id="+appSystemID+", setId=" +setId+",moduleName="+moduleName);
                        int moduleId = createMoudle(appSystemID, setId, moduleName);
                        if (moduleId == -1) {
                            moduleId = search_module(appSystemID, setId, moduleName);
                        }
                        if (moduleId < 0) {
                            if (StringUtils.isBlank(moduleName)){
                                continue;
                            }
                            logger.error(String.format("同步模块失败， 业务名=%s, 集群名=%s, 模块名=", appSystemVo.getAppSystemName(), set.getSetName(), moduleName));
                        }
                    }
                }
            }
            logger.info("同步数据结束...");
        }
    }

    private int getResposeValueofId(String column, String responseContent) {
        JSONObject jsonObject = JSON.parseObject(responseContent);
        if ("true".equals(jsonObject.getString("result"))) {
            JSONObject dataObj = jsonObject.getJSONObject("data");
            return dataObj.getInteger(column);
        }
        return -1;
    }

    private boolean getResposeBool(String responseContent) {
        JSONObject jsonObject = JSON.parseObject(responseContent);
        if ("true".equals(jsonObject.getString("result"))) {
            return true;
        }
        return false;
    }

    private int getResposeBizofId(String bk_biz_name, String responseContent) {
        JSONObject jsonObject = JSON.parseObject(responseContent);
        if ("true".equals(jsonObject.getString("result"))) {
            JSONObject dataObj = jsonObject.getJSONObject("data");
            JSONArray infos = dataObj.getJSONArray("info");
            for (int i = 0; i < infos.size(); i++) {
                JSONObject info = (JSONObject) infos.get(i);
                if (info.getString("bk_biz_name").equals(bk_biz_name)) {
                    return info.getInteger("bk_biz_id");
                }
            }
            return -1;
        }
        return -1;
    }

    private int getResposeSetofId(String bk_set_name, String responseContent) {
        JSONObject jsonObject = JSON.parseObject(responseContent);
        if ("true".equals(jsonObject.getString("result"))) {
            JSONObject dataObj = jsonObject.getJSONObject("data");
            JSONArray infos = dataObj.getJSONArray("info");
            for (int i = 0; i < infos.size(); i++) {
                JSONObject info = (JSONObject) infos.get(i);
                if (info.getString("bk_set_name").equals(bk_set_name)) {
                    return info.getInteger("bk_set_id");
                }
            }
            return -1;
        }
        return -1;
    }

    private int getResposeModuleofId(String bk_module_name, String responseContent) {
        JSONObject jsonObject = JSON.parseObject(responseContent);
        if ("true".equals(jsonObject.getString("result"))) {
            JSONObject dataObj = jsonObject.getJSONObject("data");
            JSONArray infos = dataObj.getJSONArray("info");
            for (int i = 0; i < infos.size(); i++) {
                JSONObject info = (JSONObject) infos.get(i);
                if (info.getString("bk_module_name").equals(bk_module_name)) {
                    return info.getInteger("bk_module_id");
                }
            }
            return -1;
        }
        return -1;
    }

    private int getResposeComputerofId(String responseContent) {
        JSONObject jsonObject = JSON.parseObject(responseContent);
        if ("true".equals(jsonObject.getString("result"))) {
            JSONObject dataObj = jsonObject.getJSONObject("data");
            JSONArray infos = dataObj.getJSONArray("info");
            if (infos.size() == 1) {
                JSONObject info = (JSONObject) infos.get(0);
                JSONObject host = (JSONObject) info.get("host");
                return host.getInteger("bk_host_id");
            }
            return -1;
        }
        return -1;
    }

    private int createAppSystem(AppSystemVo appSystemVo) {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> datas = new HashMap<>();

        String userNames = userAppSysMap.get(appSystemVo.getAppSystemId());
        if (StringUtils.isBlank(userNames)) {
            userNames = "admin";
        }

        datas.put("bk_biz_maintainer", userNames);
        datas.put("bk_biz_productor", userNames);
        datas.put("bk_biz_tester", userNames);
        datas.put("bk_biz_developer", userNames);
        datas.put("bk_biz_name", appSystemVo.getAppSystemName());
        datas.put("life_cycle", "已上线");
        datas.put("operator", "admin");
        datas.put("syscode", appSystemVo.getSysCode());

        String paramsTemp = JSON.toJSONString(datas);
        String url = cmdbApiServer + "/api/v3/biz/0";
        String res = "";
        try {
            updateToken(res);
            res = HttpClientUtil.sendHttpPost(url, paramsTemp, headerParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getResposeValueofId("bk_biz_id", res);
    }

    private void update_business(Integer bizID, String bizName, String userNames) {
        Map<String, Object> datas = new HashMap<>();
        datas.put("bk_biz_maintainer", userNames);
        datas.put("bk_biz_productor", userNames);
        datas.put("bk_biz_tester", userNames);
        datas.put("bk_biz_developer", userNames);
        datas.put("operator", "admin");
        datas.put("bk_biz_name", bizName);

        String paramsTemp = JSON.toJSONString(datas);
        String url = cmdbApiServer + "/api/v3/biz/0/" + bizID;

        String res = "";
        try {
            updateToken(res);
            res = HttpClientUtil.sendHttpPut(url, paramsTemp, headerParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int search_business(String bizName) {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> datas = new HashMap<>();
        params.put("bk_app_code", "zork-nodeman");
        params.put("bk_app_secret", "d749a5e9-0f4d-449c-b793-9080dab9b090");
        params.put("bk_supplier_id", 0);
        List<String> fieldsList = new ArrayList<>();
        fieldsList.add("bk_biz_id");
        fieldsList.add("bk_biz_name");
        params.put("fields", fieldsList);
        Map<String, String> condition = new HashMap<>();
        condition.put("bk_biz_name", bizName);
        params.put("condition", condition);
        Map<String, Object> page = new HashMap<>();
        page.put("start", 0);
        page.put("limit", 10);
        page.put("sort", "");
        params.put("page", page);

        String paramsTemp = JSON.toJSONString(params);
        String apiGateWay = cmdbApiServer + "/api/v3/biz/search/0";

        String res = "";
        try {
            updateToken(res);
            res = HttpClientUtil.sendHttpPost(apiGateWay, paramsTemp, headerParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getResposeBizofId(bizName, res);
    }

    private int createSet(int appSystemId, SetVo setVo) {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> datas = new HashMap<>();
        datas.put("bk_supplier_account", "0");
        datas.put("bk_biz_id", appSystemId);
        datas.put("bk_set_name", setVo.getSetName());
        datas.put("bk_parent_id", appSystemId);
        datas.put("bk_set_desc", "");
        datas.put("description", "");
        datas.put("bk_capacity", 1000);

        String paramsTemp = JSON.toJSONString(datas);
        String url = cmdbApiServer + "/api/v3/set/" + appSystemId;
        String res = "";
        try {
            updateToken(res);
            res = HttpClientUtil.sendHttpPost(url, paramsTemp, headerParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getResposeValueofId("bk_set_id", res);
    }

    private int search_set(Integer bizID, String setName) {
        Map<String, Object> params = new HashMap<>();
        params.put("bk_app_code", "zork-nodeman");
        params.put("bk_app_secret", "d749a5e9-0f4d-449c-b793-9080dab9b090");
        params.put("bk_supplier_id", 0);
        params.put("bk_biz_id", bizID);
        List<String> fieldsList = new ArrayList<>();
        fieldsList.add("bk_set_id");
        fieldsList.add("bk_set_name");
        fieldsList.add("bk_biz_id");
        params.put("fields", fieldsList);
        Map<String, Object> condition = new HashMap<>();
        condition.put("bk_set_name", setName);
        condition.put("bk_biz_id", bizID);
        params.put("condition", condition);
        Map<String, Object> page = new HashMap<>();
        page.put("start", 0);
        page.put("limit", 10);
        page.put("sort", "");
        params.put("page", page);
        String paramsTemp = JSON.toJSONString(params);
        String url = cmdbApiServer + "/api/v3/set/search/0/" + bizID;
        String res = "";
        try {
            updateToken(res);
            res = HttpClientUtil.sendHttpPost(url, paramsTemp, headerParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getResposeSetofId(setName, res);
    }

    private int search_module(Integer bizID, int setId, String moduleName) {
        Map<String, Object> params = new HashMap<>();
        List<String> fieldsList = new ArrayList<>();
        fieldsList.add("bk_module_id");
        fieldsList.add("bk_module_name");
        params.put("fields", fieldsList);
        Map<String, Object> condition = new HashMap<>();
        condition.put("bk_module_name", moduleName);
        condition.put("bk_biz_id", bizID);
        condition.put("bk_set_id", setId);
        params.put("condition", condition);
        Map<String, Object> page = new HashMap<>();
        page.put("start", 0);
        page.put("limit", 10);
        page.put("sort", "");
        params.put("page", page);
        String paramsTemp = JSON.toJSONString(params);
        String url = cmdbApiServer + "/api/v3/module/search/0/" + bizID + "/" + setId;
        String res = "";
        try {
            updateToken(res);
            res = HttpClientUtil.sendHttpPost(url, paramsTemp, headerParams);
            logger.info("[search module]" +res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getResposeModuleofId(moduleName, res);
    }

    private int createMoudle(int appSytemId, int setId, String moduleName) {
        Map<String, Object> datas = new HashMap<>();
        datas.put("bk_supplier_account", "0");
        datas.put("bk_biz_id", appSytemId);
        datas.put("bk_set_id", setId);
        datas.put("bk_module_name", moduleName);
        datas.put("bk_parent_id", setId);
        datas.put("operator", "");
        datas.put("bk_module_type", "普通");
        datas.put("bk_bak_operator", "");

        String paramsTemp = JSON.toJSONString(datas);
        String url = cmdbApiServer + "/api/v3/module/" + appSytemId + "/" + setId;
        String res = "";
        try {
            updateToken(res);
            res = HttpClientUtil.sendHttpPost(url, paramsTemp, headerParams);
            logger.info("[create module]"+res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getResposeValueofId("bk_module_id", res);
    }

    private int search_module(Integer bizID, Integer setID, String moduleName) {
        Map<String, Object> params = new HashMap<>();
        params.put("bk_app_code", "zork-nodeman");
        params.put("bk_app_secret", "d749a5e9-0f4d-449c-b793-9080dab9b090");
        params.put("bk_supplier_id", 0);
        params.put("bk_biz_id", bizID);
        params.put("bk_set_id", setID);
        List<String> fieldsList = new ArrayList<>();
        fieldsList.add("bk_module_id");
        fieldsList.add("bk_module_name");
        params.put("fields", fieldsList);
        Map<String, String> condition = new HashMap<>();
        condition.put("bk_module_name", moduleName);
        params.put("condition", condition);
        Map<String, Object> page = new HashMap<>();
        page.put("start", 0);
        page.put("limit", 10);
        page.put("sort", "");
        params.put("page", page);
        String paramsTemp = JSON.toJSONString(params);
        String url = cmdbApiServer + "/api/v3/module/" + bizID + "/" + setID;
        ;
        String res = "";
        try {
            updateToken(res);
            res = HttpClientUtil.sendHttpPost(url, paramsTemp, headerParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getResposeModuleofId(moduleName, res);
    }

    private String getTokenWithLogin(String userName, String password) {
        String token = "";
        Map<String, String> params = new HashMap<>();
        params.put("username", userName);
        params.put("password", password);
        params.put("productCode", "ControlCenter");
        try {
            String resp = HttpClientUtil.sendHttpPost(loginURL, JSON.toJSONString(params), null);
            JSONObject jsonObject = JSON.parseObject(resp);
            if ("000000".equals(jsonObject.getString("code"))) {
                JSONObject dataJson = (JSONObject) jsonObject.get("data");
                token = dataJson.getString("token");
            }
            //logger.debug(String.format("获取token成功, token=%s", token));
        } catch (Exception e) {
            logger.error(String.format("获取token失败: userName=%s,password=%s", userName, password));
        }

        return token;
    }

    private void updateToken(String res){
        String passwd = DigestUtils.md5DigestAsHex(password.getBytes());
        String token = getTokenWithLogin(userName, passwd);
        headerParams.put("Cookie", "bk_token=" + token);
    }
}
