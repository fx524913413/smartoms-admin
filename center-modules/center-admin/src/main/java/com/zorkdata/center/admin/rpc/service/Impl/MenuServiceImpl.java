package com.zorkdata.center.admin.rpc.service.Impl;

import com.zorkdata.center.admin.biz.MenuBiz;
import com.zorkdata.center.admin.entity.Menu;
import com.zorkdata.center.admin.rpc.service.Ifc.MenuServiceIfc;
import com.zorkdata.center.admin.vo.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/27 11:45
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MenuServiceImpl implements MenuServiceIfc {
    @Autowired
    MenuBiz menuBiz;

    @Override
    public void addMenu(Menu menu) {
        menuBiz.insertSelective(menu);
    }

    @Override
    public void updateMenu(Menu menu) {
        menuBiz.updateByMenuId(menu);
    }

    @Override
    public void deleteMenuByIds(Long[] ids) {
        menuBiz.deleteMenuByIds(ids);
    }

    @Override
    public List<MenuVo> getAllMenu() {
        return menuBiz.getAllMenu();
    }
}
