package com.zorkdata.center.admin.biz;

import com.zorkdata.center.admin.entity.Group;
import com.zorkdata.center.admin.entity.UserGroup;
import com.zorkdata.center.admin.mapper.GroupMapper;
import com.zorkdata.center.common.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author huziyue
 * @description: ${todo}
 * @create 2018/3/21 15:00
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GroupBiz extends BaseBiz<GroupMapper, Group> {

    public void deleteByIds(Set<Long> ids) {
        mapper.deleteByIds(ids);
    }

    public void updateByGroupId(Group group) {
        mapper.updateByGroupId(group);
    }

    public Group getGroupByGroupName(String groupName, String parentID,Long groupID) {
        return mapper.getGroupByGroupName(groupName, parentID,groupID);
    }

    public List<Long> isHaveUser(Long groupID) {
        return mapper.isHaveUser(groupID);
    }
}
