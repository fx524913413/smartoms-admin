package com.zorkdata.center.auth.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/29 16:52
 */
@Table(name = "AuthClient")
public class Client {
    @Id
    private Long id;

    @Column(name = "Code")
    private String code;
    @Column(name = "Secret")
    private String secret;
    @Column(name = "Name")
    private String name;
    @Column(name = "Locked")
    private String locked = "0";
    @Column(name = "Description")
    private String Description;

    @Column(name = "CrtTime")
    private Date crtTime;

    @Column(name = "CrtUser")
    private String crtUser;

    @Column(name = "CrtName")
    private String crtName;

    @Column(name = "CrtHost")
    private String crtHost;

    @Column(name = "UpdTime")
    private Date updTime;

    @Column(name = "UpdUser")
    private String updUser;

    @Column(name = "UpdName")
    private String updName;

    @Column(name = "UpdHost")
    private String updHost;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Date getCrtTime() {
        return crtTime;
    }

    public void setCrtTime(Date crtTime) {
        this.crtTime = crtTime;
    }

    public String getCrtUser() {
        return crtUser;
    }

    public void setCrtUser(String crtUser) {
        this.crtUser = crtUser;
    }

    public String getCrtName() {
        return crtName;
    }

    public void setCrtName(String crtName) {
        this.crtName = crtName;
    }

    public String getCrtHost() {
        return crtHost;
    }

    public void setCrtHost(String crtHost) {
        this.crtHost = crtHost;
    }

    public Date getUpdTime() {
        return updTime;
    }

    public void setUpdTime(Date updTime) {
        this.updTime = updTime;
    }

    public String getUpdUser() {
        return updUser;
    }

    public void setUpdUser(String updUser) {
        this.updUser = updUser;
    }

    public String getUpdName() {
        return updName;
    }

    public void setUpdName(String updName) {
        this.updName = updName;
    }

    public String getUpdHost() {
        return updHost;
    }

    public void setUpdHost(String updHost) {
        this.updHost = updHost;
    }
}
