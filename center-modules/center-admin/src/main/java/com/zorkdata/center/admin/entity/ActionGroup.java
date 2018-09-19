package com.zorkdata.center.admin.entity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 功能组类实体
 *
 * @author: huziyue
 * @create: 2018/5/21 13:30
 */
@Table(name = "actionGroup")
public class ActionGroup {
    @Column(name = "actionGroupID")
    private Long actionGroupID;

    @Column(name = "groupName")
    private String groupName;

    @Column(name = "productID")
    private Integer productID;

    public Long getActionGroupID() {
        return actionGroupID;
    }

    public void setActionGroupID(Long actionGroupID) {
        this.actionGroupID = actionGroupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }
}
