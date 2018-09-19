package com.zorkdata.center.admin.rpc.service.Ifc;

import com.zorkdata.center.admin.entity.Action;
import com.zorkdata.center.admin.entity.ActionGroup;

import java.util.List;

/**
 * @author: huziyue
 * @create: 2018/3/30 18:10
 */
public interface ActionServiceIfc {

    /**
     * 新增一条action
     *
     * @param action
     */
    void insert(Action action,String productCode);

    /**
     * 更新一条action
     *
     * @param action
     */
    void updateSelective(Action action,String productCode);

    /**
     * 根据id删除action
     *
     * @param actionIds
     */
    void deleteActionByIds(List<Long> actionIds);


    /**
     * 获取所有的功能
     *
     * @return
     */
    List<Action> getAllAction();

    /**
     * 获取所有的功能组
     *
     * @return
     */
    List<ActionGroup> getActionGroup(String productCode);

    /**
     * 添加一个功能组
     * @param actionGroup
     */
    void addActionGroup(ActionGroup actionGroup,String productCode);

    /**
     * 编辑一个功能组
     * @param actionGroup
     */
    void updateActionGroup(ActionGroup actionGroup,String productCode);

    /**
     * 删除一个功能组
     * @param actionGroupID
     */
    void deleteActionGroup(Long actionGroupID);
}
