package com.zorkdata.center.common.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/15 12:32
 */
public class TreeNode {
    protected Long id;

    protected Long parentId;

    List<TreeNode> children = new ArrayList<TreeNode>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public void add(TreeNode node) {
        children.add(node);
    }

}
