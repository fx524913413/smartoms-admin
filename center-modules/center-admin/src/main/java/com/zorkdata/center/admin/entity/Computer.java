package com.zorkdata.center.admin.entity;

import com.zorkdata.center.common.util.NetTelnet;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "Computer")
public class Computer {
    @Id
    @Column(name = "ComputerID")
    private Long computerID;

    @Column(name = "ComputerType")
    private String computerType;

    @Column(name = "HostName")
    private String hostName;

    @Column(name = "IP")
    private String ip;

    @Column(name = "Mac")
    private String mac;

    @Column(name = "UserName")
    private String userName;

    @Column(name = "Password")
    private String password;

    @Column(name = "LastModifyTime")
    private Date lastModifyTime;

    @Column(name = "CiCode")
    private String ciCode;

    public String getCiCode() {
        return ciCode;
    }

    public void setCiCode(String ciCode) {
        this.ciCode = ciCode;
    }

    public Long getComputerID() {
        return computerID;
    }

    public void setComputerID(Long computerID) {
        this.computerID = computerID;
    }

    public String getComputerType() {
        return computerType;
    }

    public void setComputerType(String computerType) {
        this.computerType = computerType;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
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

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    @Override
    public String toString() {
        return "Computer{" +
                "computerID=" + computerID +
                ", computerType='" + computerType + '\'' +
                ", hostName='" + hostName + '\'' +
                ", ip='" + ip + '\'' +
                ", mac='" + mac + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", lastModifyTime=" + lastModifyTime +
                ", ciCode='" + ciCode + '\'' +
                '}';
    }
}
