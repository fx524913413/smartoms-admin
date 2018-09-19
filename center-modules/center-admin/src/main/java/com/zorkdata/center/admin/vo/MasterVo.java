package com.zorkdata.center.admin.vo;

import com.zorkdata.center.admin.entity.Computer;

import java.util.List;

public class MasterVo {
    private String username;

    private String password;

    private String insDir;

    private int port;

    private List<Computer> computerList;

    private boolean noPass;

    private Long clusterID;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInsDir() {
        return insDir;
    }

    public void setInsDir(String insDir) {
        this.insDir = insDir;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<Computer> getComputerList() {
        return computerList;
    }

    public void setComputerList(List<Computer> computerList) {
        this.computerList = computerList;
    }

    public boolean isNoPass() {
        return noPass;
    }

    public void setNoPass(boolean noPass) {
        this.noPass = noPass;
    }

    public Long getClusterID() {
        return clusterID;
    }

    public void setClusterID(Long clusterID) {
        this.clusterID = clusterID;
    }
}
