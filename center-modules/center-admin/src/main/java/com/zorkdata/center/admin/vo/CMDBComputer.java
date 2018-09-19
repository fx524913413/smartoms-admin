package com.zorkdata.center.admin.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.zorkdata.center.admin.biz.ComputerBiz;
import com.zorkdata.center.admin.entity.Computer;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/5/25 17:32
 */
@Component
public class CMDBComputer {
    @Autowired
    private ComputerBiz computerBiz;

    public static void main(String[] args) {

    }
}


