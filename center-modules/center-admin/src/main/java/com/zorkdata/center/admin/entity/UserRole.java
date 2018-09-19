package com.zorkdata.center.admin.entity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author: huziyue
 * @create: 2018/3/22 15:32
 */
@Table(name = "UserRole")
public class UserRole {

    @Column(name = "UserRoleID")
    private Long userRoleID;
    @Column(name = "UserID")
    private Long userID;
    @Column(name = "RoleID")
    private Long roleID;

    public UserRole() {
    }


    public Long getUserRoleID() {
        return userRoleID;
    }

    public void setUserRoleID(Long userRoleID) {
        this.userRoleID = userRoleID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getRoleID() {
        return roleID;
    }

    public void setRoleID(Long roleID) {
        this.roleID = roleID;
    }
}
