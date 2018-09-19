package com.zorkdata.center.gate.ratelimit.config.repository.springdata;

import com.zorkdata.center.gate.ratelimit.config.Rate;
import com.zorkdata.center.gate.ratelimit.config.repository.AbstractRateLimiter;
import lombok.RequiredArgsConstructor;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/16 9:35
 */
@RequiredArgsConstructor
public class SpringDataRateLimiter extends AbstractRateLimiter {
    private final IRateLimiterRepository repository;

    @Override
    protected Rate getRate(String key) {
        return this.repository.findById(key).get();
    }

    @Override
    protected void saveRate(Rate rate) {
        this.repository.save(rate);
    }
}
