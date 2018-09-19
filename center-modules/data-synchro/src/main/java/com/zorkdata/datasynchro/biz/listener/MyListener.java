package com.zorkdata.datasynchro.biz.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zorkdata.center.common.util.HttpClientUtil;
import com.zorkdata.datasynchro.biz.AppProgramBiz;
import com.zorkdata.datasynchro.biz.ComputerBiz;
import com.zorkdata.datasynchro.entity.BKToken;
import com.zorkdata.datasynchro.entity.Computer;
import com.zorkdata.datasynchro.login.BKLoginUtil;
import com.zorkdata.datasynchro.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private AppProgramBiz appProgramBiz;
    @Autowired
    private ComputerBiz computerBiz;

    private Map<Integer, String> userAppSysMap;

    private BKToken bkToken;

    @Value("${cmdb.community.apiserver}")
    private String cmdbUrl;

    @Value("${cmdb.community.appSecret}")
    private String appSecret;

    private Logger logger = LoggerFactory.getLogger(MyListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
     if (contextRefreshedEvent.getApplicationContext().getParent() != null) {
            bkToken = BKLoginUtil.Login("admin", "zorkdata");
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
                    // 更新用户,测试用
                    //update_business(appSystemID, appSystemVo.getAppSystemName(), userNames);
                }
                for (SetVo set : appSystemVo.getSets()) {
                    int setId = createSet(appSystemID, set);
                    if (setId == -1) {
                        setId = search_set(appSystemID, set.getSetName());
                    }
                    List<ModuleVo> modules = set.getModules();
                    for (ModuleVo moduleVo : modules) {
                        createMoudle(appSystemID, setId, moduleVo);
                    }
                }
            }
            logger.info("业务集群模块同步结束...");
            logger.info("机器同步资源池开始...");
             //导入主机
            List<Computer> computers = computerBiz.getAllComputer();

            boolean ok = createHost(computers);
            System.out.println(ok);
            logger.info("机器同步资源池结束...");
             //查询出业务下模块对应主机
            List<BizVo2> list=computerBiz.getBizToComputer();
            // 查询蓝鲸cmdb主机符合要求的主机
            logger.info("绑定机器归属模块开始...");
            for (BizVo2 biz:list) {
                Integer bizID = search_business(biz.getAppSystemName());
                for (SetVo2 set:biz.getSetVos2()) {
                    Integer setID = search_set(bizID, set.getSetName());
                    for(MoudleComputerVo mc:set.getModules()){
                        Integer hostID = search_host(mc.getIp());
                        Integer moduleID = search_module(bizID, setID, mc.getModuleName());
                        addHostToBiz(bizID,hostID);
                        addHostToModule(bizID,moduleID,hostID);
                    }
                }
            }
         logger.info("绑定机器归属模块结束...");
         logger.info("同步数据全部结束...");
         // 查询到的主机转移到业务资源池
         // 然后再从业务资源池转移到模块
     }
        //然后再从业务资源池转移到模块
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

    private int getResposeModuleofId(String bk_set_name, String responseContent) {
        JSONObject jsonObject = JSON.parseObject(responseContent);
        if ("true".equals(jsonObject.getString("result"))) {
            JSONObject dataObj = jsonObject.getJSONObject("data");
            JSONArray infos = dataObj.getJSONArray("info");
            for (int i = 0; i < infos.size(); i++) {
                JSONObject info = (JSONObject) infos.get(i);
                if (info.getString("bk_module_name").equals(bk_set_name)) {
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
            if(infos.size()==1){
                JSONObject info = (JSONObject) infos.get(0);
                JSONObject host = (JSONObject)info.get("host");
                return host.getInteger("bk_host_id");
            }
            return -1;
        }
        return -1;
    }

    private int createAppSystem(AppSystemVo appSystemVo) {
        Map<String, String> map = new HashMap<>();
        map.put("X-CSRFToken", bkToken.getBklogin_csrftoken());
        map.put("Cookie", "bk_token=" + bkToken.getBk_token() + ";" + "bklogin_csrftoken=" + bkToken.getBklogin_csrftoken());
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> datas = new HashMap<>();

        String userNames = userAppSysMap.get(appSystemVo.getAppSystemId());
        if (StringUtils.isBlank(userNames)) {
            userNames = "admin";
        }

        params.put("bk_app_code", "zork-nodeman");
        params.put("bk_app_secret", appSecret);
        params.put("bk_token", bkToken.getBk_token());
        params.put("bk_supplier_id", 0);
        datas.put("life_cycle", "2");
        datas.put("language", "1");
        datas.put("bk_biz_maintainer", userNames);
        datas.put("bk_biz_productor", userNames);
        datas.put("bk_biz_tester", userNames);
        datas.put("bk_biz_developer", userNames);
        datas.put("operator", "");
        datas.put("bk_biz_name", appSystemVo.getAppSystemName());
        datas.put("time_zone", "Asia/Shanghai");
        params.put("data", datas);

        String paramsTemp = JSON.toJSONString(params);
        String apiGateWay = cmdbUrl+"/api/c/compapi/v2/cc/create_business/";
//        String a ="http://cmdb.blueking.com/api/v3/biz/0";
        String res = "";
        try {
            res = HttpClientUtil.sendHttpPost(apiGateWay, paramsTemp, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getResposeValueofId("bk_biz_id", res);
    }

    private void update_business(Integer bizID, String bizName, String userNames) {
        Map<String, String> map = new HashMap<>();
        map.put("X-CSRFToken", bkToken.getBklogin_csrftoken());
        map.put("Cookie", "bk_token=" + bkToken.getBk_token() + ";" + "bklogin_csrftoken=" + bkToken.getBklogin_csrftoken());
        Map<String, Object> params = new HashMap<>();
        params.put("bk_app_code", "zork-nodeman");
        params.put("bk_app_secret", appSecret);
        params.put("bk_token", bkToken.getBk_token());
        params.put("bk_supplier_id", 0);
        params.put("bk_biz_id", bizID);
        Map<String, String> datas = new HashMap<>();
        datas.put("life_cycle", "2");
        datas.put("language", "1");
        datas.put("bk_biz_maintainer", userNames);
        datas.put("bk_biz_productor", userNames);
        datas.put("bk_biz_tester", userNames);
        datas.put("bk_biz_developer", userNames);
        datas.put("operator", "");
        datas.put("bk_biz_name", bizName);
        datas.put("time_zone", "Asia/Shanghai");
        params.put("data", datas);

        String paramsTemp = JSON.toJSONString(params);
        String apiGateWay = cmdbUrl+"/api/c/compapi/v2/cc/update_business/";

        String res = "";
        try {
            res = HttpClientUtil.sendHttpPost(apiGateWay, paramsTemp, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int search_business(String bizName) {
        Map<String, String> map = new HashMap<>();
        map.put("X-CSRFToken", bkToken.getBklogin_csrftoken());
        map.put("Cookie", "bk_token=" + bkToken.getBk_token() + ";" + "bklogin_csrftoken=" + bkToken.getBklogin_csrftoken());
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> datas = new HashMap<>();
        params.put("bk_app_code", "zork-nodeman");
        params.put("bk_app_secret", appSecret);
        params.put("bk_token", bkToken.getBk_token());
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
        String apiGateWay = cmdbUrl+"/api/c/compapi/v2/cc/search_business/";

        String res = "";
        try {
            res = HttpClientUtil.sendHttpPost(apiGateWay, paramsTemp, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getResposeBizofId(bizName, res);
    }

    private int createSet(int appSystemId, SetVo setVo) {
        Map<String, String> map = new HashMap<>();
        map.put("X-CSRFToken", bkToken.getBklogin_csrftoken());
        map.put("Cookie", "bk_token=" + bkToken.getBk_token() + ";" + "bklogin_csrftoken=" + bkToken.getBklogin_csrftoken());
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> datas = new HashMap<>();
        params.put("bk_app_code", "zork-nodeman");
        params.put("bk_app_secret", appSecret);
        params.put("bk_token", bkToken.getBk_token());
        params.put("bk_supplier_id", 0);
        params.put("bk_biz_id", appSystemId);
        datas.put("bk_set_name", setVo.getSetName());
        datas.put("bk_parent_id", appSystemId);
        datas.put("bk_set_desc", "");
        datas.put("description", "");
        datas.put("bk_capacity", "");
        params.put("data", datas);

        String paramsTemp = JSON.toJSONString(params);
//        String a = "http://cmdb.blueking.com/api/v3/set/" + AppSystemId;
        String apiGateWay = cmdbUrl+"/api/c/compapi/v2/cc/create_set/";

        String res = "";
        try {
            res = HttpClientUtil.sendHttpPost(apiGateWay, paramsTemp, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getResposeValueofId("bk_set_id", res);
    }

    private int search_set(Integer bizID, String setName) {
        Map<String, String> map = new HashMap<>();
        map.put("X-CSRFToken", bkToken.getBklogin_csrftoken());
        map.put("Cookie", "bk_token=" + bkToken.getBk_token() + ";" + "bklogin_csrftoken=" + bkToken.getBklogin_csrftoken());
        Map<String, Object> params = new HashMap<>();
        params.put("bk_app_code", "zork-nodeman");
        params.put("bk_app_secret", appSecret);
        params.put("bk_token", bkToken.getBk_token());
        params.put("bk_supplier_id", 0);
//        List<Integer> bizIds = new ArrayList<>();
//        bizIds.add(bizID);
        params.put("bk_biz_id", bizID);
        List<String> fieldsList = new ArrayList<>();
        fieldsList.add("bk_set_id");
        fieldsList.add("bk_set_name");
        params.put("fields", fieldsList);
        Map<String, String> condition = new HashMap<>();
        condition.put("bk_set_name", setName);
        params.put("condition", condition);
        Map<String, Object> page = new HashMap<>();
        page.put("start", 0);
        page.put("limit", 10);
        page.put("sort", "");
        params.put("page", page);
        String paramsTemp = JSON.toJSONString(params);
        String apiGateWay = cmdbUrl+"/api/c/compapi/v2/cc/search_set/";
        String res = "";
        try {
            res = HttpClientUtil.sendHttpPost(apiGateWay, paramsTemp, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getResposeSetofId(setName, res);
    }

    private int createMoudle(int appSytemId, int setId, ModuleVo moduleVo) {
        Map<String, String> map = new HashMap<>();
        map.put("X-CSRFToken", bkToken.getBklogin_csrftoken());
        map.put("Cookie", "bk_token=" + bkToken.getBk_token() + ";" + "bklogin_csrftoken=" + bkToken.getBklogin_csrftoken());
        Map<String, Object> params = new HashMap<>();
        params.put("bk_app_code", "zork-nodeman");
        params.put("bk_app_secret", appSecret);
        params.put("bk_token", bkToken.getBk_token());
        params.put("bk_supplier_id", 0);
        params.put("bk_biz_id", appSytemId);
        params.put("bk_set_id", setId);
        Map<String, Object> datas = new HashMap<>();
        datas.put("bk_module_name", moduleVo.getModuleName());
        datas.put("bk_parent_id", setId);
        datas.put("operator", "");
        datas.put("bk_bak_operator", "");
        params.put("data", datas);
        String paramsTemp = JSON.toJSONString(params);
        String apiGateWay = cmdbUrl+"/api/c/compapi/v2/cc/create_module/";
        //String a = "http://cmdb.blueking.com/api/v3/module/" + appSytemId + "/" + setId;
        String res = "";
        try {
            res = HttpClientUtil.sendHttpPost(apiGateWay, paramsTemp, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getResposeValueofId("bk_module_id", res);
    }

    private int search_module(Integer bizID,Integer setID,String moduleName) {
        Map<String, String> map = new HashMap<>();
        map.put("X-CSRFToken", bkToken.getBklogin_csrftoken());
        map.put("Cookie", "bk_token=" + bkToken.getBk_token() + ";" + "bklogin_csrftoken=" + bkToken.getBklogin_csrftoken());
        Map<String, Object> params = new HashMap<>();
        params.put("bk_app_code", "zork-nodeman");
        params.put("bk_app_secret", appSecret);
        params.put("bk_token", bkToken.getBk_token());
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
        String apiGateWay = cmdbUrl+"/api/c/compapi/v2/cc/search_module/";
        String res = "";
        try {
            res = HttpClientUtil.sendHttpPost(apiGateWay, paramsTemp, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getResposeModuleofId(moduleName, res);
    }

    private boolean createHost(List<Computer> computerList) {
        Map<String, String> map = new HashMap<>();
        map.put("X-CSRFToken", bkToken.getBklogin_csrftoken());
        map.put("Cookie", "bk_token=" + bkToken.getBk_token() + ";" + "bklogin_csrftoken=" + bkToken.getBklogin_csrftoken());
        Map<String, Object> params = new HashMap<>();
        params.put("bk_app_code", "zork-nodeman");
        params.put("bk_app_secret", appSecret);
        params.put("bk_token", bkToken.getBk_token());
        params.put("bk_supplier_id", 0);
        Map<String, Map<String, Object>> host_info = new HashMap<>();
        int i = 0;
        for (Computer computer : computerList) {
            if (computer.getIp().indexOf(",")>=0) {
                computer.setIp(computer.getIp().split(",")[0]);
            }

            if (computer.getIp().indexOf(",") > 0) {
                for (String ip : computer.getIp().split(",")) {
                    Map<String, Object> computermap = new HashMap<>();
                    computermap.put("bk_host_innerip", computer.getIp());
                    computermap.put("bk_host_name",computer.getComputerName());
//                    if(computer.getType().equals("linuxcomputer")){
//                        computermap.put("bk_os_type","Linux");
//                    }else {
//                        computermap.put("bk_os_type","Windows");
//                    }
                    computermap.put("bk_cloud_id", 0);
                    host_info.put("" + i, computermap);
                    i++;
                }
            } else {
                Map<String, Object> computermap = new HashMap<>();
                computermap.put("bk_host_innerip", computer.getIp());
                computermap.put("bk_host_name",computer.getComputerName());
                computermap.put("bk_cloud_id", 0);
                host_info.put("" + i, computermap);
            }
            if (i % 100 == 0) {
                params.put("host_info", host_info);
                String paramsTemp = JSON.toJSONString(params);
                String apiGateWay = cmdbUrl+"/api/c/compapi/v2/cc/add_host_to_resource/";
                String res = "";
                try {
                    res = HttpClientUtil.sendHttpPost(apiGateWay, paramsTemp, map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(res);
                host_info.clear();
            }
            i++;
        }
        params.put("host_info", host_info);
        String paramsTemp = JSON.toJSONString(params);
        String apiGateWay = cmdbUrl+"/api/c/compapi/v2/cc/add_host_to_resource/";
//        String a = "http://cmdb.blueking.com/api/c/compapi/v2/cc/add_host_to_resource/";
        String res = "";
        try {
            res = HttpClientUtil.sendHttpPost(apiGateWay, paramsTemp, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(res);
        return getResposeBool(res);
    }

    /**
     * 根据机器查询ic
     * @param ip
     * @return
     */
    private Integer search_host(String ip) {
        Map<String, String> map = new HashMap<>();
        map.put("X-CSRFToken", bkToken.getBklogin_csrftoken());
        map.put("Cookie", "bk_token=" + bkToken.getBk_token() + ";" + "bklogin_csrftoken=" + bkToken.getBklogin_csrftoken());
        Map<String, Object> params = new HashMap<>();
        params.put("bk_app_code", "zork-nodeman");
        params.put("bk_app_secret", appSecret);
        params.put("bk_token", bkToken.getBk_token());
        params.put("bk_supplier_id", 0);
        Map<String, Object> ipmap = new HashMap<>();
        List<String> data = new ArrayList<>();
        data.add(ip);
        ipmap.put("data", data);
        ipmap.put("exact", 1);
        ipmap.put("flag", "bk_host_innerip");
        params.put("ip", ipmap);
        List<String> fieldsList = new ArrayList<>();
        fieldsList.add("bk_host_id");
//        fieldsList.add("bk_host_name");
        params.put("fields", fieldsList);
//        Map<String, String> condition = new HashMap<>();
//        condition.put("bk_set_name", setName);
//        params.put("condition", condition);
        Map<String, Object> page = new HashMap<>();
        page.put("start", 0);
        page.put("limit", 10);
        page.put("sort", "");
        params.put("page", page);
        String paramsTemp = JSON.toJSONString(params);
        String apiGateWay = cmdbUrl+"/api/c/compapi/v2/cc/search_host/";
        String res = "";
        try {
            res = HttpClientUtil.sendHttpPost(apiGateWay, paramsTemp, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getResposeComputerofId(res);
    }

    private void addHostToModule(Integer bizID,Integer moduleID,Integer hostID){
        Map<String, String> map = new HashMap<>();
        map.put("X-CSRFToken", bkToken.getBklogin_csrftoken());
        map.put("Cookie", "bk_token=" + bkToken.getBk_token() + ";" + "bklogin_csrftoken=" + bkToken.getBklogin_csrftoken());
        Map<String, Object> params = new HashMap<>();
        params.put("bk_app_code", "zork-nodeman");
        params.put("bk_app_secret", appSecret);
        params.put("bk_token", bkToken.getBk_token());
        params.put("bk_supplier_id", 0);
        params.put("bk_biz_id",bizID);
        List<Integer> hostIds = new ArrayList<>();
        hostIds.add(hostID);
        params.put("bk_host_id",hostIds);
        List<Integer> moduleIds = new ArrayList<>();
        moduleIds.add(moduleID);
        params.put("bk_module_id",moduleIds);
        params.put("is_increment",true);
        String paramsTemp = JSON.toJSONString(params);
        String apiGateWay = cmdbUrl+"/api/c/compapi/v2/cc/transfer_host_module/";
        String res = "";
        try {
            res = HttpClientUtil.sendHttpPost(apiGateWay, paramsTemp, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addHostToBiz(Integer bizID,Integer hostID){
        Map<String, String> map = new HashMap<>();
        map.put("X-CSRFToken", bkToken.getBklogin_csrftoken());
        map.put("Cookie", "bk_token=" + bkToken.getBk_token() + ";" + "bklogin_csrftoken=" + bkToken.getBklogin_csrftoken());
        Map<String, Object> params = new HashMap<>();
        params.put("bk_app_code", "zork-nodeman");
        params.put("bk_app_secret", appSecret);
        params.put("bk_token", bkToken.getBk_token());
        params.put("bk_supplier_id", 0);
        params.put("bk_biz_id",bizID);
        List<Integer> hostIds = new ArrayList<>();
        hostIds.add(hostID);
        params.put("bk_host_id",hostIds);
        String paramsTemp = JSON.toJSONString(params);
        String apiGateWay = cmdbUrl+"/api/c/compapi/v2/cc/transfer_resourcehost_to_idlemodule/";
        String res = "";
        try {
            res = HttpClientUtil.sendHttpPost(apiGateWay, paramsTemp, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}