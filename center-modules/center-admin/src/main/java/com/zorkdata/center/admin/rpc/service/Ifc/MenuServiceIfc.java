package com.zorkdata.center.admin.rpc.service.Ifc;

import com.zorkdata.center.admin.entity.Menu;
import com.zorkdata.center.admin.vo.MenuVo;

import java.util.List;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/27 11:45
 */
public interface MenuServiceIfc {
    /**
     * 新建菜单栏
     *
     * @param menu
     */
    void addMenu(Menu menu);

    /**
     * 编辑菜单栏
     *
     * @param menu
     */
    void updateMenu(Menu menu);

    /**
     * 通过id批量删除菜单
     *
     * @param ids
     */
    void deleteMenuByIds(Long[] ids);

    /**
     * 获取所有菜单
     *
     * @return
     */
    List<MenuVo> getAllMenu();

}
