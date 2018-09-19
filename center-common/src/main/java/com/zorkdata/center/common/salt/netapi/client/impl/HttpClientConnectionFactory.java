package com.zorkdata.center.common.salt.netapi.client.impl;

import com.zorkdata.center.common.salt.netapi.client.ConnectionFactory;
import com.zorkdata.center.common.salt.netapi.config.ClientConfig;
import com.zorkdata.center.common.salt.netapi.parser.JsonParser;

/**
 * Implementation of a factory for connections using Apache's HttpClient.
 *
 * @see HttpClientConnection
 */
public class HttpClientConnectionFactory implements ConnectionFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> HttpClientConnection<T> create(String endpoint,
                                              JsonParser<T> parser, ClientConfig config) {
        return new HttpClientConnection<>(endpoint, parser, config);
    }
}
