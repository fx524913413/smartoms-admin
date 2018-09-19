package com.zorkdata.center.admin.entity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @description: ${todo}
 * @author: huziyue
 * @create: 2018/3/20 15:50
 */
@Table(name = "Permission")
public class Permission {

    @Column(name = "PermissionID")
    private Long permissionID;

    @Column(name = "PermissionName")
    private String permissionName;

    @Column(name = "SourceType")
    private String sourceType;

    @Column(name = "SourceID")
    private Long sourceID;

    @Column(name = "Resource")
    private String resource;

    @Column(name = "ResourceID")
    private Long resourceID;

    @Column(name = "Chmod")
    private Integer chmod;

    public Permission() {
    }

    public Long getPermissionID() {
        return permissionID;
    }

    public void setPermissionID(Long permissionID) {
        this.permissionID = permissionID;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Long getSourceID() {
        return sourceID;
    }

    public void setSourceID(Long sourceID) {
        this.sourceID = sourceID;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Long getResourceID() {
        return resourceID;
    }

    public void setResourceID(Long resourceID) {
        this.resourceID = resourceID;
    }

    public Integer getChmod() {
        return chmod;
    }

    public void setChmod(Integer chmod) {
        this.chmod = chmod;
    }
}
