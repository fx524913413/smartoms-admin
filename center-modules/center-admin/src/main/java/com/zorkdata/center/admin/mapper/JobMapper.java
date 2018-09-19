package com.zorkdata.center.admin.mapper;

import com.zorkdata.center.admin.entity.Job;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/5/28 16:26
 */
@Repository
public interface JobMapper extends Mapper<Job> {
    void insertJob(Job job);
}
