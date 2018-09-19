package com.zorkdata.center.admin.rest;

import com.alibaba.fastjson.JSONObject;
import com.zorkdata.center.admin.rpc.service.Ifc.AgentServiceIfc;
import com.zorkdata.center.admin.vo.AgentInfo;
import com.zorkdata.center.common.util.PoolManager;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileControllerTest {
    public  static String downloadFile(List<String> minions, String fileName, String destPath) throws IOException {
        StringBuffer buffer = new StringBuffer();
        CloseableHttpResponse resp = null;
        HttpEntity he = null;
        String respContent = null;
        JSONObject jsonObject = new JSONObject();
        try {
            HttpPost httpPost = new HttpPost("http://192.168.30.21:8762/file/sendfile");
            CloseableHttpClient client = PoolManager.getHttpClient(200,25);
            jsonObject.put("minions",minions);
            String scrPath = "salt://smartdata/"+fileName;
            System.out.println(scrPath);
            System.out.println(destPath);
            jsonObject.put("scrPath",scrPath);
            jsonObject.put("destPath",destPath);
            httpPost.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));
            httpPost.setHeader("Accept","application/json, text/plain, */*");
            httpPost.setHeader("Cache-Control","no-cache");
            httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
            resp = client.execute(httpPost);
            if (resp.getStatusLine().getStatusCode() == 200) {
                he = resp.getEntity();
                respContent = EntityUtils.toString(he, "UTF-8");
            }
            System.out.print("返回码"+resp.getStatusLine().getStatusCode());
        } catch (Exception e) {
            System.out.print("[zorkerror]提交失败,返回码为：" + resp.getStatusLine().getStatusCode() + "数据为：" + jsonObject );
            throw e;
        } finally {
            try {
                if (he != null) {
                    EntityUtils.consume(he);
                }
                if (resp != null) {
                    resp.close();
                }
            } catch (Exception e) {
                System.out.print("[zorkerror]http finally失败");
                throw e;
            }
        }
        return respContent;
    }

    public static void main(String[] args) {
        List<String> minions = new ArrayList<>();
        String fileName = "filebeat_q75.yml";
        String destPath = "/shjtest/filebeat703.yml";
        minions.add("\"192.168.1.94\"");
        try {
            FileControllerTest.downloadFile(minions,fileName,destPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
