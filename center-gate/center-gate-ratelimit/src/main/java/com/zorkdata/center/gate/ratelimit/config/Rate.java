package com.zorkdata.center.gate.ratelimit.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;


/**
 * Represents a view of rate limit in a giving time for a user.
 * <p>
 * limit - How many requests can be executed by the user. Maps to X-RateLimit-Limit header
 * remaining - How many requests are still left on the current window. Maps to X-RateLimit-Remaining header
 * reset - Epoch when the rate is replenished by limit. Maps to X-RateLimit-Reset header
 *
 * @author: zhuzhigang
 * @create: 2018/3/16 9:03
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Rate {
    @Id
    private String key;
    private Long remaining;
    private Long reset;
    private Date expiration;

}