package com.zorkdata.center.admin.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.zorkdata.center.admin.config.AgentVersionConfiguration;
import com.zorkdata.center.admin.feign.BizPCService;
import com.zorkdata.center.admin.rpc.service.Ifc.AppSystemServiceIfc;
import com.zorkdata.center.admin.vo.AppClusterVo;
import com.zorkdata.center.admin.vo.AppSystemInfoVo;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("appsystem")
@Api(value = "AppSystemController", description = "AppSystem相关接口")
public class AppSystemController {
    private static final String VERSION2_2 = "2.2";
    private static final String VERSION3_0 = "3.0";
    @Autowired
    private BizPCService bizPCService;
    @Autowired
    private AgentVersionConfiguration avc;
    @Autowired
    private AppSystemServiceIfc appSystemService;

    @RequestMapping(value = "/addAppProgramInstance", method = RequestMethod.POST)
    @ResponseBody
    public RespModel addAppProgramInstance() {

        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/getAgents", method = RequestMethod.POST)
    @ResponseBody
    public RespModel getAgents() {
        String version = avc.getVersion();
        if (VERSION2_2.equals(version)) {
            return bizPCService.getOldAgents();
        } else if (VERSION3_0.equals(version)) {
            Map<String, List<AppSystemInfoVo>> newAgents = appSystemService.getNewAgents();
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, newAgents);
        } else {
            RespModel oldAgents = bizPCService.getOldAgents();
            Map<String, Object> map = JSON.parseObject(String.valueOf(oldAgents), new TypeReference<Map<String, Object>>() {
            });
            Map<String, List<AppSystemInfoVo>> newAgents = appSystemService.getNewAgents();
            map.put(VERSION3_0, newAgents.get(VERSION3_0));
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, map);
        }
    }

    @ApiOperation(value = "获取业务机器信息", notes = "获取该用户所有的业务机器信息", produces = "application/json", consumes = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名字", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String" , paramType = "body")
    })
    @RequestMapping(value = "/getAppSystemInfo", method = RequestMethod.POST)
    @ResponseBody
    public RespModel getAppSystemInfo(@RequestParam("userName") String userName, @RequestParam("token") String token) {
        String version = avc.getVersion();
        if (VERSION2_2.equals(version)) {
            String appSystemInfoV2 = bizPCService.getOldAgentsByUserName(userName);
            JSONObject jsonObj  = JSON.parseObject(appSystemInfoV2);
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, jsonObj);
        } else {
            Map<String, List<AppSystemInfoVo>> appSystemInfoMap = new HashMap<>();
            try {
                appSystemInfoMap = appSystemService.getAppSystemInfoVoV3(userName, token);
            } catch (Exception e) {
                e.printStackTrace();
                return RespTools.getRespMsgModel(CodeTable.FAILED, null);
            }
            if (VERSION3_0.equals(version)) {
                return RespTools.getRespMsgModel(CodeTable.SUCCESS, appSystemInfoMap);
            } else {
                String appSystemInfoV2 = bizPCService.getOldAgentsByUserName(userName);
                Map<String, Object> appSystemInfoMapAllVersion =
                        appSystemService.getAppSystemInfoAllVersion(appSystemInfoV2, appSystemInfoMap.get(VERSION3_0));
                return RespTools.getRespMsgModel(CodeTable.SUCCESS, appSystemInfoMapAllVersion);
            }
        }
    }

    @ApiOperation(value = "获取业务集群信息", notes = "获取用户该业务所有的集群信息", produces = "application/json", consumes = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名字", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String" , paramType = "body"),
            @ApiImplicitParam(name = "systemId", value = "业务Id", required = true, dataType = "int", paramType = "body")
    })
    @RequestMapping(value = "/getClusterBySys", method = RequestMethod.POST)
    @ResponseBody
    public RespModel getClusterBySys(@RequestParam Map<String,String> map) {
        String version = avc.getVersion();
        String systemId = null;
        if (map.containsKey("systemId")) {
            systemId = String.valueOf(map.get("systemId"));
        } else {
            return RespTools.getRespMsgModel(CodeTable.FAILED, "参数错误,没有systemid");
        }
        if (VERSION2_2.equals(version)) {
            String  ClusterBySysInfo = bizPCService.getClusterBySys(systemId);
            JSONObject jsonObj  = JSON.parseObject(ClusterBySysInfo);
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, jsonObj);
        } else {
            String userName = String.valueOf(map.get("userName"));
            String token = String.valueOf(map.get("token"));
            Map<String, List<AppClusterVo>> appClusterMap = new HashMap<>();
            try {
                appClusterMap = appSystemService.getClusterBySysV3(systemId, userName, token);
            } catch (Exception e) {
                return RespTools.getRespMsgModel(CodeTable.FAILED, e.getMessage());
            }
            if (VERSION3_0.equals(version)) {
                return RespTools.getRespMsgModel(CodeTable.SUCCESS, appClusterMap);
            } else {
                String ClusterBySysInfo = bizPCService.getClusterBySys(systemId);
                Map<String, Object> appProgramMap1 =  appSystemService.getClusterBySysAllVersion(ClusterBySysInfo, appClusterMap.get(VERSION3_0));
                return RespTools.getRespMsgModel(CodeTable.SUCCESS, appProgramMap1);
            }
        }
    }
}
