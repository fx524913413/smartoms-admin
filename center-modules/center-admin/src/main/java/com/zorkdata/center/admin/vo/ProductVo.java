package com.zorkdata.center.admin.vo;

import java.util.Date;

/**
 * 产品前端页面角色展示模型
 */
public class ProductVo {


    private Integer productID;

    private String productName;

    private Integer sort;

    private String code;

    private String icon;

    private Integer ower;

    private Date createTime;

    private String owerName;

    public ProductVo() {
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

    public String getOwerName() {
        return owerName;
    }

    public void setOwerName(String owerName) {
        this.owerName = owerName;
    }
}
