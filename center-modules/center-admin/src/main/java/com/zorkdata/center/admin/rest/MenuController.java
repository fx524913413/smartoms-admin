package com.zorkdata.center.admin.rest;

import com.zorkdata.center.admin.entity.Menu;
import com.zorkdata.center.admin.rpc.service.Ifc.MenuServiceIfc;
import com.zorkdata.center.admin.vo.MenuVo;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/30 15:06
 */
@RestController
@RequestMapping("menu")
public class MenuController {
    @Autowired
    private MenuServiceIfc menuService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public RespModel addMenu(@RequestBody Menu menu) {
        menu.setCreateTime(new Date());
        menuService.addMenu(menu);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public RespModel updateMenu(@RequestBody Menu menu) {
        menuService.updateMenu(menu);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public RespModel deleteMenu(@RequestBody Long[] ids) {
        menuService.deleteMenuByIds(ids);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    /**
     * 获取所有菜单
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/getAllMenu", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getAllMenu() {
        List<MenuVo> menus = menuService.getAllMenu();
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, menus);
    }
}
