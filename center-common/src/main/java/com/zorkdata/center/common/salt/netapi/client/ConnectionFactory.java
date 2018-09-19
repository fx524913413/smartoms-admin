package com.zorkdata.center.common.salt.netapi.client;

import com.zorkdata.center.common.salt.netapi.config.ClientConfig;
import com.zorkdata.center.common.salt.netapi.parser.JsonParser;

/**
 * Describes an interface for creating instances of an HTTP connection implementation.
 */
public interface ConnectionFactory {

    /**
     * Create a new {@link Connection} for a given endpoint and configuration.
     *
     * @param <T>      type of the result as returned by the parser
     * @param endpoint the API endpoint
     * @param parser   the parser used for parsing the result
     * @param config   the configuration
     * @return object representing a connection to the API
     */
    <T> Connection<T> create(String endpoint, JsonParser<T> parser, ClientConfig config);
}
