package com.zorkdata.center.admin.entity;


import java.sql.Timestamp;
import java.util.*;


/**
 * @author SwinBlackSea
 * @date 2017/12/27 21:05
 */
public class FileDir  {
    private Long id;
    private String name;
    private String size;
    private Integer type;
    private Long parentId;
    private String saltPath;
    private Integer isPublic;
    private Long creator;
    private Date lastModifyTime;



    public FileDir() {
    }

    public FileDir(String name, String size, Integer type, Long parentId, String saltPath, Integer isPublic, Long creator, Date lastModifyTime) {
        this.name = name;
        this.size = size;
        this.type = type;
        this.parentId = parentId;
        this.saltPath = saltPath;
        this.isPublic = isPublic;
        this.creator = creator;
        this.lastModifyTime = lastModifyTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getSaltPath() {
        return saltPath;
    }

    public void setSaltPath(String saltPath) {
        this.saltPath = saltPath;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
}
