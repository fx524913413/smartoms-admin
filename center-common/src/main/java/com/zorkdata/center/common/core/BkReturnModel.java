package com.zorkdata.center.common.core;

/**
 * @author zhuzhigang
 * @date 2018/6/14
 */
public class BkReturnModel {

    private String result;
    private int bk_error_code;
    private String bk_error_msg;
    private Object data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getBk_error_code() {
        return bk_error_code;
    }

    public void setBk_error_code(int bk_error_code) {
        this.bk_error_code = bk_error_code;
    }

    public String getBk_error_msg() {
        return bk_error_msg;
    }

    public void setBk_error_msg(String bk_error_msg) {
        this.bk_error_msg = bk_error_msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
