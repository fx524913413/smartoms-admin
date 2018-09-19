package com.zorkdata.datasynchro.entity;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "User")
public class User {
    @Column(name = "Telphone")
    private String phone;

    @Column(name = "Email")
    private String email;

    @Column(name = "UserName")
    private String username;

    @Column(name = "TrueName")
    private String chname;

    @Column(name = "QQ")
    private String QQ;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getChname() {
        return chname;
    }

    public void setChname(String chname) {
        this.chname = chname;
    }

    public String getQQ() {
        return QQ;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }
}
