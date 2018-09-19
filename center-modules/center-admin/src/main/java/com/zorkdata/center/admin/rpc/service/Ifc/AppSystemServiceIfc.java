package com.zorkdata.center.admin.rpc.service.Ifc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zorkdata.center.admin.vo.AppClusterVo;
import com.zorkdata.center.admin.vo.AppProgramVo;
import com.zorkdata.center.admin.vo.AppSystemInfoVo;
import com.zorkdata.center.admin.vo.ComputerVo;
import com.zorkdata.center.common.core.RespModel;

import java.util.List;
import java.util.Map;

public interface AppSystemServiceIfc {
    /**
     * 获取所有的3.0agent
     * @return
     */
    Map<String,List<AppSystemInfoVo>> getNewAgents();

    Map<String, List<AppSystemInfoVo>> getAppSystemInfoVoV3(String userName, String tokenString)  throws Exception;

    Map<Integer, String> getBizIdWithIpRelation(List<AppSystemInfoVo> appSystemInfoVos);

    Map<String, Object> getAppSystemInfoAllVersion(String appSystemInfoV2, List<AppSystemInfoVo> appSystemInfoVo3V);

    Map<String, List<AppClusterVo>> getClusterBySysV3(String bk_biz_id, String userName, String tokenString) throws Exception;

    Map<String, Object> getClusterBySysAllVersion(String appProgramV2, List<AppClusterVo> appProgramVoList);
}
