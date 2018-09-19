package com.zorkdata.center.admin.entity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author: huziyue
 * @create: 2018/3/30 18:08
 */
@Table(name = "Action")
public class Action {

    @Column(name = "ActionID")
    private Long actionID;

    @Column(name = "ActionName")
    private String actionName;

    @Column(name = "ProductID")
    private Integer productID;

    @Column(name = "actionGroupID")
    private Long actionGroupID;

    @Column(name = "actionCode")
    private String actionCode;

    public Long getActionID() {
        return actionID;
    }

    public void setActionID(Long actionID) {
        this.actionID = actionID;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public Long getActionGroupID() {
        return actionGroupID;
    }

    public void setActionGroupID(Long actionGroupID) {
        this.actionGroupID = actionGroupID;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }
}
