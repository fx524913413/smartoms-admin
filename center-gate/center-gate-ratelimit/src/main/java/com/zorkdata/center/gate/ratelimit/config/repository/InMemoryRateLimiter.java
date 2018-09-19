package com.zorkdata.center.gate.ratelimit.config.repository;

import com.zorkdata.center.gate.ratelimit.config.Rate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/16 9:32
 */
public class InMemoryRateLimiter extends AbstractRateLimiter {
    private Map<String, Rate> repository = new ConcurrentHashMap<>();

    @Override
    protected Rate getRate(String key) {
        return this.repository.get(key);
    }

    @Override
    protected void saveRate(Rate rate) {
        this.repository.put(rate.getKey(), rate);
    }
}
