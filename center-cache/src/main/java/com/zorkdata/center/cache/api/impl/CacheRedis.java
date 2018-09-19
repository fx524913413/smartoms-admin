package com.zorkdata.center.cache.api.impl;

import com.alibaba.fastjson.JSON;
import com.zorkdata.center.cache.api.CacheAPI;
import com.zorkdata.center.cache.config.RedisConfig;
import com.zorkdata.center.cache.constants.CacheConstants;
import com.zorkdata.center.cache.entity.CacheBean;
import com.zorkdata.center.cache.service.IRedisService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @description: redis缓存
 * @author: zhuzhigang
 * @create: 2018/3/19 9:31
 */
@Service
public class CacheRedis implements CacheAPI {
    @Autowired
    private RedisConfig redisConfig;

    @Autowired
    private IRedisService redisCacheService;

    @Override
    public String get(String key) {
        if (!isEnabled()) {
            return null;
        }
        if (StringUtils.isBlank(key)) {
            return null;
        }
        CacheBean cache = getCacheBean(key);
        if (cache != null) {
            if (cache.getExpireTime().getTime() > System.currentTimeMillis()) {
                return redisCacheService.get(cache.getKey());
            } else {
                redisCacheService.del(addSys(key));
                redisCacheService.del(cache.getKey());
            }
        }
        return null;
    }

    @Override
    public void set(String key, Object value, int expireSec) {
        set(key, value, expireSec, "");
    }

    @Override
    public Long remove(String key) {
        if (!isEnabled()) {
            return 0L;
        }
        if (StringUtils.isBlank(key)) {
            return 0L;
        }
        try {
            CacheBean cache = getCacheBean(key);
            if (cache != null) {
                redisCacheService.del(addSys(key));
                redisCacheService.del(cache.getKey());
            }
        } catch (Exception e) {
            return 0L;
        }
        return 1L;
    }

    @Override
    public Long remove(String... keys) {
        if (!isEnabled()) {
            return null;
        }
        try {
            for (int i = 0; i < keys.length; i++) {
                remove(keys[i]);
            }
        } catch (Exception e) {
            return 0L;
        }
        return 1L;
    }

    @Override
    public Long removeByPre(String pre) {
        if (!isEnabled()) {
            return 0L;
        }
        if (StringUtils.isBlank(pre)) {
            return 0L;
        }
        try {
            Set<String> result = redisCacheService.getByPre(addSys(pre));
            List<String> list = new ArrayList<String>();
            for (String key : result) {
                CacheBean cache = getCacheBean(key);
                list.add(cache.getKey());
            }
            redisCacheService.del(list.toArray(new String[]{}));
            redisCacheService.delPre(addSys(pre));
        } catch (Exception e) {
            return 0L;
        }
        return 1L;
    }

    private CacheBean getCacheBean(String key) {
        key = this.addSys(key);
        CacheBean cache = null;
        try {
            cache = JSON.parseObject(redisCacheService.get(key),
                    CacheBean.class);
        } catch (Exception e) {
            cache = new CacheBean();
            cache.setKey(key);
            cache.setExpireTime(redisCacheService.getExpireDate(key));
        }
        return cache;
    }

    /**
     * 加入系统前缀
     *
     * @param key
     * @return
     * @author Ace
     * @date 2017年5月4日
     */
    @Override
    public String addSys(String key) {
        String result = key;
        String sys = redisConfig.getSysName();
        if (key.startsWith(sys)) {
            result = key;
        } else {
            result = sys + ":" + key;
        }
        return result;
    }

    @Override
    public void set(String key, Object value, int expireSec, String desc) {
        if (StringUtils.isBlank(key) || value == null
                || StringUtils.isBlank(value.toString())) {
            return;
        }
        if (!isEnabled()) {
            return;
        }
        String realValue = "";
        if (value instanceof String) {
            realValue = value.toString();
        } else {
            realValue = JSON.toJSONString(value, false);
        }
        String realKey = CacheConstants.PRE + addSys(key);
        // redisCacheService.getExpireDate(realKey)
        Date time = new DateTime().plusSeconds(expireSec).toDate();
        CacheBean cache = new CacheBean(realKey, desc, time);
        String result = JSON.toJSONString(cache, false);
        redisCacheService.set(addSys(key), result, expireSec);
        redisCacheService.set(realKey, realValue, expireSec);
    }

    @Override
    public List<CacheBean> listAll() {
        Set<String> result = redisCacheService.getByPre(addSys(""));
        List<CacheBean> caches = new ArrayList<CacheBean>();
        if (result == null) {
            return caches;
        }
        Iterator<String> it = result.iterator();
        String key = "";
        CacheBean cache = null;
        while (it.hasNext()) {
            cache = null;
            key = it.next();
            try {
                cache = JSON.parseObject(redisCacheService.get(key),
                        CacheBean.class);
            } catch (Exception e) {
                cache = new CacheBean();
                cache.setKey(key);
                cache.setExpireTime(redisCacheService.getExpireDate(key));
            }
            if (cache == null) {
                continue;
            }
            cache.setKey(key);
            caches.add(cache);
        }
        return caches;
    }

    @Override
    public List<CacheBean> getCacheBeanByPre(String pre) {
        if (StringUtils.isBlank(pre)) {
            return new ArrayList<CacheBean>();
        }
        Set<String> result = redisCacheService.getByPre(pre);
        Iterator<String> it = result.iterator();
        List<CacheBean> caches = new ArrayList<CacheBean>();
        String key = "";
        CacheBean cache = null;
        while (it.hasNext()) {
            key = it.next();
            try {
                cache = JSON.parseObject(redisCacheService.get(key),
                        CacheBean.class);
            } catch (Exception e) {
                cache = new CacheBean();
                cache.setKey(key);
                cache.setExpireTime(redisCacheService.getExpireDate(key));
            }
            cache.setKey(key);
            caches.add(cache);
        }
        return caches;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.parseBoolean(redisConfig.getEnable());
    }
}
