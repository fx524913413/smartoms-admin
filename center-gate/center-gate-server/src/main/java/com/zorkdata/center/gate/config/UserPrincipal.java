package com.zorkdata.center.gate.config;

import com.zorkdata.center.auth.client.config.UserAuthConfig;
import com.zorkdata.center.auth.client.jwt.UserAuthUtil;
import com.zorkdata.center.auth.common.util.jwt.IJWTInfo;
import com.zorkdata.center.gate.ratelimit.config.IUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/23 9:09
 */
public class UserPrincipal implements IUserPrincipal {

    @Autowired
    private UserAuthConfig userAuthConfig;
    @Autowired
    private UserAuthUtil userAuthUtil;

    @Override
    public String getName(HttpServletRequest request) {
        IJWTInfo infoFromToken = getJwtInfo(request);
        return infoFromToken == null ? null : infoFromToken.getUniqueName();
    }

    private IJWTInfo getJwtInfo(HttpServletRequest request) {
        IJWTInfo infoFromToken = null;
        try {
            String authToken = request.getHeader(userAuthConfig.getTokenHeader());
            if (StringUtils.isEmpty(authToken)) {
                infoFromToken = null;
            } else {
                infoFromToken = userAuthUtil.getInfoFromToken(authToken);
            }
        } catch (Exception e) {
            infoFromToken = null;
        }
        return infoFromToken;
    }

}
