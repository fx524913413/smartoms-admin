package com.zorkdata.datasynchro.vo;

import java.util.List;

/**
 * 业务到机器模型
 */
public class BizVo2 {
    private String appSystemName;

    private String sysCode;

    private List<SetVo2> setVos2;

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

    public List<SetVo2> getSetVos2() {
        return setVos2;
    }

    public void setSetVos2(List<SetVo2> setVos2) {
        this.setVos2 = setVos2;
    }
}
