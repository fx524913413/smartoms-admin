package com.zorkdata.center.common.salt.netapi.client.impl;

import com.zorkdata.center.common.salt.netapi.client.ConnectionFactory;
import com.zorkdata.center.common.salt.netapi.config.ClientConfig;
import com.zorkdata.center.common.salt.netapi.parser.JsonParser;

/**
 * Implementation of a factory for connections using JDK's HttpURLConnection.
 *
 * @see JDKConnection
 */
public class JDKConnectionFactory implements ConnectionFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> JDKConnection<T> create(String endpoint, JsonParser<T> parser,
                                       ClientConfig config) {
        return new JDKConnection<>(endpoint, parser, config);
    }
}
