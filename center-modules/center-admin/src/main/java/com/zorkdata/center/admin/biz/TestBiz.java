package com.zorkdata.center.admin.biz;

import com.zorkdata.center.admin.entity.Test;
import com.zorkdata.center.admin.mapper.TestMapper;
import com.zorkdata.center.common.biz.BaseBiz;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/16 10:52
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TestBiz extends BaseBiz<TestMapper, Test> {
    @Override
    public void insertSelective(Test entity) {
        String password = new BCryptPasswordEncoder(12).encode(entity.getPassword());
        entity.setPassword(password);
        super.insertSelective(entity);
    }

    public Test getUserByUsername(String username) {
        Test test = new Test();
        test.setUsername(username);
        return mapper.selectOne(test);
    }
}
