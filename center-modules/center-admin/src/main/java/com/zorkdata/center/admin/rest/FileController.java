package com.zorkdata.center.admin.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.zorkdata.center.admin.rpc.service.Ifc.FileServiceIfc;
import com.zorkdata.center.admin.rpc.service.Ifc.GroupServiceIfc;
import com.zorkdata.center.admin.rpc.service.Impl.FileServiceImpl;
import com.zorkdata.center.admin.util.ResultInfo;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/27 15:43
 */
@RestController
@RequestMapping("file")
public class FileController {
    @Autowired
    private FileServiceIfc fileServiceIfc;

    @RequestMapping(value = "/download/{filename}", method = RequestMethod.GET)
    public void downloadFile(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                             @PathVariable(value = "filename") String filename) throws Exception {
        fileServiceIfc.downloadFile(request, response, session, filename);
    }

//    @RequestMapping(value = "/sendfile", method = RequestMethod.POST)
//    @ResponseBody
//    public RespModel sendfile(List<String> minions,String scrPath,String destPath)throws Exception {
//        Map<String, Object> map = new HashMap<>();
//        map.put("scrPath",scrPath);
//        map.put("destPath",destPath);
//        map.put("minions",minions);
//        return RespTools.getRespMsgModel(CodeTable.SUCCESS, fileServiceIfc.syncSendFile(map));
//    }

    /**
     *
     * @param map：
     * scrPath Git服务器上文件地址
     * destPath 目标Agent储存文件地址
     * minions AgentID List 例子：["192.168.1.94", "192.168.1.96", "192.168.1.95"]
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sendfile", method = RequestMethod.POST)
    @ResponseBody
    public RespModel sendfile(@RequestBody Map map)throws Exception {
        Map<String, Object> stringStringMap = new HashMap<>();
        String scrPath = String.valueOf(map.get("scrPath"));
        String destPath = String.valueOf(map.get("destPath"));
        List<String> minions = JSON.parseArray(String.valueOf(map.get("minions")), String.class);
        stringStringMap.put("scrPath",scrPath);
        stringStringMap.put("destPath",destPath);
        stringStringMap.put("minions",minions);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, fileServiceIfc.syncSendFile4AnyLocation(stringStringMap));
    }

    /**
     * 快速文件分发
     * @param file 文件实例
     * @param isOverride 是否覆盖
     * @param name 文件名称
     * @param folder 创建者
     * @return 返回上传文件的结果
     */
    @ResponseBody
    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    public RespModel fileUpload( MultipartFile file, String isOverride, String name, String folder) {
        ResultInfo resultInfo =  fileServiceIfc.fastFileUpload(file, isOverride, name, folder);
        return RespTools.getRespMsgModel(resultInfo.getCode(), resultInfo);
    }


    //postman测试用接口
    @RequestMapping(value = "/sendfile2", method = RequestMethod.GET)
    @ResponseBody
    public RespModel sendfile()throws Exception {
        List<String> minions = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
//        String minion1 = "192.168.1.96";
//        String minion3 = "192.168.1.95";
        String minion4 = "192.168.1.94";
//        String minion5 = "192.168.1.92";
//        String minion6 = "192.168.1.97";
//        String minion7 = "192.168.1.98";
//        String minion8 = "192.168.1.99";
//        minions.add(minion1);
//        minions.add(minion3);
        minions.add(minion4);
//        minions.add(minion5);
//        minions.add(minion6);
//        minions.add(minion7);
//        minions.add(minion8);
        map.put("scrPath","salt://beats/192.168.1.94/filebeat.yml");
        map.put("destPath","/filebeat_q75.yml");
        map.put("minions",minions);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, fileServiceIfc.syncSendFile(map));
    }
}
