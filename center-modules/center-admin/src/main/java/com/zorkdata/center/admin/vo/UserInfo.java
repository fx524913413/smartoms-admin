package com.zorkdata.center.admin.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: ${todo}
 * @author: huziyue
 * @create: 2018/3/20 10:13
 */
public class UserInfo implements Serializable {
    private String userId;

    private String userName;

    private String password;

    private String trueName;

    private Date createTime;

    private String sex;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
