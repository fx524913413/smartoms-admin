package com.zorkdata.center.admin.rpc.service.Impl;

import com.zorkdata.center.admin.biz.ActionBiz;
import com.zorkdata.center.admin.biz.ProductBiz;
import com.zorkdata.center.admin.entity.Action;
import com.zorkdata.center.admin.entity.ActionGroup;
import com.zorkdata.center.admin.rpc.service.Ifc.ActionServiceIfc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * =
 *
 * @author: huziyue
 * @create: 2018/3/30 18:13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ActionServiceImpl implements ActionServiceIfc {
    @Autowired
    private ActionBiz actionBiz;
    @Autowired
    private ProductBiz productBiz;

    @Override
    public void insert(Action action,String productCode) {
        Integer productID = productBiz.getProductIDByProductCode(productCode);
        action.setProductID(productID);
        actionBiz.insertSelective(action);
    }

    @Override
    public void updateSelective(Action action,String productCode) {
        Integer productID = productBiz.getProductIDByProductCode(productCode);
        action.setProductID(productID);
        actionBiz.updateByActionID(action);
    }

    @Override
    public void deleteActionByIds(List<Long> actionIds) {
        actionBiz.deleteActionByIds(actionIds);
    }

    @Override
    public List<Action> getAllAction() {
        List<Action> actions = actionBiz.selectListAll();
        return actions;
    }

    @Override
    public List<ActionGroup> getActionGroup(String productCode) {
        Integer productID = productBiz.getProductIDByProductCode(productCode);
        return actionBiz.getActionGroup(productID);
    }

    @Override
    public void addActionGroup(ActionGroup actionGroup,String productCode) {
        Integer productID = productBiz.getProductIDByProductCode(productCode);
        actionGroup.setProductID(productID);
        actionBiz.addActionGroup(actionGroup);
    }

    @Override
    public void updateActionGroup(ActionGroup actionGroup,String productCode) {
        Integer productID = productBiz.getProductIDByProductCode(productCode);
        actionGroup.setProductID(productID);
        actionBiz.updateActionGroup(actionGroup);
    }

    @Override
    public void deleteActionGroup(Long actionGroupID) {
        actionBiz.deleteActionGroup(actionGroupID);
    }
}
