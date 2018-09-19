package com.zorkdata.center.auth.client.runner;

import com.zorkdata.center.auth.client.config.ServiceAuthConfig;
import com.zorkdata.center.auth.client.config.UserAuthConfig;
import com.zorkdata.center.auth.client.feign.ServiceAuthFeign;
import com.zorkdata.center.common.msg.BaseResponse;
import com.zorkdata.center.common.msg.ObjectRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/29 16:26
 */
@Configuration
@Slf4j
public class AuthClientRunner implements CommandLineRunner {
    @Autowired
    private UserAuthConfig userAuthConfig;

    @Autowired
    private ServiceAuthConfig serviceAuthConfig;

    @Autowired
    private ServiceAuthFeign serviceAuthFeign;

    @Override
    public void run(String... args) throws Exception {
        log.info("初始化加载用户pubKey");
        try {
            refreshUserPubKey();
        } catch (Exception e) {
            log.error("初始化加载用户pubKey失败,1分钟后自动重试!", e);
        }
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void refreshUserPubKey() {
        BaseResponse resp = serviceAuthFeign.getUserPublicKey(serviceAuthConfig.getClientId(), serviceAuthConfig.getClientSecret());
        if (resp.getStatus() == HttpStatus.OK.value()) {
            ObjectRestResponse<byte[]> userResponse = (ObjectRestResponse<byte[]>) resp;
            this.userAuthConfig.setPubKeyByte(userResponse.getData());
        }
    }
}
