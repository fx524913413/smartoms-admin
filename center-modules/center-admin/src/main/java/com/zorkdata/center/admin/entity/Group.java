package com.zorkdata.center.admin.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * @description: ${todo}
 * @author: huziyue
 * @create: 2018/3/20 15:29
 */
@Table(name = "`Group`")
public class Group {

    @Column(name = "GroupID")
    private Long groupID;

    @Column(name = "GroupName")
    private String groupName;

    @Column(name = "ParentID")
    private String parentID;

    @Column(name = "CreateTime")
    private Date createTime;

    @Column(name = "Comment")
    private String comment;

    @Transient
    private List<User> users;

    public Group() {
    }


    public Long getGroupID() {
        return groupID;
    }

    public void setGroupID(Long groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
