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
@Table(name = "SaltJobRet")
public class SaltJobRet {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "minionid")
    private String minionid;

    @Column(name = "cmd")
    private String cmd;

    @Column(name = "jid")
    private String jid;

    @Column(name = "saltjobname")
    private String saltjobname;

    @Column(name = "fun_args")
    private String fun_args;

    @Column(name = "fun")
    private String fun;

    @Column(name = "retcode")
    private Integer retcode;

    @Column(name = "missing")
    private String missing;

    @Column(name = "returns")
    private String returns;

    @Column(name = "success")
    private Integer success;

    @Column(name = "stamp")
    private Date stamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMinionid() {
        return minionid;
    }

    public void setMinionid(String minionid) {
        this.minionid = minionid;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getSaltjobname() {
        return saltjobname;
    }

    public void setSaltjobname(String saltjobname) {
        this.saltjobname = saltjobname;
    }

    public String getFun_args() {
        return fun_args;
    }

    public void setFun_args(String fun_args) {
        this.fun_args = fun_args;
    }

    public String getFun() {
        return fun;
    }

    public void setFun(String fun) {
        this.fun = fun;
    }

    public Integer getRetcode() {
        return retcode;
    }

    public void setRetcode(Integer retcode) {
        this.retcode = retcode;
    }

    public String getMissing() {
        return missing;
    }

    public void setMissing(String missing) {
        this.missing = missing;
    }

    public String getReturns() {
        return returns;
    }

    public void setReturns(String returns) {
        this.returns = returns;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Date getStamp() {
        return stamp;
    }

    public void setStamp(Date stamp) {
        this.stamp = stamp;
    }

}
