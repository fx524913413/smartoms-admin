package com.zorkdata.datasynchro.entity;

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
