package com.zorkdata.center.adminv2.rest;

import com.zorkdata.center.adminv2.Configuration.BeatsConfiguration;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import com.zorkdata.center.common.util.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/6/5 16:02
 */
@RestController
@RequestMapping("beats")
public class BeatsController {
    @Autowired
    BeatsConfiguration beatsConfiguration;

    @RequestMapping(value = "/stopservice", method = RequestMethod.POST)
    @ResponseBody
    public RespModel stopService(String typeName, String worknodeid) {
        Map<String, String> param = new HashMap<>();
        param.put("typeName", typeName);
        param.put("worknodeid", worknodeid);
        String result = null;
        try {
            result = HttpClientUtil.sendHttpPost(beatsConfiguration.getBizpcurl() + "/beatservice/stopservice", param);
        } catch (Exception e) {
            e.printStackTrace();
            return RespTools.getRespMsgModel(CodeTable.FAILED, result);
        }
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, result);
    }

    @RequestMapping(value = "/sendfile", method = RequestMethod.POST)
    @ResponseBody
    public RespModel sendFile(String filePath,  String worknodeid) throws FileNotFoundException {
        File file = new File(filePath);
        Map<String, Object> param = new HashMap<>();
        param.put("worknodeid", worknodeid);
        Map<String, File> fileLists = new HashMap<>();
        fileLists.put("file", file);
        String result = null;
        try {
            result = HttpClientUtil.sendHttpPost(beatsConfiguration.getBizpcurl() + "/beatservice/sendfile", param, fileLists);
        } catch (Exception e) {
            e.printStackTrace();
            return RespTools.getRespMsgModel(CodeTable.FAILED, result);
        }
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, result);
    }

    @RequestMapping(value = "/sendexec", method = RequestMethod.POST)
    @ResponseBody
    public RespModel sendExec(String filePath, String worknodeid) {
        File file = new File(filePath);
        Map<String, Object> param = new HashMap<>();
        param.put("worknodeid", worknodeid);
        Map<String, File> fileLists = new HashMap<>();
        fileLists.put("file", file);
        String result = null;
        try {
            result = HttpClientUtil.sendHttpPost(beatsConfiguration.getBizpcurl() + "/beatservice/sendexec", param, fileLists);
        } catch (Exception e) {
            e.printStackTrace();
            return RespTools.getRespMsgModel(CodeTable.FAILED, result);
        }
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, result);
    }

    @RequestMapping(value = "/sendyml", method = RequestMethod.POST)
    @ResponseBody
    public RespModel sendYml(String filePath, String worknodeid) {
        File file = new File(filePath);
        Map<String, Object> param = new HashMap<>();
        param.put("worknodeid", worknodeid);
        Map<String, File> fileLists = new HashMap<>();
        fileLists.put("file", file);
        String result = null;
        try {
            result = HttpClientUtil.sendHttpPost(beatsConfiguration.getBizpcurl() + "/beatservice/sendyml", param, fileLists);
        } catch (Exception e) {
            e.printStackTrace();
            return RespTools.getRespMsgModel(CodeTable.FAILED, result);
        }
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, result);
    }
}
