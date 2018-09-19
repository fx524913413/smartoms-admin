package com.zorkdata.center.admin.vo;

public class BKProgramInstanceVo {
    /**实例名*/
    private String bk_inst_name;
    /**主机*/
    private Integer computer;
    /**组件*/
    private Integer appprogram;
    /**应用系统*/
    private String appsystem;
    /**程序路径*/
    private String path;
    /**配置文件路径*/
    private String confpath;
    /**配置文件名称*/
    private String confname;
    /**状态*/
    private String state;
    /**服务码*/
    private Integer servicecode;

    public String getBk_inst_name() {
        return bk_inst_name;
    }

    public void setBk_inst_name(String bk_inst_name) {
        this.bk_inst_name = bk_inst_name;
    }

    public Integer getComputer() {
        return computer;
    }

    public void setComputer(Integer computer) {
        this.computer = computer;
    }

    public Integer getAppprogram() {
        return appprogram;
    }

    public void setAppprogram(Integer appprogram) {
        this.appprogram = appprogram;
    }

    public String getAppsystem() {
        return appsystem;
    }

    public void setAppsystem(String appsystem) {
        this.appsystem = appsystem;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getConfpath() {
        return confpath;
    }

    public void setConfpath(String confpath) {
        this.confpath = confpath;
    }

    public String getConfname() {
        return confname;
    }

    public void setConfname(String confname) {
        this.confname = confname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getServicecode() {
        return servicecode;
    }

    public void setServicecode(Integer servicecode) {
        this.servicecode = servicecode;
    }
}
