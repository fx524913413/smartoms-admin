package com.zorkdata.center.admin.rest;

import com.zorkdata.center.admin.rpc.service.Ifc.ExcuteCmdIfc;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import com.zorkdata.center.common.exception.AuthenticationException;
import com.zorkdata.center.common.exception.EncodingException;
import com.zorkdata.center.common.exception.ExcuteException;
import com.zorkdata.center.common.salt.netapi.results.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("cmd")
public class ExcuteCmdController {
    @Autowired
    ExcuteCmdIfc excuteCmdIfc;

    @RequestMapping(value = "/excuteCmd", method = RequestMethod.POST)
    @ResponseBody
    public RespModel excuteCmd(@RequestBody Map<String, Object> map) {
        try {
            Map<String, String> result = excuteCmdIfc.fastExcute(map);
            if (result.get("failure").length() > 2) {
                return RespTools.getRespMsgModel(CodeTable.SUCCESS_WITH_FAILURE, result);
            } else {
                return RespTools.getRespMsgModel(CodeTable.SUCCESS, result);
            }
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return RespTools.getRespMsgModel(e.getCode(), e.getMessage());
        } catch (ExcuteException e) {
            e.printStackTrace();
            return RespTools.getRespMsgModel(e.getCode(), e.getMessage());
        } catch (EncodingException e) {
            e.printStackTrace();
            return RespTools.getRespMsgModel(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return RespTools.getRespMsgModel(CodeTable.UNKNOWN_ERROR, "执行错误");
        }
    }

    @RequestMapping(value = "/asyncExecCode", method = RequestMethod.POST)
    @ResponseBody
    public RespModel asyncExecCode(@RequestBody Map<String, Object> map) {
        Map<Long, Map<String, List<String>>> result = excuteCmdIfc.asyncExecCode(map);
        if (result != null) {
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, result);
        } else {
            return RespTools.getRespMsgModel(CodeTable.UNKNOWN_ERROR, result);
        }

    }

}
