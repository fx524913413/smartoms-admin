package com.zorkdata.center.admin.rpc.service.Ifc;

import com.zorkdata.center.admin.entity.Role;
import com.zorkdata.center.admin.entity.User;

import java.util.List;
import java.util.Set;

/**
 * 角色服务层接口
 *
 * @author: huziyue
 * @create: 2018/3/27 09:42
 */
public interface RoleServiceIfc {

    /**
     * 为当前系统添加一个角色
     *
     * @param role 新建角色数据
     */
    Boolean addRole(Role role, String productCode, Long userID);

    /**
     * 编辑当前角色数据
     *
     * @param role 当前角色数据
     */
    Boolean updateRole(Role role, String productCode, Long userID);

    /**
     * 通过id数组批量删除角色
     *
     * @param ids id的数组
     */
    Boolean deleteRoleByIds(List<Long> ids, String productCode, Long userID);

    /**
     * 通过产品id查询该产品下所有角色
     *
     * @param
     * @return
     */
    List<Role> getRoleByProductCode(String productCode);

    /**
     * 为当前角色关联多个用户
     *
     * @param userIds
     * @param roleId
     */
    Boolean addUserToRole(List<Long> userIds, Long roleId, String productCode, Long userID);

    /**
     * 为当前角色解除关联多个用户
     *
     * @param userIds
     * @param roleId
     */
    Boolean deleteUserToRole(List<Long> userIds, Long roleId, String productCode, Long userID);

    /**
     * 通过角色id查询所有用户
     *
     * @param roleId
     */
    List<User> getUserByRole(Long roleId);

    /**
     * 通过用户查询角色
     *
     * @param userID
     * @return
     */
    List<Role> getRoleByUser(Long userID);

    /**
     * 批量为多个用户赋予多个角色
     *
     * @param userIds
     * @param roleIds
     */
    Boolean addUsersToRoles(List<Long> userIds, List<Long> roleIds, Long userID, String productCode);

    /**
     * 判断当前角色有没有用户
     *
     * @param roleID
     * @return
     */
    Boolean isHaveUser(Long roleID);

    /**
     * 通过角色名和产品简码查询角色
     *
     * @param roleName
     * @param productCode
     * @param roleID
     * @return
     */
    Role getRoleByRoleNameAndProductCode(String roleName, String productCode,Long roleID);
}
