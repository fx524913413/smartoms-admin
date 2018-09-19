package com.zorkdata.center.admin.rest;

import com.alibaba.fastjson.JSON;
import com.zorkdata.center.admin.entity.Group;
import com.zorkdata.center.admin.rpc.service.Ifc.GroupServiceIfc;
import com.zorkdata.center.admin.vo.GroupTreeNode;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import com.zorkdata.center.common.util.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author: huziyue
 * @create: 2018/3/21 16:28
 */
@RestController
@RequestMapping("group")
public class GroupController {
    @Autowired
    private GroupServiceIfc groupService;

    /**
     * 添加一个用户组
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public RespModel addGroup(@RequestBody Map request) {
        Long userID = Long.parseLong(String.valueOf(request.get("userID")));
        String productCode = String.valueOf(request.get("productCode"));
        Group group = JSON.parseObject(String.valueOf(request.get("group")), Group.class);
        Group group1 = groupService.getGroupByGroupName(group.getGroupName(), group.getParentID(),null);
        if (group1 != null) {
            return RespTools.getRespMsgModel(CodeTable.ALREADY_EXIST, null);
        }
        group.setCreateTime(new Date());
        Boolean flag = groupService.insertSelective(group, userID, productCode);
        if(flag==true){
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
        }
        return RespTools.getRespMsgModel(CodeTable.NOT_ALLOW, null);
    }

    /**
     * 编辑一个用户组
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public RespModel updateGroup(@RequestBody Map request) {
        Long userID = Long.parseLong(String.valueOf(request.get("userID")));
        String productCode = String.valueOf(request.get("productCode"));
        Group group = JSON.parseObject(String.valueOf(request.get("group")), Group.class);
        Group group1 = groupService.getGroupByGroupName(group.getGroupName(), group.getParentID(),group.getGroupID());
        if (group1 != null&&group1.getGroupName().equals(group.getGroupName())&&group1.getParentID().equals(group.getParentID())) {
            return RespTools.getRespMsgModel(CodeTable.ALREADY_EXIST, null);
        }
        Boolean flag = groupService.updateSelective(group, userID, productCode);
        if(flag==true){
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
        }
        return RespTools.getRespMsgModel(CodeTable.NOT_ALLOW, null);
    }

    /**
     * 删除一个用户组及其下所有用户组
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public RespModel deleteGroup(@RequestBody Map request) {
        List<GroupTreeNode> treeNodes = groupService.selectList();
        Long userID = Long.parseLong(String.valueOf(request.get("userID")));
        String productCode = String.valueOf(request.get("productCode"));
        Set<Long> ids = TreeUtil.getAllId(Long.parseLong(String.valueOf(request.get("groupID"))), treeNodes);
        Boolean flag=groupService.deleteGroupById(ids,userID,productCode);
        if(flag==true){
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
        }
        return RespTools.getRespMsgModel(CodeTable.NOT_ALLOW, null);
    }

    /**
     * 获取用户组树
     *
     * @return
     */
    @RequestMapping(value = "/getGroupTree", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getGroupTree() {
        List<GroupTreeNode> treeNodes = groupService.selectList();
        List<GroupTreeNode> bulid = TreeUtil.bulid(treeNodes, -1L);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, bulid);
    }

    /**
     * 判断当前角色是否拥有用户
     *
     * @param groupID
     * @return
     */
    @RequestMapping(value = "/isHaveUser", method = RequestMethod.GET)
    @ResponseBody
    public RespModel isHaveUser(@RequestParam Long groupID) {
        Boolean flag = groupService.isHaveUser(groupID);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, flag);
    }
}
