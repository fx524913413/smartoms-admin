package com.zorkdata.center.admin.mapper;

import com.zorkdata.center.admin.entity.Test;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/16 10:50
 */
public interface TestMapper extends Mapper<Test> {
    public List<Test> selectMemberByGroupId(@Param("groupId") int groupId);

    public List<Test> selectLeaderByGroupId(@Param("groupId") int groupId);
}
