package com.zorkdata.center.admin.rpc.service.Ifc;

import com.zorkdata.center.admin.vo.AppProgramVo;
import com.zorkdata.center.common.core.RespModel;

import java.util.List;
import java.util.Map;

public interface AppProgramServiceIfc {
    Map<String, List<AppProgramVo>> getAppProgramVoV3(String bk_biz_id, String userName, String tokenString) throws Exception;

    Map<String, Object> getAppProgramAllVersion(String appProgramV2, List<AppProgramVo> appProgramVoList);
}
