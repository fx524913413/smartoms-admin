package com.zorkdata.center.cache.test.cache;

import com.zorkdata.center.cache.constants.CacheScope;
import com.zorkdata.center.cache.parser.IKeyGenerator;
import com.zorkdata.center.cache.parser.IUserKeyGenerator;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/19 10:03
 */
public class MyKeyGenerator extends IKeyGenerator {
    @Override
    public IUserKeyGenerator getUserKeyGenerator() {
        return null;
    }

    @Override
    public String buildKey(String key, CacheScope scope, Class<?>[] parameterTypes, Object[] arguments) {
        return "myKey_" + arguments[0];
    }
}
