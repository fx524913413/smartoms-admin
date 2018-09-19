package com.zorkdata.center.admin.vo;

import com.zorkdata.center.admin.entity.Computer;

import java.util.List;
import java.util.Map;

public class AgentVo {

    private List<Map> masterList;

    private String username;

    private String password;

    private String insDir;

    private List<ComputerAndComputerSaltType> computerAndComputerSaltTypeList;

    private boolean passwardneed;

    public List<Map> getMasterList() {
        return masterList;
    }

    public void setMasterList(List<Map> masterList) {
        this.masterList = masterList;
    }

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

    public List<ComputerAndComputerSaltType> getComputerAndComputerSaltTypeList() {
        return computerAndComputerSaltTypeList;
    }

    public void setComputerAndComputerSaltTypeList(List<ComputerAndComputerSaltType> computerAndComputerSaltTypeList) {
        this.computerAndComputerSaltTypeList = computerAndComputerSaltTypeList;
    }

    public boolean isPasswardneed() {
        return passwardneed;
    }

    public void setPasswardneed(boolean passwardneed) {
        this.passwardneed = passwardneed;
    }
}
