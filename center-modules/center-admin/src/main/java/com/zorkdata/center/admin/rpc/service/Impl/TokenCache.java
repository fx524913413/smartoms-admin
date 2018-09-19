package com.zorkdata.center.admin.rpc.service.Impl;

import java.util.HashMap;
import java.util.Map;

public class TokenCache {
    private static Map<String, String> tokenCache = new HashMap();

    public static Map<String, String> getTokenCache() {
        return tokenCache;
    }

    public static void setTokenCache(Map<String, String> tokenCache) {
        TokenCache.tokenCache = tokenCache;
    }

    public static void setToken(String token) {
        tokenCache.put("token", token);
    }

    public static String getToken() {
        return tokenCache.get("token");
    }

}
