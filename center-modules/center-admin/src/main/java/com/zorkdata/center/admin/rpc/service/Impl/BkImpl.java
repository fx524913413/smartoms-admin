package com.zorkdata.center.admin.rpc.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.zorkdata.center.admin.config.CmdbConfiguration;
import com.zorkdata.center.admin.vo.AppSystemInfoVo;
import com.zorkdata.center.admin.vo.WorkerNode;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.BkReturnModel;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import com.zorkdata.center.common.util.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 蓝鲸api帮助类
 * @author: zhuzhigang
 * @create: 2018/6/14 17:34
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BkImpl {
    private static final String TRUE = "true";
    private static final String BK_INST_NAME = "bk_inst_name";
    private static final String USERNAME = "username";
    private static final String BK_USER = "BK_User";
    private static final String HTTP_BLUEKING_SUPPLIER_ID = "HTTP_BLUEKING_SUPPLIER_ID";
    private static final String TOKEN = "token";
    private static final String COOKIE = "Cookie";
    private static final String BK_TOKEN = "bk_token=";
    private static String VERSION = "v3";
    public static final String VALUE0 = "0";
    private static String bk_supplier_account = VALUE0;

    @Autowired
    private CmdbConfiguration cmdbConfiguration;


    /**
     * 添加实例
     * data 字段说明
     * bk_inst_id	int	新增数据记录的ID
     *
     * @param bk_obj_id           模型ID
     * @param bk_supplier_account 开发商账号
     * @param bk_inst_name        实例名
     */
    public RespModel createInst(String bk_obj_id, String bk_supplier_account, String bk_inst_name, Map<String, Object> param) throws Exception {
        if (bk_supplier_account == null) {
            bk_supplier_account = BkImpl.bk_supplier_account;
        }
        Map map = new HashMap<>();
        map.put(BK_INST_NAME, bk_inst_name);
        if (param != null) {
            map.putAll(param);
        }
        //String url = cmdbConfiguration.getApiServer() + "/api/" + VERSION + "/inst/" + bk_supplier_account + "/" + bk_obj_id;
        String url = "http://192.168.1.93:8083" + "/api/" + VERSION + "/inst/" + bk_supplier_account + "/" + bk_obj_id;
        String returnCode = HttpClientUtil.sendHttpPost(url, JSON.toJSONString(map));
        return getRespModel(returnCode);
    }

    /**
     * 添加实例
     * data 字段说明
     * bk_inst_id	int	新增数据记录的ID
     *
     * @param bk_obj_id           模型ID
     * @param bk_supplier_account 开发商账号
     * @param obj                 实例对象
     */
    public RespModel createInst(String bk_obj_id, String bk_supplier_account, Object obj) throws Exception {
        if (bk_supplier_account == null) {
            bk_supplier_account = BkImpl.bk_supplier_account;
        }
        //String url = cmdbConfiguration.getApiServer() + "/api/" + VERSION + "/inst/" + bk_supplier_account + "/" + bk_obj_id;
        String url = cmdbConfiguration.getApiServer() + "/api/" + VERSION + "/inst/" + bk_supplier_account + "/" + bk_obj_id;
        String returnCode = HttpClientUtil.sendHttpPost(url, JSON.toJSONString(obj));
        return getRespModel(returnCode);
    }

    /**
     * 查询实例
     *
     * @param bk_obj_id           模型ID
     * @param bk_supplier_account 开发商账号
     * @param param               参数
     * @return
     * @throws Exception
     */
    public RespModel searchInst(String bk_obj_id, String bk_supplier_account, Map<String, Object> param) throws Exception {
        if (bk_supplier_account == null) {
            bk_supplier_account = BkImpl.bk_supplier_account;
        }

        // API POST /api/{version}/inst/association/search/owner/{bk_supplier_account}/object/{bk_obj_id}
        //String url = cmdbConfiguration.getApiServer() + "/api/" + VERSION + "/inst/association/search/owner/" + bk_supplier_account + "/object/" + bk_obj_id;
        String url = "http://192.168.1.93:8083" + "/api/" + VERSION + "/inst/association/search/owner/" + bk_supplier_account + "/object/" + bk_obj_id;
        String returnCode = HttpClientUtil.sendHttpPost(url, JSON.toJSONString(param));
        return getRespModel(returnCode);
    }

    /**
     * 删除实例
     *
     * @param bk_obj_id           对象ID
     * @param bk_supplier_account 开发商账号
     * @param bk_inst_id          实例ID
     * @return
     * @throws Exception
     */
    public RespModel deleteInst(String bk_obj_id, String bk_supplier_account, String bk_inst_id) throws Exception {
        if (bk_supplier_account == null) {
            bk_supplier_account = BkImpl.bk_supplier_account;
        }

        // API: DELETE /api/{version}/inst/{bk_supplier_account}/{bk_obj_id}/{bk_inst_id}
        String url = cmdbConfiguration.getApiServer() + "/api/" + VERSION + "/inst/" + bk_supplier_account + "/" + bk_obj_id + "/" + bk_inst_id;
//        String url = "http://192.168.1.93:8083" + "/api/" + VERSION + "/inst/" + bk_supplier_account + "/" + bk_obj_id + "/" + bk_inst_id;
        String returnCode = HttpClientUtil.sendHttpDelete(url);
        return getRespModel(returnCode);

    }

    /**
     * 更新实例
     *
     * @param bk_obj_id
     * @param bk_supplier_account
     * @param bk_inst_id
     * @param param
     * @return
     * @throws Exception
     */
    public RespModel updateInst(String bk_obj_id, String bk_supplier_account, String bk_inst_id, String param) throws Exception {
        if (bk_supplier_account == null) {
            bk_supplier_account = BkImpl.bk_supplier_account;
        }

        // API: put http://192.168.1.93:8083/api/v3/inst/0/programinstance/6
        String url = cmdbConfiguration.getApiServer() + "/api/" + VERSION + "/inst/" + bk_supplier_account + "/" + bk_obj_id + "/" + bk_inst_id;
//        String url = "http://192.168.1.93:8083" + "/api/" + VERSION + "/inst/" + bk_supplier_account + "/" + bk_obj_id + "/" + bk_inst_id;
        String returnCode = HttpClientUtil.sendHttpPut(url, param);
        return getRespModel(returnCode);

    }

    /**
     * 创建主机实例
     *
     * @param bk_supplier_account 开发商账号
     * @param param               主机参数
     * @return
     * @throws Exception
     */
    public RespModel createHostInst(String bk_supplier_account, Map<String, Object> param) throws Exception {
        if (bk_supplier_account == null) {
            bk_supplier_account = BkImpl.bk_supplier_account;
        }
//        Map map = new HashMap<>();
//        if (param != null) {
//            map.putAll(param);
//        }
        //String url = cmdbConfiguration.getApiServer() + "/api/" + VERSION + "/hosts/add"
        String url = "http://192.168.1.93:8083" + "/api/" + VERSION + "/hosts/add";
        String s = JSON.toJSONString(param);
        String returnCode = HttpClientUtil.sendHttpPost(url, s);
        return getRespModel(returnCode);
    }

    /**
     * 查询业务实例
     *
     * @param bk_supplier_account 开发商账号
     * @param param               参数
     * @return
     * @throws Exception
     */
    public RespModel searchBizInst(String bk_supplier_account, Map<String, Object> param) throws Exception {
        if (bk_supplier_account == null) {
            bk_supplier_account = BkImpl.bk_supplier_account;
        }
        String url = cmdbConfiguration.getApiServer() + "/api/" + VERSION + "/biz/search/" + bk_supplier_account;
        Map headerMap = setHeaderAndCookieDefault(param);
        String s = JSON.toJSONString(param);
        String returnCode = HttpClientUtil.sendHttpPost(url, s, headerMap);
        return getRespModel(returnCode);
    }

    private Map<String, String> setHeaderAndCookieDefault(Map<String, Object> param) {
        Map<String, String> map = new HashMap<>();
        if (param.containsKey(USERNAME)) {
            map.put(BK_USER, String.valueOf(param.get(USERNAME)));
            param.remove(USERNAME);
        }
        map.put(HTTP_BLUEKING_SUPPLIER_ID, VALUE0);
        if (param.containsKey(TOKEN)) {
            map.put(COOKIE, BK_TOKEN + String.valueOf(param.get(TOKEN)));
            param.remove(TOKEN);
        }
        return map;
    }

    /**
     * 查询业务实例
     *
     * @param bk_supplier_account 开发商账号
     * @param param               参数
     * @return
     * @throws Exception
     */
    public RespModel searchHostInst(String bk_supplier_account, Map<String, Object> param) throws Exception {
        if (bk_supplier_account == null) {
            bk_supplier_account = BkImpl.bk_supplier_account;
        }
        String url = cmdbConfiguration.getApiServer() + "/api/" + VERSION + "/hosts/search";
        Map headerMap = setHeaderAndCookieDefault(param);
        String s = JSON.toJSONString(param);
        String returnCode = HttpClientUtil.sendHttpPost(url, s, headerMap);
        return getRespModel(returnCode);
    }

    public RespModel searchTopoInst(String bk_supplier_account, String bk_inst_id, Map<String, Object> param) throws Exception {
        if (bk_supplier_account == null) {
            bk_supplier_account = BkImpl.bk_supplier_account;
        }
        Map headerMap = setHeaderAndCookieDefault(param);
        String url = cmdbConfiguration.getApiServer() + "/api/" + VERSION + "/topo/inst/" + bk_supplier_account + "/" + bk_inst_id;
        String returnCode = HttpClientUtil.sendHttpGet(url, headerMap);
        return getRespModel(returnCode);
    }

    private RespModel getRespModel(String returnCode) {
        if (returnCode != null) {
            BkReturnModel bkReturnModel = JSON.parseObject(returnCode, new TypeReference<BkReturnModel>() {
            });
            if (bkReturnModel != null) {
                if (bkReturnModel.getResult() != null) {
                    if (bkReturnModel.getResult().equals(TRUE)) {
                        return RespTools.getRespMsgModel(CodeTable.SUCCESS, bkReturnModel.getData());
                    } else {
                        return RespTools.getRespMsgModel(CodeTable.FAILED, bkReturnModel.getBk_error_msg());
                    }
                }
            }
        }
        return RespTools.getRespMsgModel(CodeTable.FAILED, returnCode);
    }

    public static void main(String[] args) throws Exception {
        String tokenString = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ6aHV6aGlnYW5nIiwidXNlcklkIjoiNzQiLCJuYW1lIjoi5pyx5b-X5YiaIiwiZXhwIjoxNTMwMzU2MTQxfQ.GDKi2Rl-TrP6Qf6No82QUb5nKPej6GYSTtlnU0C0z1uc-GK5EtN_h_jhVSlCm_ubvLqg5teaYQ3pGjKjW451b6IUAqXUtfcipY_9ef11ffsSVEGn9E5DTp-rlPr8M_4qOqGoa5d_3np6N56foEQiJuJmS9y3XOwpLd5Jq6OZ-D0";
        HashMap<String, List<AppSystemInfoVo>> appSystemInfoMap = new HashMap<>();
        List<AppSystemInfoVo> appSystemInfoVos = new ArrayList<>();

        BkImpl bk = new BkImpl();
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> page = new HashMap<>();
        page.put("limlt", 10);
        page.put("start", 0);
        map.put("page", page);
        map.put("bk_biz_id", 6);
        map.put(USERNAME, "zhuzhigang");
        map.put(TOKEN, tokenString);
        RespModel bkHostInst = bk.searchBizInst(null, map);
        System.out.println(bkHostInst);
        if ("000000".equals(bkHostInst.getCode())) {
            JSONObject obj = JSON.parseObject(bkHostInst.getData().toString());
            JSONArray jsonArray = (JSONArray) obj.get("info");
            if (jsonArray.size() != 0) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    AppSystemInfoVo appSystemInfoVo = new AppSystemInfoVo();

                    JSONObject appSystem = (JSONObject) jsonArray.get(i);
                    Integer appSystemID = Integer.parseInt(appSystem.get("bk_biz_id").toString());
                    String appSystemName = appSystem.get("bk_biz_name").toString();
                    appSystemInfoVo.setAppSystemId(appSystemID);
                    appSystemInfoVo.setAppSystemName(appSystemName);

                    System.out.println(appSystemID + ":" + appSystemName);
                    Map<String, Object> pages = new HashMap<>();
                    Map<String, Object> map1 = new HashMap<>();
                    pages.put("limlt", 10);
                    pages.put("start", 0);
                    map1.put("page", pages);
                    map1.put("bk_biz_id", appSystemID);
                    map1.put(USERNAME, "zhuzhigang");
                    map1.put(TOKEN, tokenString);
                    RespModel hostInst = bk.searchHostInst(null, map1);
                    System.out.println(hostInst);
                    if ("000000".equals(hostInst.getCode())) {
                        JSONObject object = JSON.parseObject(hostInst.getData().toString());
                        JSONArray hostArr = (JSONArray) object.get("info");
                        if (hostArr.size() != 0) {
                            List<WorkerNode> workerNodes = new ArrayList<>();
                            for (int j = 0; j < hostArr.size(); j++) {
                                WorkerNode workerNode = new WorkerNode();

                                JSONObject temp = (JSONObject) hostArr.get(j);
                                JSONObject host = (JSONObject) temp.get("host");
                                String ip = host.get("bk_host_innerip").toString();
                                if (!ip.equals("127.0.0.1")) {
                                    System.out.println(appSystemName + "的机器ip有：" + ip);
                                    workerNode.setIp(ip);
                                    workerNodes.add(workerNode);
                                }
                            }
                            appSystemInfoVo.setWorkerNodes(workerNodes);
                        }
                    }
                    appSystemInfoVos.add(appSystemInfoVo);
                }
            }
        }
        appSystemInfoMap.put("2.2", appSystemInfoVos);
    }
}
