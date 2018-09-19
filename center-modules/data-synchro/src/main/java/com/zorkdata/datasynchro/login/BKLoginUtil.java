package com.zorkdata.datasynchro.login;


import com.zorkdata.center.common.util.HttpClientUtil;
import com.zorkdata.datasynchro.entity.BKToken;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 蓝鲸平台登录工具类
 */
@Component
public class BKLoginUtil {

    private static Logger log = Logger.getLogger(BKLoginUtil.class);

    public static BKToken Login(String username, String password) {
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        BKToken bkToken = new BKToken();
        String bklogin_csrftoken = null;
        try {
            log.info("开始登录");
            CookieStore cookieStore = new BasicCookieStore();
            httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            httpGet = new HttpGet("http://paas.blueking.com/platform/");
            httpClient.execute(httpGet);
            List<Cookie> cookies = cookieStore.getCookies();
            for (int i = 0; i < cookies.size(); i++) {
                if (cookies.get(i).getName().equals("bklogin_csrftoken")) {
                    bklogin_csrftoken = cookies.get(i).getValue();
                    bkToken.setBklogin_csrftoken(bklogin_csrftoken);
                    System.out.println("获取bklogin_csrftoken成功，值为：" + bklogin_csrftoken);
                    log.info("获取bklogin_csrftoken成功，值为：" + bklogin_csrftoken);
                }
            }
            if (bklogin_csrftoken != null) {
                HttpPost httpPost = new HttpPost("http://paas.blueking.com/login/?&c_url=/");
                //设置请求头
                httpPost.setHeader("X-CSRFToken", bklogin_csrftoken);
                // 设置参数
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);
                httpPost.setEntity(entity);
                HttpResponse response = httpClient.execute(httpPost);
                System.out.println(response);
                List<Cookie> bkcookies = cookieStore.getCookies();
                for (Cookie cookie : bkcookies) {
                    if (cookie.getName().equals("bk_token")) {
                        String bk_token = cookie.getValue();
                        bkToken.setBk_token(bk_token);
                        System.out.println("获取bk_token成功，值为：" + bk_token);
                        log.info("获取bk_token成功，值为：" + bk_token);
                    } else if (cookie.getName().equals("bklogin_csrftoken")) {
                        String bklogin_csrftoken1 = cookie.getValue();
                        bkToken.setBklogin_csrftoken(bklogin_csrftoken1);
                        System.out.println("获取bklogin_csrftoken成功，值为：" + bklogin_csrftoken1);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println(bkToken);
        return bkToken;
    }

    //测试登录
    public static void main(String[] args) {
        BKToken bkToken = Login("admin", "zorkdata");
        try {
            Map<String, String> map = new HashMap<>();
            map.put("X-CSRFToken", bkToken.getBklogin_csrftoken());
            map.put("Cookie", "bk_token=" + bkToken.getBk_token() + ";" + "bklogin_csrftoken=" + bkToken.getBklogin_csrftoken());;
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", "test"));
            params.add(new BasicNameValuePair("chname", "test"));
            params.add(new BasicNameValuePair("qq", "123456"));
            params.add(new BasicNameValuePair("phone", "12345678910"));
            params.add(new BasicNameValuePair("role", "0"));
            params.add(new BasicNameValuePair("email", "1@qq.com"));
            try {
                String s = HttpClientUtil.sendHttpPostForm("http://paas.blueking.com/login/accounts/save_user/", params, map);
                System.out.println(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
