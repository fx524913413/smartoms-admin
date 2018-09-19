package com.zorkdata.center.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zorkdata.center.auth.common.util.jwt.IJWTInfo;
import com.zorkdata.center.auth.common.util.jwt.JWTInfo;
import com.zorkdata.center.auth.configuration.TokenConfig;
import com.zorkdata.center.auth.feign.IUserService;
import com.zorkdata.center.auth.service.AuthService;
import com.zorkdata.center.auth.util.user.JwtTokenUtil;
import com.zorkdata.center.auth.vo.UserInfo;
import com.zorkdata.center.cache.annotation.Cache;
import com.zorkdata.center.cache.service.IRedisService;
import com.zorkdata.center.common.core.RespModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.util.HashSet;
import java.util.Map;

import static java.awt.SystemColor.info;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/20 15:24
 */
@Service
public class AuthServiceImpl implements AuthService {
    private JwtTokenUtil jwtTokenUtil;
    private IUserService userService;
    @Autowired
    private IRedisService iRedisService;
    @Autowired
    private TokenConfig tokenConfig;

    @Autowired
    public AuthServiceImpl(JwtTokenUtil jwtTokenUtil, IUserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @Override
    public String login(String username, String password, String productCode) throws Exception {
        // 远程调用接口获取用户
        RespModel respModel = userService.validate(username, password, productCode);
        if ("000100".equals(respModel.getCode())) {
            throw new Exception("用户不存在");
        } else if ("000109".equals(respModel.getCode())) {
            throw new Exception("当前产品内不存在此用户");
        }
        if (respModel != null && respModel.getData() != null) {
            String userstr = JSON.toJSONString((Map) respModel.getData());
            UserInfo info = JSON.parseObject(userstr, UserInfo.class);
            String token = null;
            if (info != null) {
                if (!StringUtils.isEmpty(info.getUserId())) {
                    token = jwtTokenUtil.generateToken(new JWTInfo(info.getUserName(), info.getUserId() + "", info.getTrueName()));
                    iRedisService.set("user:token:" + token, JSON.toJSONString(jwtTokenUtil.getInfoFromToken(token)), tokenConfig.getExpire());
                }
            }
            return token;
        }
        return null;
    }

    @Override
    public JWTInfo validate(String token) throws Exception {
//        JWTInfo infoFromToken = jwtTokenUtil.getInfoFromToken(token);
        String str = iRedisService.get("user:token:" + token);
        if (str != null) {
            JWTInfo jwtInfo = JSON.parseObject(str, new TypeReference<JWTInfo>() {
            });
            return jwtInfo;
        }
        return null;
    }

    @Override
    public Boolean invalid(String token) {
        // TODO: 2018/3/20 注销token
        return null;
    }

    @Override
    public RespModel get_all_user() {
        return userService.getAllUser();
    }

    @Override
    public String refresh(String oldToken) {
        // TODO: 2018/3/20 刷新token
        return null;
    }

}
