package com.zorkdata.center.admin.biz;

import com.zorkdata.center.admin.entity.Permission;
import com.zorkdata.center.admin.entity.Role;
import com.zorkdata.center.admin.mapper.PermissionMapper;
import com.zorkdata.center.admin.mapper.RoleMapper;
import com.zorkdata.center.admin.vo.RoleInfo;
import com.zorkdata.center.common.biz.BaseBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author huziyue
 * @create 2018/3/27 9:46
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleBiz extends BaseBiz<RoleMapper, Role> {
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public void insertSelective(Role entity) {
        mapper.insertSelectiveGetId(entity);
    }

    public void updateByRoleID(Role role) {
        mapper.updateByRoleID(role);
    }

    public void deleteByIds(List<Long> id) {
        mapper.deleteByIds(id);
    }

    public List<Role> getRoleByProductID(Integer productID) {
        return mapper.getRoleByProductID(productID);
    }

    public List<RoleInfo> getRoleInfoByProductID(Integer productID) {
        return mapper.getRoleInfoByProductID(productID);
    }

    public List<Long> getRoleIDByProductID(Integer productID) {
        return mapper.getRoleIDByProductID(productID);
    }

    public List<Role> getRoleByIds(Set<Long> roleIds) {
        return mapper.getRoleByIds(roleIds);
    }

    public Role getRoleByRoleNameAndProductCode(String roleName, Integer productID,Long roleID) {
        return mapper.getRoleByRoleNameAndProductCode(roleName, productID,roleID);
    }
}
