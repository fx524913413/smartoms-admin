package com.zorkdata.center.common.salt.netapi.utils;

import com.zorkdata.center.common.salt.netapi.exception.SaltException;

/**
 * Like java.util.function.Function but can throw SaltException
 *
 * @param <T> parameter type
 * @param <R> return type
 */
@FunctionalInterface
public interface FunctionE<T, R> {
    R apply(T t) throws SaltException;
}
