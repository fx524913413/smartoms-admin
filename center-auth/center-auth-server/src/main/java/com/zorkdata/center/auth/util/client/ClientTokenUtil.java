package com.zorkdata.center.auth.util.client;

import com.zorkdata.center.auth.common.util.jwt.IJWTInfo;
import com.zorkdata.center.auth.common.util.jwt.JWTHelper;
import com.zorkdata.center.auth.common.util.jwt.JWTInfo;
import com.zorkdata.center.auth.configuration.KeyConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/29 17:00
 */
@Configuration
public class ClientTokenUtil {
    private Logger logger = LoggerFactory.getLogger(ClientTokenUtil.class);

    @Value("${client.expire}")
    private int expire;
    @Autowired
    private KeyConfiguration keyConfiguration;

    public String generateToken(IJWTInfo jwtInfo) throws Exception {
        return JWTHelper.generateToken(jwtInfo, keyConfiguration.getServicePriKey(), expire);
    }

    public JWTInfo getInfoFromToken(String token) throws Exception {
        return JWTHelper.getInfoFromToken(token, keyConfiguration.getServicePubKey());
    }
}
