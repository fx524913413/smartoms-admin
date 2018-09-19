package com.zorkdata.center.cache.api;

import com.zorkdata.center.cache.entity.CacheBean;

import java.util.List;

/**
 * @description: 缓存API
 * @author: zhuzhigang
 * @create: 2018/3/19 9:30
 */
public interface CacheAPI {
    /**
     * 传入key获取缓存json，需要用fastjson转换为对象
     *
     * @param key
     * @return
     */
    public String get(String key);

    /**
     * 保存缓存
     *
     * @param key
     * @param value
     * @param expireSec
     */
    public void set(String key, Object value, int expireSec);

    /**
     * 保存缓存
     *
     * @param key
     * @param value
     * @param expireSec
     * @param desc
     */
    public void set(String key, Object value, int expireSec, String desc);

    /**
     * 移除单个缓存
     *
     * @param key
     * @return
     */
    public Long remove(String key);

    /**
     * 移除多个缓存
     *
     * @param keys
     * @return
     */
    public Long remove(String... keys);

    /**
     * 按前缀移除缓存
     *
     * @param pre
     * @return
     */
    public Long removeByPre(String pre);

    /**
     * 通过前缀获取缓存信息
     *
     * @param pre
     * @return
     */
    public List<CacheBean> getCacheBeanByPre(String pre);

    /**
     * 获取所有缓存对象信息
     *
     * @return
     */
    public List<CacheBean> listAll();

    /**
     * 是否启用缓存
     *
     * @return
     */
    public boolean isEnabled();

    /**
     * 加入系统标志缓存
     *
     * @param key
     * @return
     */
    public String addSys(String key);
}
