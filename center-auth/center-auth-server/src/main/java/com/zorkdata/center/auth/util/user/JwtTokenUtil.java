package com.zorkdata.center.auth.util.user;

import com.zorkdata.center.auth.common.util.jwt.IJWTInfo;
import com.zorkdata.center.auth.common.util.jwt.JWTHelper;
import com.zorkdata.center.auth.common.util.jwt.JWTInfo;
import com.zorkdata.center.auth.configuration.KeyConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/20 15:26
 */
@Component
public class JwtTokenUtil {
    @Value("${jwt.expire}")
    public int expire;
    @Autowired
    private KeyConfiguration keyConfiguration;

//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;

    public String generateToken(IJWTInfo jwtInfo) throws Exception {
        return JWTHelper.generateToken(jwtInfo, keyConfiguration.getUserPriKey(), expire);
    }

    public JWTInfo getInfoFromToken(String token) throws Exception {
        return JWTHelper.getInfoFromToken(token, keyConfiguration.getUserPubKey());
    }
}
