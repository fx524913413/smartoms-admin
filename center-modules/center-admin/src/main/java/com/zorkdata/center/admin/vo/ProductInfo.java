package com.zorkdata.center.admin.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 前端产品展示模型
 */
public class ProductInfo {

    private Integer id;

    private Integer parentID;

    private String name;

    private Integer sort;

    private String code;

    private String icon;

    private Integer ower;

    private Date createTime;

    private List<RoleInfo> children = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getOwer() {
        return ower;
    }

    public void setOwer(Integer ower) {
        this.ower = ower;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<RoleInfo> getChildren() {
        return children;
    }

    public void setChildren(List<RoleInfo> children) {
        this.children = children;
    }
}
