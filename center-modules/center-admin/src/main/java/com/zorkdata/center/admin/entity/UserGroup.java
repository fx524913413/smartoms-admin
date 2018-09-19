package com.zorkdata.center.admin.entity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author: huziyue
 * @create: 2018/3/22 15:28
 */
@Table(name = "UserGroup")
public class UserGroup {

    @Column(name = "UserGroupID")
    private Long userGroupID;
    @Column(name = "UserID")
    private Long userID;
    @Column(name = "GroupID")
    private Long groupID;

    public UserGroup() {
    }


    public Long getUserGroupID() {
        return userGroupID;
    }

    public void setUserGroupID(Long userGroupID) {
        this.userGroupID = userGroupID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getGroupID() {
        return groupID;
    }

    public void setGroupID(Long groupID) {
        this.groupID = groupID;
    }
}
