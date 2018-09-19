package com.zorkdata.center.admin.mapper;

import com.zorkdata.center.admin.entity.Group;
import com.zorkdata.center.admin.entity.UserGroup;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Set;

/**
 * @description: ${todo}
 * @author: huziyue
 * @create: 2018/3/20 15:30
 */
@Repository
public interface GroupMapper extends Mapper<Group> {

    /**
     * 删除一个用户组数据
     *
     * @param ids 用户组id集合
     */
    void deleteByIds(@Param("ids") Set<Long> ids);

    /**
     * 根据组id编辑用户组数据
     *
     * @param group 更改的用户组数据
     */
    void updateByGroupId(Group group);

    /**
     * 通过组名查询组织
     *
     * @param groupName
     * @return
     */
    Group getGroupByGroupName(@Param("groupName") String groupName, @Param("parentID") String parentID,@Param("groupID") Long groupID);

    /**
     * 查看用户组内是否存在用户
     * @param groupID
     * @return
     */
    List<Long> isHaveUser(Long groupID);
}
