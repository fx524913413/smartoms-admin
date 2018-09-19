package com.zorkdata.center.admin.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 前台产品角色树展示模型
 */
public class ProductRoleTree {

    private Integer id;

    private String parentID;

    private String name;

    private List<ProductInfo> children = new ArrayList<ProductInfo>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductInfo> getChildren() {
        return children;
    }

    public void setChildren(List<ProductInfo> children) {
        this.children = children;
    }


}
