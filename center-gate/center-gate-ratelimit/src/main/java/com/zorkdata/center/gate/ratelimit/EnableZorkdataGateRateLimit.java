package com.zorkdata.center.gate.ratelimit;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/16 9:36
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RateLimitAutoConfiguration.class)
@Documented
@Inherited
public @interface EnableZorkdataGateRateLimit {
}
