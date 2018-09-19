package com.zorkdata.center.admin.rpc.service.Impl;

import com.zorkdata.center.admin.biz.*;
import com.zorkdata.center.admin.entity.Product;
import com.zorkdata.center.admin.entity.Role;
import com.zorkdata.center.admin.entity.User;
import com.zorkdata.center.admin.entity.UserRole;
import com.zorkdata.center.admin.rpc.service.Ifc.RoleServiceIfc;
import com.zorkdata.center.admin.util.PermissionCode;
import com.zorkdata.center.admin.util.PermissionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: huziyue
 * @create: 2018/3/27 09:44
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl implements RoleServiceIfc {
    @Autowired
    private RoleBiz roleBiz;
    @Autowired
    private UserRoleBiz userRoleBiz;
    @Autowired
    private UserBiz userBiz;
    @Autowired
    private ProductBiz productBiz;
    @Autowired
    private PermissionBiz permissionBiz;
    @Autowired
    PermissionUtil permissionUtil;

    @Override
    public Boolean addRole(Role role, String productCode, Long userID) {
        Boolean flag = permissionUtil.haveActionRole(PermissionCode.PERMISSION_CONTROL, userID, productCode);
        if (flag) {
            role.setProductID(productBiz.getProductIDByProductCode(productCode));
            roleBiz.insertSelective(role);
            return true;
        }
        return false;
    }

    @Override
    public Boolean updateRole(Role role, String productCode, Long userID) {
        Boolean flag = permissionUtil.haveActionRole(PermissionCode.PERMISSION_CONTROL, userID, productCode);
        if (flag) {
            roleBiz.updateByRoleID(role);
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteRoleByIds(List<Long> ids, String productCode, Long userID) {
        Boolean flag = permissionUtil.haveActionRole(PermissionCode.PERMISSION_CONTROL, userID, productCode);
        if (flag) {
            userRoleBiz.deleteByRoleIds(ids);
            permissionBiz.deleteBySourceIds(PermissionCode.ROLE, ids);
            roleBiz.deleteByIds(ids);
            return true;
        }
        return false;
    }

    @Override
    public List<Role> getRoleByProductCode(String productCode) {
        return roleBiz.getRoleByProductID(productBiz.getProductIDByProductCode(productCode));
    }

    @Override
    public Boolean addUserToRole(List<Long> userIds, Long roleId, String productCode, Long userID) {
        Boolean flag = permissionUtil.haveActionRole(PermissionCode.PERMISSION_CONTROL, userID, productCode);
        if (flag) {
            if (userIds != null && userIds.size() != 0) {
                for (Long userId : userIds) {
                    UserRole userRole = new UserRole();
                    userRole.setRoleID(roleId);
                    userRole.setUserID(userId);
                    userRoleBiz.insertSelective(userRole);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteUserToRole(List<Long> userIds, Long roleId, String productCode, Long userID) {
        Boolean flag = permissionUtil.haveActionRole(PermissionCode.PERMISSION_CONTROL, userID, productCode);
        if (flag) {
            if (userIds != null && userIds.size() != 0) {
                userRoleBiz.deleteUserToRole(userIds, roleId);
            }
            return true;
        }
        return false;
    }

    @Override
    public List<User> getUserByRole(Long roleId) {
        List<User> users = new ArrayList<>();
        List<Long> userIds = userRoleBiz.getUserIDByRoleID(roleId);
        if (userIds != null && userIds.size() != 0) {
            users = userBiz.getUserByIds(userIds);
        }
        return users;
    }

    @Override
    public List<Role> getRoleByUser(Long userID) {
        Set<Long> roleIds = userRoleBiz.getRoleIDByUserID(userID);
        if (roleIds != null && roleIds.size() != 0) {
            return roleBiz.getRoleByIds(roleIds);
        }
        return null;
    }

    @Override
    public Boolean addUsersToRoles(List<Long> userIds, List<Long> roleIds, Long userID, String productCode) {
        Boolean flag = permissionUtil.haveActionRole(PermissionCode.PERMISSION_CONTROL, userID, productCode);
        if (flag) {
            for (Long userId : userIds) {
                for (Long roleID : roleIds) {
                    UserRole userRole = new UserRole();
                    userRole.setUserID(userId);
                    userRole.setRoleID(roleID);
                    userRoleBiz.insertSelective(userRole);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public Boolean isHaveUser(Long roleID) {
        List<Long> userIds = userRoleBiz.getUserIDByRoleID(roleID);
        if (userIds != null && userIds.size() != 0) {
            return true;
        }
        return false;
    }

    @Override
    public Role getRoleByRoleNameAndProductCode(String roleName, String productCode,Long roleID) {
        Integer productID = productBiz.getProductIDByProductCode(productCode);
        return roleBiz.getRoleByRoleNameAndProductCode(roleName, productID,roleID);
    }
}
