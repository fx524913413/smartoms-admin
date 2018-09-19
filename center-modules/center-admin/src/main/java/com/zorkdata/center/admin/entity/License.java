package com.zorkdata.center.admin.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @description: ${todo}
 * @author: dingyu
 * @create: 2018/4/4 10:23
 */
@Table(name = "`License`")
public class License {

    @Column(name = "LicenseID")
    private Long licenseID;

    @Column(name = "GroupID")
    private Long groupID;

    @Column(name = "LicenseMessage")
    private String licenseMessage;


    public String getUser_amount() {
        return user_amount;
    }

    public void setUser_amount(String user_amount) {
        this.user_amount = user_amount;
    }

    private String user_amount;

    public String getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(String expire_date) {
        this.expire_date = expire_date;
    }

    private String expire_date;

    public String getMachine_amount() {
        return machine_amount;
    }

    public void setMachine_amount(String machine_amount) {
        this.machine_amount = machine_amount;
    }

    private String machine_amount;

    public License() {
    }

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


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    private String groupName;

}
