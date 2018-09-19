package com.zorkdata.datasynchro.entity;

/**
 * 蓝鲸的token实体类
 */
public class BKToken {
    private String bklogin_csrftoken;

    private String bk_token;

    public String getBklogin_csrftoken() {
        return bklogin_csrftoken;
    }

    public void setBklogin_csrftoken(String bklogin_csrftoken) {
        this.bklogin_csrftoken = bklogin_csrftoken;
    }

    public String getBk_token() {
        return bk_token;
    }

    public void setBk_token(String bk_token) {
        this.bk_token = bk_token;
    }
}
