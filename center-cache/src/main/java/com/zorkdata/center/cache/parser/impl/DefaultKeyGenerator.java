package com.zorkdata.center.cache.parser.impl;

import com.zorkdata.center.cache.constants.CacheScope;
import com.zorkdata.center.cache.parser.IKeyGenerator;
import com.zorkdata.center.cache.parser.IUserKeyGenerator;
import com.zorkdata.center.cache.utils.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/19 9:22
 */
@Service
public class DefaultKeyGenerator extends IKeyGenerator {

    public static final String D_W = "\\d+\\.?[\\w]*";
    public static final String STR = "{";
    public static final String STRING = ":{";
    public static final String REGEX = "\\.";
    @Autowired(required = false)
    private IUserKeyGenerator userKeyGenerator;

    @Override
    public String buildKey(String key, CacheScope scope, Class<?>[] parameterTypes, Object[] arguments) {
        boolean isFirst = true;
        if (key.indexOf(STR) > 0) {
            key = key.replace(STR, STRING);
            Pattern pattern = Pattern.compile(D_W);
            Matcher matcher = pattern.matcher(key);
            while (matcher.find()) {
                String tmp = matcher.group();
                String express[] = matcher.group().split(REGEX);
                String i = express[0];
                int index = Integer.parseInt(i) - 1;
                Object value = arguments[index];
                if (parameterTypes[index].isAssignableFrom(List.class)) {
                    List result = (List) arguments[index];
                    value = result.get(0);
                }
                if (value == null || value.equals("null")) {
                    value = "";
                }
                if (express.length > 1) {
                    String field = express[1];
                    value = ReflectionUtils.getFieldValue(value, field);
                }
                if (isFirst) {
                    key = key.replace(STR + tmp + "}", value.toString());
                } else {
                    key = key.replace(STR + tmp + "}", LINK + value.toString());
                }
            }
        }
        return key;
    }

    @Override
    public IUserKeyGenerator getUserKeyGenerator() {
        return userKeyGenerator;
    }

}