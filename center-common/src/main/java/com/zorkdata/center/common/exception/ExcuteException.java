package com.zorkdata.center.common.exception;

import com.zorkdata.center.common.constant.CodeTable;

/**
 * @author peichanglei
 * @date 2018/1/18 16:53
 */
public class ExcuteException extends RuntimeException {
    private String code;

    public ExcuteException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ExcuteException(String code) {
        super(CodeTable.getDescription(code));
        this.code = code;
    }

    public ExcuteException(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
