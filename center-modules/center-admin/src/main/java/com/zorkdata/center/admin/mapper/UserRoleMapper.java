package com.zorkdata.center.admin.mapper;

import com.zorkdata.center.admin.entity.UserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author: huziyue
 * @create: 2018/3/22 15:42
 */
@Repository
public interface UserRoleMapper extends Mapper<UserRole> {

    /**
     * 通过角色id解除与用户的关联关系
     *
     * @param ids
     */
    void deleteByRoleIds(List<Long> ids);

    /**
     * 通过用户id解除角色与用户的关联关系
     *
     * @param ids
     */
    void deleteByUserIds(List<Long> ids);

    /**
     * 通过用户id查询角色id
     *
     * @param userID
     * @return
     */
    List<Long> getRoleIDByUserID(Long userID);

    /**
     * 为当前角色解除关联多个用户
     *
     * @param userIds
     * @param roleId
     */
    void deleteUserToRole(@Param("userIds") List<Long> userIds, @Param("roleId") Long roleId);

    /**
     * 通过角色查询用户id
     */
    List<Long> getUserIDByRoleID(Long roleId);

    /**
     * 通过多个角色id查询用户id
     *
     * @param roleIds
     * @return
     */
    List<Long> getUserIDByRoleIds(List<Long> roleIds);

    /**
     * 解除该用户下的角色
     *
     * @param userID
     * @param roleIds
     */
    void deleteByUserIDAndRoleIds(@Param("userID") Long userID, @Param("roleIds") List<Long> roleIds);
}
