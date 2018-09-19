package com.zorkdata.center.auth.client.jwt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zorkdata.center.auth.client.config.TokenConfig;
import com.zorkdata.center.auth.client.config.UserAuthConfig;
import com.zorkdata.center.auth.common.util.jwt.IJWTInfo;
import com.zorkdata.center.auth.common.util.jwt.JWTHelper;
import com.zorkdata.center.auth.common.util.jwt.JWTInfo;
import com.zorkdata.center.cache.annotation.Cache;
import com.zorkdata.center.cache.service.IRedisService;
import com.zorkdata.center.common.exception.UserTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/22 11:19
 */
@Configuration
public class UserAuthUtil {
    @Autowired
    private UserAuthConfig userAuthConfig;
    @Autowired
    private IRedisService iRedisService;
    @Autowired
    private TokenConfig tokenConfig;

    public JWTInfo getInfoFromToken(String token) throws Exception {
        try {
            String str = iRedisService.get("user:token:" + token);
            if (str != null) {
                iRedisService.set("user:token:" + token, str, tokenConfig.getExpire());
                JWTInfo jwtInfo = JSON.parseObject(str, new TypeReference<JWTInfo>() {
                });
                return jwtInfo;
            }
            return null;
        } catch (Exception e) {
            throw new Exception("get redis token error");
        }
//        catch (ExpiredJwtException ex) {
//            throw new UserTokenException("User token expired!");
//        } catch (SignatureException ex) {
//            throw new UserTokenException("User token signature error!");
//        } catch (IllegalArgumentException ex) {
//            throw new UserTokenException("User token is null or empty!");
//        }
    }
}