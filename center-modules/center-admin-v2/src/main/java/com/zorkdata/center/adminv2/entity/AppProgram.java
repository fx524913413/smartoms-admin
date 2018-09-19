package com.zorkdata.center.adminv2.entity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author Administrator
 */
@Table(name = "AppProgram")
public class AppProgram {
    @Column(name = "AppProgramID")
    private int appProgramID;
}
