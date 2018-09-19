package com.zorkdata.center.admin.biz;

import com.zorkdata.center.admin.entity.Action;
import com.zorkdata.center.admin.entity.ActionGroup;
import com.zorkdata.center.admin.mapper.ActionMapper;
import com.zorkdata.center.common.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author: huziyue
 * @create: 2018/3/30 18:10
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ActionBiz extends BaseBiz<ActionMapper, Action> {
    public void updateByActionID(Action action) {
        mapper.updateByActionID(action);
    }

    public void deleteActionByIds(List<Long> actionIds) {
        mapper.deleteActionByIds(actionIds);
    }


    public List<Action> getActionByResourceID(Set<Long> actionIds) {
        return mapper.getActionByResourceID(actionIds);
    }

    public List<Action> getActionByProductID(Integer productID) {
        return mapper.getActionByProductID(productID);
    }

    public Long getActionIDByActionCode(String actionCode) {
        return mapper.getActionIDByActionCode(actionCode);
    }

    public List<ActionGroup> getActionGroup(Integer productID) {
        return mapper.getActionGroup(productID);
    }

    public void addActionGroup(ActionGroup actionGroup) {
        mapper.addActionGroup(actionGroup);
    }

    public void updateActionGroup(ActionGroup actionGroup) {
        mapper.updateActionGroup(actionGroup);
    }

    public void deleteActionGroup(Long actionGroupID) {
        mapper.deleteActionGroup(actionGroupID);
    }
}
