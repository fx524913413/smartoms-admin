package com.zorkdata.center.auth.service;

import com.zorkdata.center.auth.common.util.jwt.JWTInfo;
import com.zorkdata.center.common.core.RespModel;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/20 15:22
 */
public interface AuthService {
    String login(String username, String password, String produceCode) throws Exception;

    String refresh(String oldToken);

    JWTInfo validate(String token) throws Exception;

    Boolean invalid(String token);

    RespModel get_all_user();

}