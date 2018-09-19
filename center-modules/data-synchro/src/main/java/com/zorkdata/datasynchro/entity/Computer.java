package com.zorkdata.datasynchro.entity;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "Computer")
public class Computer {

    @Column(name = "ComputerID")
    private Long computerID;

    @Column(name = "ComputerName")
    private String computerName;

    @Column(name = "IP")
    private String ip;

    @Column(name = "Type")
    private String type;

    public Long getComputerID() {
        return computerID;
    }

    public void setComputerID(Long computerID) {
        this.computerID = computerID;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
