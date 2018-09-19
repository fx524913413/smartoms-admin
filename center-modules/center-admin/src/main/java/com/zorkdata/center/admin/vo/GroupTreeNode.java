package com.zorkdata.center.admin.vo;

import com.zorkdata.center.common.vo.TreeNode;

import java.util.Date;

/**
 * 用户组树模型
 *
 * @author huziyue
 * @create : 2018/3/22 09:21
 */
public class GroupTreeNode extends TreeNode {

    private String groupName;

    private Date createTime;

    private String comment;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
}
