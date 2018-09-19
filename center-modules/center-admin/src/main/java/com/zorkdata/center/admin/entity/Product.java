package com.zorkdata.center.admin.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @description: ${todo}
 * @author: huziyue
 * @create: 2018/3/20 16:26
 */
@Table(name = "Product")
public class Product {

    @Column(name = "ProductID")
    private Integer productID;

    @Column(name = "ProductName")
    private String productName;

    @Column(name = "Sort")
    private Integer sort;

    @Column(name = "Code")
    private String code;

    @Column(name = "Icon")
    private String icon;

    @Column(name = "Ower")
    private Integer ower;

    @Column(name = "CreateTime")
    private Date createTime;

    @Column(name = "url")
    private String url;

    public Product() {
    }


    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getOwer() {
        return ower;
    }

    public void setOwer(Integer ower) {
        this.ower = ower;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
