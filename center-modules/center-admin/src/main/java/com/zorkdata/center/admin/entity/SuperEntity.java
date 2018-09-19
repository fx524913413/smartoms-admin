package com.zorkdata.center.admin.entity;

import java.util.TreeMap;

/**
 * 万能实体类，可用于任何数据的封装
 */
public class SuperEntity extends TreeMap<String, Object> {

    public SuperEntity setProperty(String key, Object value) {

        this.put(key, value);

        return this;
    }
}
