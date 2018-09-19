package com.zorkdata.center.admin.rest;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zorkdata.center.admin.config.AgentVersionConfiguration;
import com.zorkdata.center.admin.feign.BizPCService;
import com.zorkdata.center.admin.rpc.service.Ifc.AppProgramServiceIfc;
import com.zorkdata.center.admin.vo.AppProgramVo;
import com.zorkdata.center.admin.vo.AppSystemInfoVo;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("appprogram")
public class AppProgramController {
    private static final String VERSION2_2 = "2.2";
    private static final String VERSION3_0 = "3.0";
    @Autowired
    private BizPCService bizPCService;
    @Autowired
    private AgentVersionConfiguration avc;

    @Autowired
    private AppProgramServiceIfc appProgramServiceIfc;

    @ApiOperation(value = "获取业务集群模块机器信息", notes = "获取该用户所有的业务集群模块机器信息", produces = "application/json", consumes = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名字", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String" , paramType = "body"),
            @ApiImplicitParam(name = "systemId", value = "业务Id", required = true, dataType = "int", paramType = "body")
    })
    @RequestMapping(value = "/getAppProgramBySys", method = RequestMethod.POST)
    @ResponseBody
    public RespModel getAppProgramBySys(@RequestParam Map<String,String> map) {
        String version = avc.getVersion();
        String systemId = null;
        if (map.containsKey("systemId")) {
            systemId = String.valueOf(map.get("systemId"));
        } else {
            return RespTools.getRespMsgModel(CodeTable.FAILED, "参数错误,没有systemid");
        }
        if (VERSION2_2.equals(version)) {
            String appProgramInfo = bizPCService.getAppProgramInfo(systemId);
            JSONObject jsonObj  = JSON.parseObject(appProgramInfo);
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, jsonObj);
        } else {
            String userName = String.valueOf(map.get("userName"));
            String token = String.valueOf(map.get("token"));
            Map<String, List<AppProgramVo>> appProgramMap = new HashMap<>();
            try {
                appProgramMap = appProgramServiceIfc.getAppProgramVoV3(systemId, userName, token);
            } catch (Exception e) {
                return RespTools.getRespMsgModel(CodeTable.FAILED, e.getMessage());
            }
            if (VERSION3_0.equals(version)) {
                return RespTools.getRespMsgModel(CodeTable.SUCCESS, appProgramMap);
            } else {
                String appProgramInfo = bizPCService.getAppProgramInfo(systemId);
                Map<String, Object> appProgramMap1 = appProgramServiceIfc.getAppProgramAllVersion(appProgramInfo, appProgramMap.get(VERSION3_0));
                return RespTools.getRespMsgModel(CodeTable.SUCCESS, appProgramMap1);
            }
        }
    }
}
