package com.zorkdata.center.cache.aspect;

import com.zorkdata.center.cache.annotation.CacheClear;
import com.zorkdata.center.cache.api.CacheAPI;
import com.zorkdata.center.cache.constants.CacheScope;
import com.zorkdata.center.cache.parser.IKeyGenerator;
import com.zorkdata.center.cache.parser.impl.DefaultKeyGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/19 9:29
 */
@Aspect
@Service
public class CacheClearAspect {
    @Autowired
    private IKeyGenerator keyParser;
    @Autowired
    private CacheAPI cacheAPI;
    protected Logger log = Logger.getLogger(this.getClass());
    private ConcurrentHashMap<String, IKeyGenerator> generatorMap = new ConcurrentHashMap<String, IKeyGenerator>();

    @Pointcut("@annotation(com.zorkdata.center.cache.annotation.CacheClear)")
    public void aspect() {
    }

    @Around("aspect()&&@annotation(anno)")
    public Object interceptor(ProceedingJoinPoint invocation, CacheClear anno)
            throws Throwable {
        MethodSignature signature = (MethodSignature) invocation.getSignature();
        Method method = signature.getMethod();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] arguments = invocation.getArgs();
        String key = "";
        if (StringUtils.isNotBlank(anno.key())) {
            key = getKey(anno, anno.key(), CacheScope.application,
                    parameterTypes, arguments);
            cacheAPI.remove(key);
        } else if (StringUtils.isNotBlank(anno.pre())) {
            key = getKey(anno, anno.pre(), CacheScope.application,
                    parameterTypes, arguments);
            cacheAPI.removeByPre(key);
        } else if (anno.keys().length > 1) {
            for (String tmp : anno.keys()) {
                tmp = getKey(anno, tmp, CacheScope.application, parameterTypes,
                        arguments);
                cacheAPI.removeByPre(tmp);
            }
        }
        return invocation.proceed();
    }

    /**
     * 解析表达式
     *
     * @param anno
     * @param parameterTypes
     * @param arguments
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private String getKey(CacheClear anno, String key, CacheScope scope,
                          Class<?>[] parameterTypes, Object[] arguments)
            throws InstantiationException, IllegalAccessException {
        String finalKey;
        String generatorClsName = anno.generator().getName();
        IKeyGenerator keyGenerator = null;
        if (anno.generator().equals(DefaultKeyGenerator.class)) {
            keyGenerator = keyParser;
        } else {
            if (generatorMap.containsKey(generatorClsName)) {
                keyGenerator = generatorMap.get(generatorClsName);
            } else {
                keyGenerator = anno.generator().newInstance();
                generatorMap.put(generatorClsName, keyGenerator);
            }
        }
        finalKey = keyGenerator.getKey(key, scope, parameterTypes, arguments);
        return finalKey;
    }
}