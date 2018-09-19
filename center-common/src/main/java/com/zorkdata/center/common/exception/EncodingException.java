package com.zorkdata.center.common.exception;

import com.zorkdata.center.common.constant.CodeTable;

/**
 * @author peichanglei
 * @date 2018/1/20 16:02
 */
public class EncodingException extends RuntimeException {
    private String code;

    public EncodingException(String code, String message) {
        super(message);
        this.code = code;
    }

    public EncodingException(String code) {
        super((CodeTable.getDescription(code)));
        this.code = code;
    }

    public EncodingException(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
