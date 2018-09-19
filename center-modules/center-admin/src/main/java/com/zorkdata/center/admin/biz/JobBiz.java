package com.zorkdata.center.admin.biz;

import com.zorkdata.center.admin.entity.Job;
import com.zorkdata.center.admin.mapper.JobMapper;
import com.zorkdata.center.common.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/5/28 21:09
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class JobBiz extends BaseBiz<JobMapper, Job> {
    public void insertJob(Job job) {
        mapper.insertJob(job);
    }
}
