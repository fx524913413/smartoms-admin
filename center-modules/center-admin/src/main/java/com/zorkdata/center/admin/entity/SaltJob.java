package com.zorkdata.center.admin.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/5/24 22:10
 */
@Table(name = "SaltJob")
public class SaltJob {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "jid")
    private String jid;

    @Column(name = "saltjobname")
    private String saltjobname;

    @Column(name = "arg")
    private String arg;

    @Column(name = "fun")
    private String fun;

    @Column(name = "minions")
    private String minions;

    @Column(name = "missing")
    private String missing;

    @Column(name = "tgt")
    private String tgt;

    @Column(name = "tgt_type")
    private String tgt_type;

    @Column(name = "user")
    private String user;

    @Column(name = "state")
    private Integer state;

    @Column(name = "stamp")
    private Date stamp;

    @Column(name = "jobid")
    private Long jobid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getArg() {
        return arg;
    }

    public void setArg(String arg) {
        this.arg = arg;
    }

    public String getFun() {
        return fun;
    }

    public void setFun(String fun) {
        this.fun = fun;
    }

    public String getSaltjobname() {
        return saltjobname;
    }

    public void setSaltjobname(String saltjobname) {
        this.saltjobname = saltjobname;
    }

    public String getMinions() {
        return minions;
    }

    public void setMinions(String minions) {
        this.minions = minions;
    }

    public String getMissing() {
        return missing;
    }

    public void setMissing(String missing) {
        this.missing = missing;
    }

    public String getTgt() {
        return tgt;
    }

    public void setTgt(String tgt) {
        this.tgt = tgt;
    }

    public String getTgt_type() {
        return tgt_type;
    }

    public void setTgt_type(String tgt_type) {
        this.tgt_type = tgt_type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getStamp() {
        return stamp;
    }

    public void setStamp(Date stamp) {
        this.stamp = stamp;
    }

    public Long getJobid() {
        return jobid;
    }

    public void setJobid(Long jobid) {
        this.jobid = jobid;
    }
}
