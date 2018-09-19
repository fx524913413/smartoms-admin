package com.zorkdata.center.admin.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/27 11:06
 */
@Table(name = "Menu")
public class Menu {
    @Column(name = "MenuID")
    private Long menuID;
    @Column(name = "MenuName")
    private String menuName;
    @Column(name = "ParentID")
    private Long parentID;
    @Column(name = "URL")
    private String url;
    @Column(name = "Sort")
    private Long sort;
    @Column(name = "Code")
    private String code;
    @Column(name = "ProductID")
    private String productID;
    @Column(name = "CreateTime")
    private Date createTime;
    @Column(name = "Comment")
    private String comment;

    public Long getMenuID() {
        return menuID;
    }

    public void setMenuID(Long menuID) {
        this.menuID = menuID;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Long getParentID() {
        return parentID;
    }

    public void setParentID(Long parentID) {
        this.parentID = parentID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}
