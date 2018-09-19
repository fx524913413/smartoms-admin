package com.zorkdata.center.cache.test.unit;

import com.zorkdata.center.cache.test.CacheTest;
import com.zorkdata.center.cache.test.entity.User;
import com.zorkdata.center.cache.test.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/19 10:05
 */
@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes = CacheTest.class) // 指定我们SpringBoot工程的Application启动类
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void testGetUser() {
        userService.get("test");
        userService.get("test");
    }

    @Test
    public void testGetList() {
        userService.getLlist();
        userService.getLlist();
    }

    @Test
    public void testGetSet() {
        userService.getSet();
        userService.getSet();
    }

    @Test
    public void testGetMap() {
        userService.getMap();
        userService.getMap();
    }

    @Test
    public void testSave() {
        userService.save(new User("ace", 25, "ace"));
    }

    @Test
    public void testByKeyGenerator() {
        userService.get(28);
        userService.get(28);
    }
}
