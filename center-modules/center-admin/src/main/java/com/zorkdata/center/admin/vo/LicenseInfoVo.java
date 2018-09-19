package com.zorkdata.center.admin.vo;

import java.io.Serializable;

/**
 * @description: ${todo}
 * @author: dingyu
 * @create: 2018/4/4 13:36
 */
public class LicenseInfoVo implements Serializable {
    private Long licenseID;

    private Long groupID;

    private String licenseMessage;


    public long getLicenseID() {
        return licenseID;
    }

    public void setLicenseID(long licenseID) {
        this.licenseID = licenseID;
    }


    public long getGroupID() {
        return groupID;
    }

    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }


    public String getLicenseMessage() {
        return licenseMessage;
    }

    public void setLicenseMessage(String licenseMessage) {
        this.licenseMessage = licenseMessage;
    }
}
