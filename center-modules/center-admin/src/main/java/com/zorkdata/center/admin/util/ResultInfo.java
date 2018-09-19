package com.zorkdata.center.admin.util;

import com.zorkdata.center.common.constant.CodeTable;

import java.util.HashMap;
import java.util.Map;

/**
 * 视图
 *
 * @author peichanglei
 * @date 2018/1/18 15:17
 */
public class ResultInfo {
    private String code;
    private String msg;
    private Object data;
    private Map<String, String> addition;

    public ResultInfo() {
        this.code = CodeTable.SUCCESS;
    }

    public ResultInfo(String code, String message, Object data) {
        this.code = code;
        this.msg = message;
        this.data = data;
    }

    public ResultInfo(String code, Object data) {
        this.code = code;
        this.data = data;
    }

    public ResultInfo(String code) {
        this.code = code;
        this.msg = CodeTable.getDescription(code);
    }

    public Map<String, String> getAddition() {
        return addition;
    }

    public void setAddition(Map<String, String> addition) {
        this.addition = addition;
    }

    public void addAddition(String key, String value) {
        if (this.addition == null) {
            this.addition = new HashMap<>(16);
        }
        this.addition.put(key, value);
    }

    public boolean successCode() {
        return code == CodeTable.SUCCESS;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    public ResultInfo withCode(String code) {
        this.code = code;
        return this;
    }

    public ResultInfo withMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public ResultInfo withData(Object data) {
        this.data = data;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(Object data) {
        this.data = data;
    }


}
