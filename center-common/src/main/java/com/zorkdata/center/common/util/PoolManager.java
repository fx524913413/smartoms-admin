package com.zorkdata.center.common.util;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/6/5 16:30
 */
public class PoolManager {
    public static PoolingHttpClientConnectionManager clientConnectionManager = null;
    private static int defaultMaxTotal = 200;
    private static int defaultMaxPerRoute = 25;

    public synchronized static void getInstance(int maxTotal, int maxPerRoute) {
        try {
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .build();
            clientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            if (maxTotal > 0) {
                clientConnectionManager.setMaxTotal(maxTotal);
            } else {
                clientConnectionManager.setMaxTotal(defaultMaxTotal);
            }
            if (maxPerRoute > 0) {
                clientConnectionManager.setDefaultMaxPerRoute(maxPerRoute);
            } else {
                clientConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
            }
        } catch (Exception e) {
            System.out.println("初始化clientConnectionManager失败" + e);
        }
    }

    public static CloseableHttpClient getHttpClient(int maxTotal, int maxPerRoute) {
        if (clientConnectionManager == null) {
            getInstance(maxTotal, maxPerRoute);
        }
        return HttpClients.custom().setConnectionManager(clientConnectionManager).setConnectionManagerShared(true).build();
    }
}
