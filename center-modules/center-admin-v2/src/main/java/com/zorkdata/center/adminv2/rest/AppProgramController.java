package com.zorkdata.center.adminv2.rest;


import com.alibaba.fastjson.JSON;
import com.zorkdata.center.adminv2.biz.AppProgramBiz;
import com.zorkdata.center.adminv2.vo.AppProgramVo;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("appprogram")
public class AppProgramController {
    @Autowired
    private AppProgramBiz appProgramBiz;

    @RequestMapping(value = "/getAppProgramInfo", method = RequestMethod.POST)
    @ResponseBody
    public String getAppProgramInfo(String systemid) {
        HashMap<String, List<AppProgramVo>> map1 = new HashMap<>();
        Map<String,Object> map = new HashMap();
        List<Integer> systemIds = new ArrayList<>();
        systemIds.add(Integer.parseInt(systemid));
        map.put("systemid",systemIds);
        List<AppProgramVo> appSystemInfoVos = appProgramBiz.getSystemInfo(map);
        map1.put("2.2", appSystemInfoVos);
        return JSON.toJSONString(map1);
    }
}
