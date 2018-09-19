package com.zorkdata.center.admin.mapper;

import com.zorkdata.center.admin.entity.Action;
import com.zorkdata.center.admin.entity.ActionGroup;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Set;

/**
 * @author: huziyue
 * @create: 2018/3/30 18:09
 */
@Repository
public interface ActionMapper extends Mapper<Action> {

    /**
     * 更新一条action
     *
     * @param action
     */
    void updateByActionID(Action action);

    /**
     * 根据id删除action
     *
     * @param actionIds
     */
    void deleteActionByIds(@Param("actionIds") List<Long> actionIds);

    /**
     * 通过关联的actionid查询所有的action
     *
     * @param actionIds
     * @return
     */
    List<Action> getActionByResourceID(@Param("actionIds") Set<Long> actionIds);

    /**
     * 根据产品id查询所有的action
     *
     * @param productID
     * @return
     */
    List<Action> getActionByProductID(Integer productID);

    /**
     * 获取功能是否可修改用户资源的id
     *
     * @return
     */
    Long getActionIDByActionCode(String actionCode);

    /**
     * 获取所有的功能组
     *
     * @return
     */
    List<ActionGroup> getActionGroup(Integer productID);

    /**
     * 添加一个功能组
     * @param actionGroup
     */
    void addActionGroup(ActionGroup actionGroup);

    /**
     * 编辑一个功能组
     * @param actionGroup
     */
    void updateActionGroup(ActionGroup actionGroup);

    /**
     * 删除一个功能组
     * @param actionGroupID
     */
    void deleteActionGroup(Long actionGroupID);
}
