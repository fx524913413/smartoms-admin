package com.zorkdata.center.admin.vo;

import com.zorkdata.center.admin.config.FileConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

public class SaltProperties {
    @Autowired
    FileConfiguration fileConfiguration;
    private String URL;
    private String userName;
    private String password;
    private Integer socketTimeout;
    private static SaltProperties saltProperties;

    private SaltProperties() {
        saltProperties.setURL(fileConfiguration.getSaltURL());
        saltProperties.setPassword(fileConfiguration.getSaltPassword());
        saltProperties.setUserName(fileConfiguration.getSaltUserName());
        saltProperties.setSocketTimeout(fileConfiguration.getSocketTimeout());
    }

//    public static synchronized SaltProperties getSaltProperties(){
//        if(saltProperties==null){
//            return new SaltProperties();
//        }
//        return saltProperties;
//    }

    public static SaltProperties getSaltProperties() {
        if (saltProperties == null) {
            return new SaltProperties();
        }
        return saltProperties;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }
}
