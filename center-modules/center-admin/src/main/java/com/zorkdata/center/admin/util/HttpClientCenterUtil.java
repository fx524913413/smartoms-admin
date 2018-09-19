package com.zorkdata.center.admin.util;

import com.alibaba.fastjson.JSON;
import com.zorkdata.center.admin.config.FileConfiguration;
import com.zorkdata.center.admin.rpc.service.Impl.TokenCache;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * author：xch
 * date:2018/4/27
 * HTTPclient请求中台的接口
 */
public class HttpClientCenterUtil {
    public static String sendPost(String url, List<String> targets) {
//        HttpClient client=new HttpClient()
        return "";
    }

    /**
     * @author: xch
     * @date: 2018/4/27 15:38
     * @param: [url, targets]
     * @return: java.lang.String
     * @description:
     */
    public static Map<String, List<String>> getAgentsByTargets(List<String> targets) {
        Map<String, List<String>> agentList = new HashMap<>(16);

        HttpClient httpClient = HttpClients.createDefault();
        String URL = "http://192.168.1.93:8765/api/admin/agent/getAgents";
        String token = TokenCache.getToken();

        HttpPost httpPost = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("ipList", JSON.toJSONString(targets)));
        try {
            httpPost.setHeader("Accept", "application/json, text/plain, */*");
            httpPost.setHeader("Cache-Control", "no-cache");
            httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
            httpPost.setHeader("Authorization", token);
            httpPost.setEntity(new StringEntity(JSON.toJSONString(targets)));
            HttpResponse response = httpClient.execute(httpPost);
            String strResult = "";
            if (response.getStatusLine().getStatusCode() == 200) {
                strResult = EntityUtils.toString(response.getEntity());
                Map<String, Object> mapTemp = JSON.parseObject(strResult);
                List<Map<String, Object>> result = (List<Map<String, Object>>) mapTemp.get("data");
                result.forEach(r -> {
                    String api = r.get("saltApi").toString();
                    List<Map<String, String>> agentTemp = (List<Map<String, String>>) r.get("agentList");
                    List<String> agents = new ArrayList<>();
                    agentTemp.forEach(agent -> {
                        agents.add(agent.get("name").toString());
                    });
                    agentList.put(api, agents);

                });
                System.out.println("resule=" + strResult);
            } else {
                System.out.println("ERROR");
            }
        } catch (Exception e) {
            e.printStackTrace();
//            return "post failure :caused by-->" + e.getMessage().toString();
        }
        return agentList;
    }


    //    public static Map<String,List<String>> getAgentsByTargets(List<String> targets){
//        HttpClient httpClient = HttpClients.createDefault();
//
//        PostMethod post = new PostMethod("http://127.0.0.1:8080/sms/SendSms?access_code=123");
//        post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"utf-8");
//
//
//        String sTelContent = "{'tel':'15980920215,13696921193','content':'yebinghuai短信猫测试内容'}";
//        post.setRequestBody(sTelContent);
//
//
//        httpclient.executeMethod(post);
//        info = new String(post.getResponseBody(),"utf-8");
//        System.out.println(info);
//
//
//    }
    public static Object httpClientGet() {
        HttpClient httpClient = HttpClients.createDefault();
//        String computerModuleIDUrl=;
        HttpGet httpGet = new HttpGet("http://192.168.1.245:8000/webapi/modules");
        Object data = new Object();
        try {
            //设置请求头
            httpGet.setHeader("Accept", "application/json, text/plain, */*");
            httpGet.setHeader("Cache-Control", "no-cache");
            httpGet.setHeader("Content-Type", "application/json;charset=utf-8");
            HttpResponse response = httpClient.execute(httpGet);
            String strResult = "";
            if (response.getStatusLine().getStatusCode() == 200) {
                strResult = EntityUtils.toString(response.getEntity());
                Map<String, Object> mapTemp = JSON.parseObject(strResult);
                if ("000000".equals(mapTemp.get("code"))) {
                    data = mapTemp.get("data");
                    System.out.println("1111");
                } else {
                    System.out.println("请求中台失败！");
                    throw new Exception("返回状态码：" + mapTemp.get("code"));
                }
            } else {
                System.out.println("请求中台失败！");
                throw new Exception("HttpClientException:StatusCode=" + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("请求中台失败！");
            e.printStackTrace();
        }
        return data;
    }

    public static void main(String[] args) {
        httpClientGet();
//        Map<String, Object> obj = (Map<String, Object>) JSON.parse(json);
//        if (String.valueOf(obj.get("code")).equals("000000")) {
//            Map<String, Object> data = (Map<String, Object>) obj.get("data");
//            JSONArray list = (JSONArray) data.get("list");
//            for (int i = 0; i < list.size(); i++) {
//                Computer computer = new Computer();
//                JSONObject jsonObject = (JSONObject) list.get(i);
//                String cicode = String.valueOf(jsonObject.get("ciCode"));
//                String hostname = String.valueOf(jsonObject.get("名称"));
//                String ip = "[";
//                String ostype = null;
//                List<String> ipList = new ArrayList();
//                JSONArray relationshipInstance = (JSONArray) jsonObject.get("relationshipInstance");
//                for (int j = 0; j < relationshipInstance.size(); j++) {
//                    Map<String, Object> hashMap = (Map<String, Object>) relationshipInstance.get(j);
//                    if (String.valueOf(hashMap.get("selectedModule")).equals("主机") && String.valueOf(hashMap.get("ralationshipModule")).equals("IP")) {
//                        JSONArray jsonArray = (JSONArray) hashMap.get("relationshipInstance");
//                        for (int k = 0; k < jsonArray.size(); k++) {
//                            ipList.add(((Map<String, String>) jsonArray.get(k)).get("IP地址"));
//                        }
//                    }
//
//                    if (String.valueOf(hashMap.get("selectedModule")).equals("操作系统") && String.valueOf(hashMap.get("ralationshipModule")).equals("主机")) {
//                        JSONArray jsonArray = (JSONArray) hashMap.get("relationshipInstance");
//                        for (int k = 0; k < jsonArray.size(); k++) {
//                            if (((Map<String, String>) jsonArray.get(k)).get("类型").equals("windows")) {
//                                ostype = "windowscomputer";
//                            } else {
//                                ostype = "linuxcomputer";
//                            }
//                        }
//                    }
//                }
//
//                if (ipList != null && ipList.size() > 0) {
//                    for (int y = 0; y < ipList.size(); y++) {
//                        if (y == ipList.size() - 1) {
//                            ip += ipList.get(y) + "]";
//                        } else {
//                            ip += ipList.get(y) + ",";
//                        }
//                    }
//                } else {
//                    ip = null;
//                }
//                computer.setComputerType(ostype);
//                computer.setHostName(hostname);
//                computer.setIp(ip);
//                computer.setCiCode(cicode);
//                System.out.println(computer.toString());
//            }
//        }
    }
}

