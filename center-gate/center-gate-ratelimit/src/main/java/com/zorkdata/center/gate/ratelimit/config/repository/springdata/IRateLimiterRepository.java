package com.zorkdata.center.gate.ratelimit.config.repository.springdata;

import com.zorkdata.center.gate.ratelimit.config.Rate;
import org.springframework.data.repository.CrudRepository;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/16 9:34
 */
public interface IRateLimiterRepository extends CrudRepository<Rate, String> {
}
