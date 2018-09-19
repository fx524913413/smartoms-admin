package com.zorkdata.center.admin.rpc.service;

import com.zorkdata.center.admin.biz.TestBiz;
import com.zorkdata.center.admin.entity.Test;
import com.zorkdata.center.admin.vo.TestInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/16 11:06
 */
@Service
public class PermissionService {
    @Autowired
    private TestBiz testBiz;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public TestInfo validate(String username, String password) {
        TestInfo info = new TestInfo();
        if (username == null && password == null) {
            info.setId("111");
            return info;
        } else {
            Test test = testBiz.getUserByUsername(username);
            if (encoder.matches(password, test.getPassword())) {
                BeanUtils.copyProperties(test, info);
                info.setId(test.getId().toString());
            }
            return info;
        }
    }
}
