package com.zorkdata.center.adminv2.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/6/5 12:59
 */

public class AppSystemVo extends TreeNode {
    private int appSystemId;
    private String appSystemName;
    private String sysCode;
    private AppSystemVo parentSystem;
    // 构建树父级关联
    private AppSystemVo LinkedObj;
    private List<AppSystemVo> appSystemPoUpList = new ArrayList<AppSystemVo>();
    private List<AppSystemVo> appSystemPoDownList = new ArrayList<AppSystemVo>();

    public int getAppSystemId() {
        return appSystemId;
    }

    public void setAppSystemId(int appSystemId) {
        this.appSystemId = appSystemId;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getAppSystemName() {
        return appSystemName;
    }

    public void setAppSystemName(String appSystemName) {
        this.appSystemName = appSystemName;
    }

    public List<AppSystemVo> getAppSystemPoUpList() {
        return appSystemPoUpList;
    }

    public void setAppSystemPoUpList(List<AppSystemVo> appSystemPoUpList) {
        this.appSystemPoUpList = appSystemPoUpList;
    }

    public List<AppSystemVo> getAppSystemPoDownList() {
        return appSystemPoDownList;
    }

    public void setAppSystemPoDownList(List<AppSystemVo> appSystemPoDownList) {
        this.appSystemPoDownList = appSystemPoDownList;
    }

    public AppSystemVo getParentSystem() {
        return parentSystem;
    }

    public void setParentSystem(AppSystemVo parentSystem) {
        this.parentSystem = parentSystem;
    }

    public AppSystemVo getLinkedObj() {
        return LinkedObj;
    }

    public void setLinkedObj(AppSystemVo linkedObj) {
        LinkedObj = linkedObj;
    }
}