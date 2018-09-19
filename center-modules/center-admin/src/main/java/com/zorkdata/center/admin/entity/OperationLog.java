package com.zorkdata.center.admin.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @description: ${todo}
 * @author: huziyue
 * @create: 2018/3/20 17:00
 */
@Table(name = "OperationLog")
public class OperationLog {

    @Column(name = "OperationLogID")
    private Integer operationLogID;

    @Column(name = "OperationType")
    private Integer operationType;

    @Column(name = "Content")
    private String content;

    @Column(name = "UserID")
    private Integer userID;

    @Column(name = "OperationTime")
    private Date operationTime;

    public OperationLog() {
    }

    public Integer getOperationLogID() {
        return operationLogID;
    }

    public void setOperationLogID(Integer operationLogID) {
        this.operationLogID = operationLogID;
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }
}
