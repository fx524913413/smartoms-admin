package com.zorkdata.center.adminv2.rest;

import com.alibaba.fastjson.JSON;
import com.zorkdata.center.adminv2.biz.AppSystemBiz;
import com.zorkdata.center.adminv2.vo.AppClusterVo;
import com.zorkdata.center.adminv2.vo.AppProgramVo;
import com.zorkdata.center.adminv2.vo.AppSystemInfoVo;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/6/5 14:53
 */
@RestController
@RequestMapping("appsystem")
public class AppSystemController {
    @Autowired
    private AppSystemBiz appSystemBiz;

    @RequestMapping(value = "/getAppSystemInfo", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getAppSystemInfo() {
        HashMap<String, List<AppSystemInfoVo>> map = new HashMap<>();
        List<AppSystemInfoVo> appSystemInfoVos = appSystemBiz.getSystemInfo();
        map.put("2.2",appSystemInfoVos);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, map);
    }


    @RequestMapping(value = "/getAppSystemInfoByUserName", method = RequestMethod.POST)
    @ResponseBody
    public String getAppSystemInfoByUserName(@RequestParam String userName) {
        HashMap<String, List<AppSystemInfoVo>> map = new HashMap<>();
        List<AppSystemInfoVo> appSystemInfoVos = appSystemBiz.getSystemInfoByUserName(userName);
        map.put("2.2",appSystemInfoVos);
        return JSON.toJSONString(map);
    }

    @RequestMapping(value = "/getClusterBySys", method = RequestMethod.POST)
    @ResponseBody
    public String getClusterBySys(@RequestParam String systemId) {
        HashMap<String, List<AppClusterVo>> map1 = new HashMap<>();
        Map<String,Object> map = new HashMap();
        List<Integer> systemIds = new ArrayList<>();
        systemIds.add(Integer.parseInt(systemId));
        map.put("systemId",systemIds);
        List<AppClusterVo> appSystemInfoVos = appSystemBiz.getClusterBySysInfo(map);
        map1.put("2.2", appSystemInfoVos);
        return JSON.toJSONString(map1);
    }
}
