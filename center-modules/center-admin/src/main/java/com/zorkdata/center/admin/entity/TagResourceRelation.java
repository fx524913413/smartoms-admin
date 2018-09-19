package com.zorkdata.center.admin.entity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/30 13:06
 */
@Table(name = "TagResourceRelation")
public class TagResourceRelation {
    @Column(name = "ID")
    private Long id;

    @Column(name = "TagID")
    private Long tagID;

    @Column(name = "ResourceType")
    private String resourceType;

    @Column(name = "ResourceID")
    private Long resourceID;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTagID() {
        return tagID;
    }

    public void setTagID(Long tagID) {
        this.tagID = tagID;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Long getResourceID() {
        return resourceID;
    }

    public void setResourceID(Long resourceID) {
        this.resourceID = resourceID;
    }
}
