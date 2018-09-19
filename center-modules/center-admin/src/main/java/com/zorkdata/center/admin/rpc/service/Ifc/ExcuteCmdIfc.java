package com.zorkdata.center.admin.rpc.service.Ifc;

import com.zorkdata.center.common.salt.netapi.results.ResultInfo;

import java.util.List;
import java.util.Map;

public interface ExcuteCmdIfc {
    Map<String, String> fastExcute(Map<String, Object> map);

    public Map<Long, Map<String, List<String>>> asyncExecCode(Map<String, Object> map);
}
