package com.zorkdata.center.admin.biz;

import com.zorkdata.center.admin.entity.UserRole;
import com.zorkdata.center.admin.mapper.UserRoleMapper;
import com.zorkdata.center.common.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author huziyue
 * @create 2018/3/27 9:47
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserRoleBiz extends BaseBiz<UserRoleMapper, UserRole> {

    public void deleteByRoleIds(List<Long> ids) {
        mapper.deleteByRoleIds(ids);
    }

    public void deleteByUserIds(List<Long> ids) {
        mapper.deleteByUserIds(ids);
    }

    public Set<Long> getRoleIDByUserID(Long userID) {
        Set<Long> set = new HashSet<>();
        List<Long> roleIds = mapper.getRoleIDByUserID(userID);
        for (Long roleId : roleIds) {
            set.add(roleId);
        }
        return set;
    }

    public void deleteUserToRole(List<Long> userIds, Long roleId) {
        mapper.deleteUserToRole(userIds, roleId);
    }

    public List<Long> getUserIDByRoleID(Long roleId) {
        return mapper.getUserIDByRoleID(roleId);
    }

    public List<Long> getUserIDByRoleIds(List<Long> roleIds) {
        return mapper.getUserIDByRoleIds(roleIds);
    }

    public void deleteByUserIDAndRoleIds(Long userID, List<Long> roleIds) {
        mapper.deleteByUserIDAndRoleIds(userID, roleIds);
    }
}
