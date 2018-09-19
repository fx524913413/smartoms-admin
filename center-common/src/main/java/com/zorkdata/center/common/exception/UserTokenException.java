package com.zorkdata.center.common.exception;

import com.zorkdata.center.common.constant.CommonConstants;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/15 12:26
 */
public class UserTokenException extends BaseException {
    public UserTokenException(String message) {
        super(message, CommonConstants.EX_USER_INVALID_CODE);
    }
}
