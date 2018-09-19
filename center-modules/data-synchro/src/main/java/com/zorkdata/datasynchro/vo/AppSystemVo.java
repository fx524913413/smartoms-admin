package com.zorkdata.datasynchro.vo;

import java.util.List;

public class AppSystemVo {
    private Integer appSystemId;
    private String appSystemName;
    private String sysCode;
    private List<SetVo> sets;

    public Integer getAppSystemId() {
        return appSystemId;
    }

    public void setAppSystemId(Integer appSystemId) {
        this.appSystemId = appSystemId;
    }

    public String getAppSystemName() {
        return appSystemName;
    }

    public void setAppSystemName(String appSystemName) {
        this.appSystemName = appSystemName;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public List<SetVo> getSets() {
        return sets;
    }

    public void setSets(List<SetVo> sets) {
        this.sets = sets;
    }
}
