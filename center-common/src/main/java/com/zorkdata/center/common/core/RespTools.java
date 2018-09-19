package com.zorkdata.center.common.core;

import com.alibaba.fastjson.JSON;
import com.zorkdata.center.common.constant.CodeTable;

/**
 * @author zhuzhigang
 * @since 1.0
 */
public class RespTools {
    /**
     * 获取返回数据
     *
     * @param code 返回码
     * @return
     */
    public static String getRespMsg(String code) {
        RespModel respModel = new RespModel();
        respModel.setCode(code);
        respModel.setMsg(CodeTable.getDescription(code));
        return JSON.toJSONString(respModel);
    }

    /**
     * 获取返回数据
     *
     * @param code 返回码
     * @return
     */
    public static String getRespMsg(String code, Object data) {
        RespModel respModel = new RespModel();
        respModel.setCode(code);
        respModel.setMsg(CodeTable.getDescription(code));
        respModel.setData(data);
        return JSON.toJSONString(respModel);
    }

    public static RespModel getRespMsgModel(String code, Object data) {
        RespModel respModel = new RespModel();
        respModel.setCode(code);
        respModel.setMsg(CodeTable.getDescription(code));
        respModel.setData(data);
        return respModel;
    }

    public static BkReturnModel getBkReturnModel(String result, Object data) {
        BkReturnModel respModel = new BkReturnModel();
        respModel.setResult(result);
        respModel.setData(data);
        return respModel;
    }
}
