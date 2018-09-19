package com.zorkdata.center.cache.aspect;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;

import com.zorkdata.center.cache.annotation.Cache;
import com.zorkdata.center.cache.api.CacheAPI;
import com.zorkdata.center.cache.parser.ICacheResultParser;
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

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/19 9:27
 */
@Aspect
@Service
public class CacheAspect {
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private IKeyGenerator keyParser;
    @Autowired
    private CacheAPI cacheAPI;

    private ConcurrentHashMap<String, ICacheResultParser> parserMap = new ConcurrentHashMap<String, ICacheResultParser>();
    private ConcurrentHashMap<String, IKeyGenerator> generatorMap = new ConcurrentHashMap<String, IKeyGenerator>();

    @Pointcut("@annotation(com.zorkdata.center.cache.annotation.Cache)")
    public void aspect() {
    }

    @Around("aspect()&&@annotation(anno)")
    public Object interceptor(ProceedingJoinPoint invocation, Cache anno)
            throws Throwable {
        MethodSignature signature = (MethodSignature) invocation.getSignature();
        Method method = signature.getMethod();
        Object result = null;
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] arguments = invocation.getArgs();
        String key = "";
        String value = "";
        try {
            key = getKey(anno, parameterTypes, arguments);
            value = cacheAPI.get(key);
            Type returnType = method.getGenericReturnType();
            result = getResult(anno, result, value, returnType);
        } catch (Exception e) {
            log.error("获取缓存失败：" + key, e);
        } finally {
            if (result == null) {
                result = invocation.proceed();
                if (StringUtils.isNotBlank(key)) {
                    cacheAPI.set(key, result, anno.expire());
                }
            }
        }
        return result;
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
    private String getKey(Cache anno, Class<?>[] parameterTypes,
                          Object[] arguments) throws InstantiationException,
            IllegalAccessException {
        String key;
        String generatorClsName = anno.generator().getName();
        IKeyGenerator keyGenerator = null;
        if (anno.generator().equals(DefaultKeyGenerator.class)) {
            keyGenerator = keyParser;
        } else {
            if (generatorMap.contains(generatorClsName)) {
                keyGenerator = generatorMap.get(generatorClsName);
            } else {
                keyGenerator = anno.generator().newInstance();
                generatorMap.put(generatorClsName, keyGenerator);
            }
        }

        key = keyGenerator.getKey(anno.key(), anno.scope(), parameterTypes,
                arguments);
        return key;
    }

    /**
     * 解析结果
     *
     * @param anno
     * @param result
     * @param value
     * @param returnType
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private Object getResult(Cache anno, Object result, String value,
                             Type returnType) throws InstantiationException,
            IllegalAccessException {
        String parserClsName = anno.parser().getName();
        ICacheResultParser parser = null;
        if (parserMap.containsKey(parserClsName)) {
            parser = parserMap.get(parserClsName);
        } else {
            parser = anno.parser().newInstance();
            parserMap.put(parserClsName, parser);
        }
        if (parser != null) {
            if (anno.result()[0].equals(Object.class)) {
                result = parser.parse(value, returnType,
                        null);
            } else {
                result = parser.parse(value, returnType,
                        anno.result());
            }
        }
        return result;
    }
}