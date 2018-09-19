package com.zorkdata.center.admin.mapper;

import com.zorkdata.center.admin.entity.SaltJob;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/5/24 22:09
 */
@Repository
public interface SaltJobMapper extends Mapper<SaltJob> {
    void insertBatch(SaltJob saltJob);
}
