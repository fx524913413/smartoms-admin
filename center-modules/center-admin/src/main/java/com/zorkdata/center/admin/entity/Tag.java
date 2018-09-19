package com.zorkdata.center.admin.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @description: ${todo}
 * @author: huziyue
 * @create: 2018/3/20 18:00
 */
@Table(name = "Tag")
public class Tag {

    @Column(name = "id")
    private Long id;

    @Column(name = "tagName")
    private String tagName;

    @Column(name = "productID")
    private Integer productID;

    @Column(name = "comment")
    private String comment;

    @Column(name = "createTime")
    private Date createTime;

    public Tag() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
