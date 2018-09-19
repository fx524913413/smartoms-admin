package com.zorkdata.center.common.exception;

import com.zorkdata.center.common.constant.CommonConstants;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/15 12:25
 */
public class ClientTokenException extends BaseException {
    public ClientTokenException(String message) {
        super(message, CommonConstants.EX_CLIENT_INVALID_CODE);
    }
}
