package com.zorkdata.center.adminv2.vo;

import java.util.LinkedHashMap;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/6/5 12:57
 */
public class TreeNode {
    /**
     * 节点Id
     */
    private int nodeId;
    /**
     * 父节点Id
     */
    private Object parentId;
    /**
     * 文本内容
     */
    private LinkedHashMap<String, Object> ext;
    /**
     * 显示名
     */
    private String displayName;
    /**
     * 状态
     */
    private int nodeState;
    /**
     * 类型
     */
    private String ntype;

    /**
     * 构造函数
     */
    public TreeNode() {
    }

    /**
     * 构造函数
     *
     * @param nodeId 节点Id
     */
    public TreeNode(int nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * 构造函数
     *
     * @param nodeId   节点Id
     * @param parentId 父节点Id
     */
    public TreeNode(int nodeId, int parentId) {
        this.nodeId = nodeId;
        this.parentId = parentId;
    }

    /**
     * 构造函数
     *
     * @param nodeId   节点Id
     * @param parentId 父节点Id
     * @param parentId 附加信息
     * @param parentId 显示名
     * @param parentId 类型(appsystem,agent)
     */
    public TreeNode(int nodeId, int parentId, LinkedHashMap<String, Object> map, String displayName, String type) {
        this.nodeId = nodeId;
        this.parentId = parentId;
        this.ext = map;
        this.displayName = displayName;
        this.ntype = type;
    }

    /**
     * 构造函数
     *
     * @param nodeId   节点Id
     * @param parentId 父节点Id
     * @param parentId 附加信息
     * @param parentId 显示名
     * @param parentId 类型(appsystem,agent)
     */
    public TreeNode(int nodeId, String parentId, LinkedHashMap<String, Object> map, String displayName, String type) {
        this.nodeId = nodeId;
        this.parentId = parentId;
        this.ext = map;
        this.displayName = displayName;
        this.ntype = type;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public Object getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public LinkedHashMap<String, Object> getExt() {
        return ext;
    }

    public void setExt(LinkedHashMap<String, Object> ext) {
        this.ext = ext;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getNodeState() {
        return nodeState;
    }

    public void setNodeState(int nodeState) {
        this.nodeState = nodeState;
    }

    public String getNtype() {
        return ntype;
    }

    public void setNtype(String ntype) {
        this.ntype = ntype;
    }

    public void setParentId(Object parentId) {
        this.parentId = parentId;
    }
}
