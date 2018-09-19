package com.zorkdata.center.cache.test.service;

import com.zorkdata.center.cache.test.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/19 10:05
 */
public interface UserService {
    public User get(String account);

    public List<User> getLlist();

    public Set<User> getSet();

    public Map<String, User> getMap();

    public void save(User user);

    public User get(int age);
}
