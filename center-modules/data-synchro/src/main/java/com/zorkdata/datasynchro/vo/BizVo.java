package com.zorkdata.datasynchro.vo;

import java.util.List;

/**
 * 业务到机器模型
 */
public class BizVo {
    private String appSystemName;

    private String sysCode;

    private List<SetVo> setVos;

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

    public List<SetVo> getSetVos() {
        return setVos;
    }

    public void setSetVos(List<SetVo> setVos) {
        this.setVos = setVos;
    }
}
