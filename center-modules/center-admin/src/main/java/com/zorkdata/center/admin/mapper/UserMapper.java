package com.zorkdata.center.admin.mapper;

import com.zorkdata.center.admin.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Set;

/**
 * @description: ${todo}
 * @author: huziyue
 * @create: 2018/3/20 9:58
 */
@Repository
public interface UserMapper extends Mapper<User> {

    /**
     * 根据传入的用户组id查找相应的组内用户
     *
     * @param groupID 用户组id
     * @return 组内所有用户
     */
    List<User> getUserByGroupId(Long groupID);

    /**
     * 向用户表插入一条数据并返回主键id
     *
     * @param entity
     */
    void insertSelectiveGetId(User entity);

    /**
     * 根据传入的id数据删除对应的用户
     *
     * @param ids 用户id集合
     */
    void deleteByIds(@Param("ids") Set<Long> ids);

    /**
     * 编辑用户数据
     *
     * @param user 用户数据
     */
    void updateByUserID(User user);

    /**
     * 通过id获得用户
     *
     * @param userIds
     * @return
     */
    List<User> getUserByIds(@Param("userIds") List<Long> userIds);

    /**
     * 获取所有用户
     *
     * @return
     */
    List<User> getAllUser();

    /**
     * 根据用户名查询用户
     * @param userName
     * @param userID
     * @return
     */
    User getUserByUserName(@Param("userName") String userName,@Param("userID") Long userID);
}
