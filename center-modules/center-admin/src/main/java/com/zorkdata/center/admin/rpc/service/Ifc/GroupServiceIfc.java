package com.zorkdata.center.admin.rpc.service.Ifc;

import com.zorkdata.center.admin.entity.Group;
import com.zorkdata.center.admin.vo.GroupTreeNode;

import java.util.List;
import java.util.Set;

/**
 * 用户组服务层接口
 *
 * @author: huziyue
 * @create: 2018/3/21 15:53
 */
public interface GroupServiceIfc {

    /**
     * 获取所有用户组数据
     *
     * @return 用户组树结构集合
     */
    List<GroupTreeNode> selectList();

    /**
     * 获取单个用户组信息
     *
     * @param id 用户组id
     * @return 用户组对象
     */
    Group selectOne(Long id);

    /**
     * 添加一个用户组数据
     *
     * @param group 用户组对象
     */
    Boolean insertSelective(Group group,Long userID,String productCode);

    /**
     * 编辑一个用户组数据
     *
     * @param group 用户组对象
     */
    Boolean updateSelective(Group group,Long userID,String productCode);

    /**
     * 删除一个用户组数据
     *
     * @param ids 用户组id集合
     */
    Boolean deleteGroupById(Set<Long> ids,Long userID,String productCode);

    /**
     * 通过组名查询组织
     *
     * @param groupName
     * @return
     */
    Group getGroupByGroupName(String groupName, String parentID,Long groupID);

    /**
     * 判断当前用户组是否存在用户
     * @param groupID
     * @return
     */
    Boolean isHaveUser(Long groupID);
}
