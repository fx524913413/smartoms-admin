package com.zorkdata.center.auth.service;

import java.util.List;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/20 15:24
 */
public interface AuthClientService {
    public String apply(String clientId, String secret) throws Exception;

    /**
     * 获取授权的客户端列表
     *
     * @param serviceId
     * @param secret
     * @return
     */
    public List<String> getAllowedClient(String serviceId, String secret) throws Exception;

    /**
     * 获取服务授权的客户端列表
     *
     * @param serviceId
     * @return
     */
    public List<String> getAllowedClient(String serviceId);

    public void registryClient();

    public void validate(String clientId, String secret) throws Exception;
}
