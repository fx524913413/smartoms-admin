package com.zorkdata.center.admin.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/5/28 16:14
 */
@Table(name = "Job")
public class Job {
    @Id
    @Column(name = "jobid")
    private Long jobid;

    @Column(name = "state")
    private Integer state;

    @Column(name = "createtime")
    private Date createtime;

    public Long getJobid() {
        return jobid;
    }

    public void setJobid(Long jobid) {
        this.jobid = jobid;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}
