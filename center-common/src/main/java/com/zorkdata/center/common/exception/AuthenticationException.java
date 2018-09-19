package com.zorkdata.center.common.exception;

import com.zorkdata.center.common.constant.CodeTable;

/**
 * @author peichanglei
 * @date 2018/1/18 17:04
 */
public class AuthenticationException extends RuntimeException {
    private String code;

    public AuthenticationException(String code, String message) {
        super(message);
        this.code = code;
    }

    public AuthenticationException(String code) {
        super(CodeTable.getDescription(code));
        this.code = code;
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
