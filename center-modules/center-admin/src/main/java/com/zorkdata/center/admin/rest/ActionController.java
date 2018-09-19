package com.zorkdata.center.admin.rest;

import com.alibaba.fastjson.JSON;
import com.zorkdata.center.admin.entity.Action;
import com.zorkdata.center.admin.entity.ActionGroup;
import com.zorkdata.center.admin.rpc.service.Ifc.ActionServiceIfc;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author: huziyue
 * @create: 2018/3/30 18:14
 */
@RestController
@RequestMapping("action")
public class ActionController {
    @Autowired
    private ActionServiceIfc actionService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public RespModel addAction(@RequestBody Map request) {
        Action action = JSON.parseObject(String.valueOf(request.get("action")), Action.class);
        String productCode = String.valueOf(request.get("productCode"));
        actionService.insert(action,productCode);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/addActionGroup", method = RequestMethod.POST)
    @ResponseBody
    public RespModel addActionGroup(@RequestBody Map request) {
        ActionGroup actionGroup = JSON.parseObject(String.valueOf(request.get("actionGroup")), ActionGroup.class);
        String productCode = String.valueOf(request.get("productCode"));
        actionService.addActionGroup(actionGroup,productCode);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/updateActionGroup", method = RequestMethod.POST)
    @ResponseBody
    public RespModel updateActionGroup(@RequestBody Map request) {
        ActionGroup actionGroup = JSON.parseObject(String.valueOf(request.get("actionGroup")), ActionGroup.class);
        String productCode = String.valueOf(request.get("productCode"));
        actionService.updateActionGroup(actionGroup,productCode);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/deleteActionGroup", method = RequestMethod.POST)
    @ResponseBody
    public RespModel deleteActionGroup(@RequestBody Long actionGroupID) {
        actionService.deleteActionGroup(actionGroupID);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public RespModel updateAction(@RequestBody Map request) {
        Action action = JSON.parseObject(String.valueOf(request.get("action")), Action.class);
        String productCode = String.valueOf(request.get("productCode"));
        actionService.updateSelective(action,productCode);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public RespModel deleteAction(@RequestBody Map request) {
        List<Long> actionIds = JSON.parseArray(String.valueOf(request.get("actionIds")), Long.TYPE);
        actionService.deleteActionByIds(actionIds);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/getAllAction", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getAllAction() {
        List<Action> allAction = actionService.getAllAction();
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, allAction);
    }

    @RequestMapping(value = "/getActionGroup", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getActionGroup(@RequestParam String productCode) {
        List<ActionGroup> ActionGroup = actionService.getActionGroup(productCode);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, ActionGroup);
    }
}
