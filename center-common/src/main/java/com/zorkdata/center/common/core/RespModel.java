package com.zorkdata.center.common.core;

/**
 * @author zhuzhigang
 * @since 1.0
 */
public class RespModel {
    private String code;//返回数据代码0000正确返回，>=0001为发生异常
    private String msg;//正常返回时的数据
    //    private String orderNo;//orderNo
    private Object data;//正常返回时的数据
//    private String source;// 来源(第三方或缓存)
//    private String srcCode;// 数据源状态码
//    private String srcMsg;// 数据源状态描述
//    private String tradeNo;//采集端msgid


    public String getCode() {
        return code;

    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
