package com.zorkdata.center.cache.service;

import com.zorkdata.center.cache.entity.CacheBean;
import com.zorkdata.center.cache.vo.CacheTree;

import java.util.List;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/19 9:34
 */
public interface ICacheManager {
    public void removeAll();

    public void remove(String key);

    public void remove(List<CacheBean> caches);

    public void removeByPre(String pre);

    public List<CacheTree> getAll();

    public List<CacheTree> getByPre(String pre);

    public void update(String key, int hour);

    public void update(List<CacheBean> caches, int hour);

    public String get(String key);
}
