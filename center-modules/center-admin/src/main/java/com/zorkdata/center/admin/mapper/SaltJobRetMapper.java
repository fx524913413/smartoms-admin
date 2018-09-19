package com.zorkdata.center.admin.mapper;

import com.zorkdata.center.admin.entity.SaltJobRet;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/5/24 22:09
 */
@Repository
public interface SaltJobRetMapper extends Mapper<SaltJobRet> {
    void insertBatch(SaltJobRet saltJobRet);
}
