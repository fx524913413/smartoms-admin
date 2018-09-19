package com.zorkdata.center.auth.controller;

import com.zorkdata.center.auth.common.util.jwt.JWTInfo;
import com.zorkdata.center.auth.service.AuthService;
import com.zorkdata.center.auth.util.user.JwtAuthenticationRequest;
import com.zorkdata.center.auth.util.user.JwtAuthenticationResponse;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.LoginResult;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import com.zorkdata.center.common.core.UserResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/20 17:26
 */
@RestController
@RequestMapping("jwt")
public class AuthController {
    public static final String EMP_STRING = "";
    @Value("${jwt.token-header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public RespModel createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) throws Exception {
        if (authenticationRequest.getUsername() == null || authenticationRequest.getPassword() == null) {
            return RespTools.getRespMsgModel(CodeTable.ACOUNT_ERROR, null);
        }
        try {
            final String token = authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword(), authenticationRequest.getProductCode());
            if (token != null && !token.equals(EMP_STRING)) {
                return RespTools.getRespMsgModel(CodeTable.SUCCESS, new JwtAuthenticationResponse(token));
            } else {
                return RespTools.getRespMsgModel(CodeTable.ACOUNT_ERROR, null);
            }
        } catch (Exception e) {
            String message = e.getMessage();
            if ("用户不存在".equals(message)) {
                return RespTools.getRespMsgModel(CodeTable.ACOUNT_NOTEXIST, null);
            } else if ("当前产品内不存在此用户".equals(message)) {
                return RespTools.getRespMsgModel(CodeTable.NOT_IN_PRODUCT, null);
            } else {
                return RespTools.getRespMsgModel(CodeTable.UNKNOWN_ERROR, e.getMessage());
            }
        }
    }

    @RequestMapping(value = "get_user", method = RequestMethod.GET)
    public LoginResult check_url(String bk_token) throws Exception {
        try {
            JWTInfo jwtInfo = authService.validate(bk_token);
            LoginResult loginResult = new LoginResult();
            loginResult.setCode(CodeTable.SUCCESS);
            loginResult.setResult(true);
            loginResult.setMessage("ok");
            Map<String, Object> userInfo = new HashMap<>(16);
            userInfo.put("username", jwtInfo.getUniqueName());
            userInfo.put("chname", jwtInfo.getName());
            userInfo.put("phone", "");
            userInfo.put("email", "");
            if(userInfo.get("username").equals("admin") || userInfo.get("username").equals("zhuzhigang")){
                userInfo.put("role","1");
            }else {
                userInfo.put("role", "");
            }
            userInfo.put("language", "zh-cn");
            userInfo.put("owner_uin","0");
            loginResult.setData(userInfo);
            return loginResult;
        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping(value = "get_all_user", method = RequestMethod.GET)
    public UserResult bk_account_url(String bk_token) throws Exception {
        try {
            JWTInfo jwtInfo = authService.validate(bk_token);
            RespModel respModel = authService.get_all_user();
            Object object = respModel.getData();
            UserResult userResult = new UserResult();
            userResult.setCode(CodeTable.SUCCESS);
            userResult.setResult(true);
            userResult.setMessage("ok");
            List<Map<String,Object>> listUser = new ArrayList<>();
            if(object!=null){
                List<Map<String,Object>> lists = ( List<Map<String,Object>>)object;
                for(Map<String,Object> list:lists){
                    Map<String, Object> userInfo = new HashMap<>(16);
                    userInfo.put("username", list.get("userName"));
                    userInfo.put("chname", list.get("trueName"));
                    userInfo.put("phone", "");
                    userInfo.put("email", "");
                    if(userInfo.get("username").equals("admin") || userInfo.get("username").equals("zhuzhigang")){
                        userInfo.put("role","1");
                    }else {
                        userInfo.put("role", "");
                    }
                    userInfo.put("language", "zh-cn");
                    userInfo.put("owner_uin","0");
                    listUser.add(userInfo);
                }
            }
            userResult.setData(listUser);
            return userResult;
        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping(value = "refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(
            HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if (refreshedToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        }
    }

    @RequestMapping(value = "verify", method = RequestMethod.GET)
    public RespModel verify(String token) throws Exception {
        try {
            JWTInfo jwtInfo = authService.validate(token);
            if (jwtInfo == null) {
                return RespTools.getRespMsgModel(CodeTable.TOKEN_ERROR_CODE, null);
            } else {
                return RespTools.getRespMsgModel(CodeTable.SUCCESS, jwtInfo);
            }
        } catch (Exception e) {
            return RespTools.getRespMsgModel(CodeTable.FAILED, null);
        }
    }

    @RequestMapping(value = "invalid", method = RequestMethod.POST)
    public ResponseEntity<?> invalid(String token) {
        authService.invalid(token);
        return ResponseEntity.ok(true);
    }
}
