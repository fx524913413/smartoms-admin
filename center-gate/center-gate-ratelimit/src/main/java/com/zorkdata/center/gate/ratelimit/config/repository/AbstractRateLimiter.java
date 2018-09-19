package com.zorkdata.center.gate.ratelimit.config.repository;

import com.zorkdata.center.gate.ratelimit.config.Rate;
import com.zorkdata.center.gate.ratelimit.config.RateLimiter;
import com.zorkdata.center.gate.ratelimit.config.properties.RateLimitProperties;

import java.util.Date;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/16 9:13
 */
public abstract class AbstractRateLimiter implements RateLimiter {
    protected abstract Rate getRate(String key);

    protected abstract void saveRate(Rate rate);

    @Override
    public synchronized Rate consume(final RateLimitProperties.Policy policy, final String key) {
        Rate rate = this.create(policy, key);
        this.updateRate(rate);
        this.saveRate(rate);
        return rate;
    }

    private Rate create(final RateLimitProperties.Policy policy, final String key) {
        Rate rate = this.getRate(key);
        if (isExpired(rate)) {

            final Long limit = policy.getLimit();
            final Long refreshInterval = SECONDS.toMillis(policy.getRefreshInterval());
            final Date expiration = new Date(System.currentTimeMillis() + refreshInterval);

            rate = new Rate(key, limit, refreshInterval, expiration);
        }
        return rate;
    }

    private void updateRate(final Rate rate) {
        if (rate.getReset() > 0) {
            Long reset = rate.getExpiration().getTime() - System.currentTimeMillis();
            rate.setReset(reset);
        }
        rate.setRemaining(Math.max(-1, rate.getRemaining() - 1));
    }

    private boolean isExpired(final Rate rate) {
        return rate == null || (rate.getExpiration().getTime() < System.currentTimeMillis());
    }
}
