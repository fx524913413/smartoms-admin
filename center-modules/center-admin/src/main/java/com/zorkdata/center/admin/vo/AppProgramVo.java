package com.zorkdata.center.admin.vo;

/**
 * @author Administrator
 */
public class AppProgramVo {
    private String appSystemName;
    private String appType;
    private long appProgramId;
    private String appProgramName;
    private String appProgramNo;
    private String IPAddress;
    private String hostName;
    private String Type;
    private String code;
    private String computerId;
    private String osType;
    private String clusterName;
    private String clusterInstanceId;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getClusterInstanceId() {
        return clusterInstanceId;
    }

    public void setClusterInstanceId(String clusterInstanceId) {
        this.clusterInstanceId = clusterInstanceId;
    }

    private String appInstanceId;
    private String modelInCMDB;
    private String memo;
    private String externalComputerId;

    public String getExternalComputerId() {
        return externalComputerId;
    }

    public void setExternalComputerId(String externalComputerId) {
        this.externalComputerId = externalComputerId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getAppInstanceId() {
        return appInstanceId;
    }

    public void setAppInstanceId(String appInstanceId) {
        this.appInstanceId = appInstanceId;
    }

    public String getModelInCMDB() {
        return modelInCMDB;
    }

    public void setModelInCMDB(String modelInCMDB) {
        this.modelInCMDB = modelInCMDB;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getComputerId() {
        return computerId;
    }

    public void setComputerId(String computerId) {
        this.computerId = computerId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAppSystemName() {
        return appSystemName;
    }

    public void setAppSystemName(String appSystemName) {
        this.appSystemName = appSystemName;
    }

    public long getAppProgramId() {
        return appProgramId;
    }

    public void setAppProgramId(long appProgramId) {
        this.appProgramId = appProgramId;
    }

    public String getAppProgramName() {
        return appProgramName;
    }

    public String getAppProgramNo() {
        return appProgramNo;
    }

    public void setAppProgramNo(String appProgramNo) {
        this.appProgramNo = appProgramNo;
    }

    public void setAppProgramName(String appProgramName) {
        this.appProgramName = appProgramName;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String iPAddress) {
        IPAddress = iPAddress;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }
}
