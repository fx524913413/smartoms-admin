package com.zorkdata.center.admin.mapper;

import com.zorkdata.center.admin.entity.UserGroup;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Set;

/**
 * @author: huziyue
 * @create: 2018/3/22 15:41
 */
@Repository
public interface UserGroupMapper extends Mapper<UserGroup> {

    /**
     * 通过组织id中断组织与用户的关系
     *
     * @param ids 用户组id集合
     */
    void deleteByGroupIds(@Param("ids") Set<Long> ids);

    /**
     * 通过用户id中断组织与用户的关系
     *
     * @param ids 用户id集合
     */
    void deleteByUserIds(@Param("ids") Set<Long> ids);

    /**
     * 通过用户id查询用户组id
     *
     * @param userID
     * @return
     */
    List<Long> getGroupIDByUserID(Long userID);
}
