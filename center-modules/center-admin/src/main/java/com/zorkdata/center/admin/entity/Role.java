package com.zorkdata.center.admin.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * @description: ${todo}
 * @author: huziyue
 * @create: 2018/3/20 15:21
 */
@Table(name = "Role")
public class Role {

    @Column(name = "RoleID")
    private Long roleID;

    @Column(name = "RoleName")
    private String roleName;

    @Column(name = "State")
    private Integer state;

    @Column(name = "CreateTime")
    private Date createTime;

    @Column(name = "Comment")
    private String comment;

    @Column(name = "ProductID")
    private Integer productID;

    @Transient
    private List<User> users;

    public Role() {
    }

    public Long getRoleID() {
        return roleID;
    }

    public void setRoleID(Long roleID) {
        this.roleID = roleID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
