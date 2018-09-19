package com.zorkdata.center.adminv2.entity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/6/5 13:27
 */
@Table(name = "AppSystem")
public class AppSystem {
    @Column(name = "AppSystemID")
    private int appSystemId;
    @Column(name = "AppSystemName")
    private String appSystemName;
    @Column(name = "SysCode")
    private String sysCode;
}
