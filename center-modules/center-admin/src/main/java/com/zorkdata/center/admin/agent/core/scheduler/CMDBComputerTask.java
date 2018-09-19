package com.zorkdata.center.admin.agent.core.scheduler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zorkdata.center.admin.biz.ComputerBiz;
import com.zorkdata.center.admin.config.CmdbConfiguration;
import com.zorkdata.center.admin.entity.Computer;
import com.zorkdata.center.admin.rpc.service.Ifc.UserServiceIfc;
import com.zorkdata.center.admin.rpc.service.Impl.UserServiceImpl;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.*;

/**
 * @author huziyue
 */
@Component
public class CMDBComputerTask implements Job {
    private static final Logger logger = LoggerFactory.getLogger(CMDBComputerTask.class);
    @Autowired
    private ComputerBiz computerBiz;

    @Autowired
    private CmdbConfiguration cmdbConfiguration;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("同步CMDB定时任务");
        long start = System.currentTimeMillis();

        String computerModuleIDUrl = cmdbConfiguration.getZorkcmdbApiServer() + "/webapi/modules";
        String pagesUrl = cmdbConfiguration.getZorkcmdbApiServer() + "/webapi/moduleInstances/";
        String computerInfoUrl = cmdbConfiguration.getZorkcmdbApiServer() + "/webapi/relationshipInstances/";
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet();
        try {
            //设置请求头
            httpGet.setURI(new URI(computerModuleIDUrl));
            httpGet.setHeader("Accept", "application/json, text/plain, */*");
            httpGet.setHeader("Cache-Control", "no-cache");
            httpGet.setHeader("Content-Type", "application/json;charset=utf-8");
            HttpResponse response = httpClient.execute(httpGet);
            String strResult = "";
            if (response.getStatusLine().getStatusCode() == 200) {
                logger.info("获取ModuleID成功");
                strResult = EntityUtils.toString(response.getEntity());
                Map<String, Object> mapTemp = JSON.parseObject(strResult);
                String computerModuleID = "";
                Integer pages = null;
                if ("000000".equals(mapTemp.get("code"))) {
                    JSONArray datalist = (JSONArray) mapTemp.get("data");
                    for (int i = 0; i < datalist.size(); i++) {
                        JSONObject jsonObject = (JSONObject) datalist.get(i);
                        if ("主机".equals(String.valueOf(jsonObject.get("moduleName")))) {
                            computerModuleID = String.valueOf(jsonObject.get("moduleID"));
                            logger.info("moduleID为：" + computerModuleID);
                            break;
                        }
                    }
                    if (computerModuleID != null && !"".equals(computerModuleID)) {
                        httpGet.setURI(new URI(pagesUrl + computerModuleID + "?pageSize=200"));
                        HttpResponse pagesResponse = httpClient.execute(httpGet);
                        if (pagesResponse.getStatusLine().getStatusCode() == 200) {
                            Map<String, Object> tempMap = JSON.parseObject(EntityUtils.toString(pagesResponse.getEntity()));
                            if ("000000".equals(tempMap.get("code"))) {
                                Map<String, Object> dataMap = JSON.parseObject(String.valueOf(tempMap.get("data")));
                                pages = Integer.parseInt(String.valueOf(dataMap.get("pages")));
                                logger.info("获取数据总页数成功！页数为：" + pages);
                            }
                        } else {
                            logger.error("获取数据总页数失败" + pagesResponse.getStatusLine().getStatusCode());
                        }
                    }
                    if (pages != null) {
                        for (int j = 1; j <= pages; j++) {
                            List<Computer> computers = new ArrayList<>();
                            httpGet.setURI(new URI(computerInfoUrl + computerModuleID + "?pageSize=200&pageNum=" + j));
                            HttpResponse computerInfoResponse = httpClient.execute(httpGet);
                            if (computerInfoResponse.getStatusLine().getStatusCode() == 200) {
                                Map<String, Object> Map = JSON.parseObject(EntityUtils.toString(computerInfoResponse.getEntity()));
                                if (String.valueOf(Map.get("code")).equals("000000")) {
                                    Map<String, Object> data = (Map<String, Object>) Map.get("data");
                                    JSONArray list = (JSONArray) data.get("list");
                                    for (int i = 0; i < list.size(); i++) {
                                        Computer computer = new Computer();
                                        JSONObject jsonObject = (JSONObject) list.get(i);
                                        String cicode = String.valueOf(jsonObject.get("ciCode"));
                                        String hostname = String.valueOf(jsonObject.get("名称"));
                                        String ip = "";
                                        String ostype = null;
                                        List<String> ipList = new ArrayList();
                                        JSONArray relationshipInstance = (JSONArray) jsonObject.get("relationshipInstance");
                                        for (int y = 0; y < relationshipInstance.size(); y++) {
                                            Map<String, Object> hashMap = (Map<String, Object>) relationshipInstance.get(y);
                                            if (String.valueOf(hashMap.get("selectedModule")).equals("主机") && String.valueOf(hashMap.get("ralationshipModule")).equals("IP")) {
                                                JSONArray jsonArray = (JSONArray) hashMap.get("relationshipInstance");
                                                for (int k = 0; k < jsonArray.size(); k++) {
                                                    ipList.add(((Map<String, String>) jsonArray.get(k)).get("IP地址"));
                                                }
                                            }

                                            if (String.valueOf(hashMap.get("selectedModule")).equals("操作系统") && String.valueOf(hashMap.get("ralationshipModule")).equals("主机")) {
                                                JSONArray jsonArray = (JSONArray) hashMap.get("relationshipInstance");
                                                for (int k = 0; k < jsonArray.size(); k++) {
                                                    if (((Map<String, String>) jsonArray.get(k)).get("类型").equals("windows")) {
                                                        ostype = "windowscomputer";
                                                    } else {
                                                        ostype = "linuxcomputer";
                                                    }
                                                }
                                            }
                                        }
                                        if (ipList != null && ipList.size() > 0) {
                                            Collections.sort(ipList);
                                            for (int y = 0; y < ipList.size(); y++) {
                                                if (y == ipList.size() - 1) {
                                                    ip += ipList.get(y);
                                                } else {
                                                    ip += ipList.get(y) + ",";
                                                }
                                            }
                                        } else {
                                            ip = null;
                                        }
                                        if (ip != null && !"".equals(ip)) {
                                            computer.setComputerType(ostype);
                                            computer.setHostName(hostname);
                                            computer.setIp(ip);
                                            computer.setCiCode(cicode);
                                            computers.add(computer);
                                        }
                                    }
                                }
                            }
                            if (computers != null && computers.size() > 0) {
                                computerBiz.updateComputerCiCodeBatch(computers);
                                computerBiz.insertBatch(computers);
                            }
                        }
                    }
                    long end = System.currentTimeMillis();
                    logger.info("一共花了" + (end - start) + "毫秒");
                } else {
                    logger.error("数据总页数为空！");
                }
            } else {
                logger.error("获取moudleID失败！HttpClientException:StatusCode=" + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            logger.error("请求cmdb失败！", e);
        }
    }
}
