package com.zorkdata.center.admin.biz;

import com.zorkdata.center.admin.entity.UserGroup;
import com.zorkdata.center.admin.mapper.UserGroupMapper;
import com.zorkdata.center.admin.util.MyException;
import com.zorkdata.center.common.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(rollbackFor = Exception.class ,noRollbackFor= MyException.class)
public class UserGroupBiz extends BaseBiz<UserGroupMapper, UserGroup> {

    public void deleteByGroupIds(Set<Long> ids) {
        mapper.deleteByGroupIds(ids);
    }

    public void deleteByUserIds(Set<Long> ids) {
        mapper.deleteByUserIds(ids);
    }

    public Set<Long> getGroupIDByUserID(Long userID) {
        Set<Long> set = new HashSet<>();
        List<Long> groupIds = mapper.getGroupIDByUserID(userID);
        set.addAll(groupIds);
        return set;
    }
}
