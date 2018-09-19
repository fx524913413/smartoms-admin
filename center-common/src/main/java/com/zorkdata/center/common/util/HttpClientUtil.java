package com.zorkdata.center.common.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * httpcomponents-client-4.5.2实现http(s)的post请求和get请求方法
 */

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/6/5 16:29
 */
public class HttpClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static int poolManagerMaxTotal = 200;
    private static int poolManagerMaxPerRoute = 50;
    private static int socketTimeout = 3 * 0000;
    private static int connectTimeout = 3 * 000;
    /**
     * 设置请求和传输超时时间
     */
    private static RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(socketTimeout)
            .setConnectTimeout(connectTimeout)
            .setConnectionRequestTimeout(connectTimeout)
            // cookie warn
            .setCookieSpec(CookieSpecs.STANDARD)
            .build();

    private HttpClientUtil() {
    }

    /**
     * 获取httpclient
     *
     * @return
     */
    private static CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpClient = PoolManager.getHttpClient(poolManagerMaxTotal, poolManagerMaxPerRoute);
        return httpClient;
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     */
    public static String sendHttpPost(String httpUrl) throws Exception {
        // 创建httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param params  参数(格式:key1=value1&key2=value2)
     */
    public static String sendHttpPost(String httpUrl, String params) throws Exception {
        // 创建httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        // 设置参数
        StringEntity stringEntity = new StringEntity(params, "UTF-8");
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);
        return sendHttpPost(httpPost);
    }

    public static String sendHttpPost(String httpUrl, String params, Map<String, String> header) throws Exception {
        // 创建httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        // 设置参数
        StringEntity stringEntity = new StringEntity(params, "UTF-8");
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);
        return sendHttpPost(httpPost, header);
    }

    public static String sendHttpPostForm(String httpUrl, List<NameValuePair> params, Map<String, String> header) throws Exception {
        // 创建httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        // 设置参数
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
        httpPost.setEntity(entity);
        entity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
        return sendHttpPost(httpPost, header);
    }

    public static String sendHttpDelete(String httpUrl) throws Exception {
        // 创建httpDelete
        HttpDelete httpDelete = new HttpDelete(httpUrl);
        return sendHttpDelete(httpDelete);
    }

    public static String sendHttpPut(String httpUrl, String params) throws Exception {
        // 创建httpDelete
        HttpPut httpPut = new HttpPut(httpUrl);
        return sendHttpPut(httpPut);
    }

    public static String sendHttpPut(String httpUrl, String params, Map<String, String> header) throws Exception {
        // 创建httpPost
        HttpPut httpPut = new HttpPut(httpUrl);
        // 设置参数
        StringEntity stringEntity = new StringEntity(params, "UTF-8");
        stringEntity.setContentType("application/json");
        httpPut.setEntity(stringEntity);
        return sendHttpPut(httpPut, header);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param params  参数
     */
    public static String sendHttpPost(String httpUrl, Map<String, String> params) throws Exception {
        // 创建httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        // 创建参数队列
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                nameValuePairs.add(new BasicNameValuePair(key, params.get(key)));
            }
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求（带文件）
     *
     * @param httpUrl   地址
     * @param params    参数
     * @param fileLists 附件
     */
    public static String sendHttpPost1(String httpUrl, Map<String, Object> params, Map<String, File> fileLists) throws Exception {
        InputStream inputStream = null;
        // 创建httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                meBuilder.addPart(key, new StringBody(params.get(key) + "", ContentType.TEXT_PLAIN));
            }
        }
        if (fileLists != null && !fileLists.isEmpty()) {
            for (String key : fileLists.keySet()) {
                inputStream = new FileInputStream(fileLists.get(key));
                meBuilder.addBinaryBody("file", inputStream);
            }
        }
        HttpEntity reqEntity = meBuilder.build();
        httpPost.setEntity(reqEntity);
        String retString = sendHttpPost(httpPost);
        if (inputStream != null) {
            inputStream.close();
        }
        return retString;
    }


