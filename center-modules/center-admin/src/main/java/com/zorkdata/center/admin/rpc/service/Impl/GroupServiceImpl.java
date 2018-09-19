package com.zorkdata.center.admin.rpc.service.Impl;

import com.zorkdata.center.admin.biz.GroupBiz;
import com.zorkdata.center.admin.biz.UserGroupBiz;
import com.zorkdata.center.admin.entity.Group;
import com.zorkdata.center.admin.entity.UserGroup;
import com.zorkdata.center.admin.rpc.service.Ifc.GroupServiceIfc;
import com.zorkdata.center.admin.util.PermissionCode;
import com.zorkdata.center.admin.util.PermissionUtil;
import com.zorkdata.center.admin.vo.GroupTreeNode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 用户组服务层实现类
 *
 * @author: huziyue
 * @create: 2018/3/20 16:20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GroupServiceImpl implements GroupServiceIfc {
    @Autowired
    private GroupBiz groupBiz;

    @Autowired
    private UserGroupBiz userGroupBiz;

    @Autowired
    private PermissionUtil permissionUtil;

    @Override
    public List<GroupTreeNode> selectList() {
        List<GroupTreeNode> treeNodes = new ArrayList<GroupTreeNode>();
        List<Group> groups = groupBiz.selectListAll();
        for (Group group : groups) {
            GroupTreeNode node = new GroupTreeNode();
            node.setId(group.getGroupID());
            node.setParentId(Long.parseLong(group.getParentID()));
            BeanUtils.copyProperties(group, node);
            treeNodes.add(node);
        }
        return treeNodes;
    }

    @Override
    public Group selectOne(Long id) {
        return groupBiz.selectById(id);
    }

    @Override
    public Boolean insertSelective(Group group,Long userID,String productCode) {
        Boolean flag = permissionUtil.haveActionRole(PermissionCode.PERMISSION_CONTROL, userID, productCode);
        if(flag==true){
            groupBiz.insertSelective(group);
            return true;
        }
        return false;
    }

    @Override
    public Boolean updateSelective(Group group,Long userID,String productCode) {
        Boolean flag = permissionUtil.haveActionRole(PermissionCode.PERMISSION_CONTROL, userID, productCode);
        if(flag==true){
            groupBiz.updateByGroupId(group);
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteGroupById(Set<Long> ids,Long userID,String productCode) {
        Boolean flag = permissionUtil.haveActionRole(PermissionCode.PERMISSION_CONTROL, userID, productCode);
        if(flag==true){
            userGroupBiz.deleteByGroupIds(ids);
            groupBiz.deleteByIds(ids);
            return true;
        }
        return false;
    }

    @Override
    public Group getGroupByGroupName(String groupName, String parentID,Long groupID) {
        return groupBiz.getGroupByGroupName(groupName, parentID,groupID);
    }

    @Override
    public Boolean isHaveUser(Long groupID) {
        List<Long> groups=groupBiz.isHaveUser(groupID);
        if(groups!=null&&groups.size()>0){
            return true;
        }
        return false;
    }
}