//    public static String sendHttpPost(String httpUrl, Map<String, Object> params, Map<String, InputStream> fileLists) throws Exception {
//        InputStream inputStream = null;
//        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
//        MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
//        if (params != null && !params.isEmpty()) {
//            for (String key : params.keySet()) {
//                meBuilder.addPart(key, new StringBody(params.get(key) + "", ContentType.TEXT_PLAIN));
//            }
//        }
//        if (fileLists != null && !fileLists.isEmpty()) {
//            for (String key : fileLists.keySet()) {
//                inputStream = fileLists.get(key);
//                meBuilder.addBinaryBody("file", inputStream);
//            }
//        }
//        HttpEntity reqEntity = meBuilder.build();
//        httpPost.setEntity(reqEntity);
//        String retString = sendHttpPost(httpPost);
//        if (inputStream != null) {
//            inputStream.close();
//        }
//        return retString;
//    }

    /**
     * 发送Post请求
     *
     * @param httpPost
     * @return
     */
    private static String sendHttpPost(HttpPost httpPost) throws Exception {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = getHttpClient();
            httpPost.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity resEntity = response.getEntity();
                responseContent = EntityUtils.toString(resEntity, "UTF-8");
            } else {
                logger.info("请求bizpc服务失败，状态码为：" + statusCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    private static String sendHttpPost(HttpPost httpPost, Map<String, String> header) throws Exception {

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = getHttpClient();
            httpPost.setConfig(requestConfig);
            if (header != null && header.size() > 0) {
                for (String key : header.keySet()) {
                    httpPost.setHeader(key, header.get(key));
                }
            }
            // 执行请求
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            // if (statusCode == HttpStatus.SC_OK) {
            HttpEntity resEntity = response.getEntity();
            responseContent = EntityUtils.toString(resEntity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    private static String sendHttpPut(HttpPut httpPut, Map<String, String> header) throws Exception {

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = getHttpClient();
            httpPut.setConfig(requestConfig);
            if (header != null && header.size() > 0) {
                for (String key : header.keySet()) {
                    httpPut.setHeader(key, header.get(key));
                }
            }
            // 执行请求
            response = httpClient.execute(httpPut);
            int statusCode = response.getStatusLine().getStatusCode();
            // if (statusCode == HttpStatus.SC_OK) {
            HttpEntity resEntity = response.getEntity();
            responseContent = EntityUtils.toString(resEntity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    private static String sendHttpDelete(HttpDelete httpDelete) throws Exception {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = getHttpClient();
            httpDelete.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpDelete);
            int statusCode = response.getStatusLine().getStatusCode();
            // if (statusCode == HttpStatus.SC_OK) {
            HttpEntity resEntity = response.getEntity();
            responseContent = EntityUtils.toString(resEntity, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    private static String sendHttpPut(HttpPut httpPut) throws Exception {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = getHttpClient();
            httpPut.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpPut);
            int statusCode = response.getStatusLine().getStatusCode();
            // if (statusCode == HttpStatus.SC_OK) {
            HttpEntity resEntity = response.getEntity();
            responseContent = EntityUtils.toString(resEntity, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    /**
     * 发送 get请求
     *
     * @param httpUrl
     */
    public static String sendHttpGet(String httpUrl) throws Exception {
        HttpGet httpGet = new HttpGet(httpUrl);
        return sendHttpGet(httpGet);
    }

    /**
     * 发送get请求
     *
     * @param httpUrl
     * @param header
     * @return
     * @throws Exception
     */
    public static String sendHttpGet(String httpUrl, Map<String, String> header) throws Exception {
        HttpGet httpGet = new HttpGet(httpUrl);
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = getHttpClient();
            httpGet.setConfig(requestConfig);
            if (header != null && header.size() > 0) {
                for (String key : header.keySet()) {
                    httpGet.setHeader(key, header.get(key));
                }
            }
            // 执行请求
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity resEntity = response.getEntity();
                responseContent = EntityUtils.toString(resEntity, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    /**
     * 发送 get请求Https
     *
     * @param httpUrl
     */
    public static String sendHttpsGet(String httpUrl) throws Exception {
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
        return sendHttpsGet(httpGet);
    }

    /**
     * 发送Get请求
     *
     * @param httpGet
     * @return
     */
    private static String sendHttpGet(HttpGet httpGet) throws Exception {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = getHttpClient();
            httpGet.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity resEntity = response.getEntity();
                responseContent = EntityUtils.toString(resEntity, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    /**
     * 发送Get请求Https
     *
     * @param httpGet
     * @return
     */
    private static String sendHttpsGet(HttpGet httpGet) throws Exception {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.load(new URL(httpGet.getURI().toString()));
            DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
            httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();
            httpGet.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    public static final byte[] readBytes(InputStream is, int contentLen) {
        if (contentLen > 0) {
            int readLen = 0;
            int readLengthThisTime = 0;
            byte[] message = new byte[contentLen];
            try {
                while (readLen != contentLen) {
                    readLengthThisTime = is.read(message, readLen, contentLen - readLen);
                    if (readLengthThisTime == -1) {// Should not happen.
                        break;
                    }
                    readLen += readLengthThisTime;
                }
                return message;
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }
        return new byte[]{};
    }

    public static void main(String[] args) throws Exception {
        String url = "http://192.168.1.92:8010/dyn/IWorkerNodeSyncService/GetWorkerNodesInfo?recvAddress=1.222";
//        sendHttpPost1(url, null);
//        for (int i = 0; i < 100; i++) {
//            long startTime1 = System.currentTimeMillis();
//            String re = HttpClientUtil.get(url, "utf-8");
//            long endTime1 = System.currentTimeMillis();
//            System.out.println("获取结果时间1 " + +(endTime1 - startTime1) + "ms");
//        }
//        long startTime1 = System.currentTimeMillis();
//        String re = HttpClientUtil.get(url, "utf-8");
//        long endTime1 = System.currentTimeMillis();
//        System.out.println("获取结果时间1 " + +(endTime1-startTime1)+"ms");
//        JSONObject obj = JSONObject.parseObject(re);
//        String tmp = obj.getString("data");
//        if(null != tmp) {
//            JSONArray jsonObj = JSONObject.parseArray(tmp);
//            for(int i = 0;  i < jsonObj.size(); i++) {
//                JSONObject obj1 = (JSONObject) jsonObj.get(i);
//                System.out.println(obj1.get("WorkerNodeID") + ", " + obj1.get("State"));
//            }
//        }
    }

    /**
     * 发送 post请求（带文件）
     *
     * @param httpUrl   地址
     * @param params    参数
     * @param fileLists 附件
     */
    public static String sendHttpPost(String httpUrl, Map<String, Object> params, Map<String, File> fileLists) throws Exception {
        InputStream inputStream = null;
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                meBuilder.addPart(key, new StringBody(params.get(key) + "", ContentType.TEXT_PLAIN));
            }
        }
        if (fileLists != null && !fileLists.isEmpty()) {
            for (String key : fileLists.keySet()) {
                inputStream = new FileInputStream(fileLists.get(key));
                meBuilder.addBinaryBody("file", inputStream);
            }
        }
        HttpEntity reqEntity = meBuilder.build();
        httpPost.setEntity(reqEntity);
        String retString = sendHttpPost(httpPost);
        if (inputStream != null) {
            inputStream.close();

        }
        return retString;
    }
}
