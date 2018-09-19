package com.zorkdata.center.admin.rest;

import com.alibaba.fastjson.JSON;
import com.zorkdata.center.admin.biz.UserBiz;
import com.zorkdata.center.admin.entity.Role;
import com.zorkdata.center.admin.entity.User;
import com.zorkdata.center.admin.rpc.service.Ifc.ProductServiceIfc;
import com.zorkdata.center.admin.rpc.service.Ifc.RoleServiceIfc;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import com.zorkdata.center.common.rest.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description: ${todo}
 * @author: huziyue
 * @create: 2018/3/21 14:17
 */
@RestController
@RequestMapping("role")
public class RoleController extends BaseController<UserBiz, User> {

    @Autowired
    private RoleServiceIfc roleService;

    @Autowired
    private ProductServiceIfc productService;

    /**
     * 根据产品code获取所有角色
     *
     * @param productCode
     * @return
     */
    @RequestMapping(value = "/getRoleByProductID", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getRoleByProductID(@RequestParam String productCode) {
        List<Role> roles = roleService.getRoleByProductCode(productCode);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, roles);
    }

    /**
     * 添加一个角色
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public RespModel addRole(@RequestBody Map request) {
        Long userID = Long.parseLong(String.valueOf(request.get("userID")));
        String productCode = String.valueOf(request.get("productCode"));
        Role role = JSON.parseObject(String.valueOf(request.get("role")), Role.class);
        //所有新建角色状态默认为草稿状态
        Role roleByRoleName = roleService.getRoleByRoleNameAndProductCode(role.getRoleName(), productCode,null);
        if (roleByRoleName != null) {
            return RespTools.getRespMsgModel(CodeTable.ALREADY_EXIST, null);
        }
        role.setState(1);
        role.setCreateTime(new Date());
        Boolean flag = roleService.addRole(role, productCode, userID);
        if (flag) {
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
        }
        return RespTools.getRespMsgModel(CodeTable.NOT_ALLOW, null);
    }

    /**
     * 编辑一个角色
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public RespModel updateRole(@RequestBody Map request) {
        Long userID = Long.parseLong(String.valueOf(request.get("userID")));
        String productCode = String.valueOf(request.get("productCode"));
        Role role = JSON.parseObject(String.valueOf(request.get("role")), Role.class);
        Role roleByRoleName = roleService.getRoleByRoleNameAndProductCode(role.getRoleName(), productCode,role.getRoleID());
        if (roleByRoleName != null&&roleByRoleName.getRoleName().equals(role.getRoleName())) {
            return RespTools.getRespMsgModel(CodeTable.ALREADY_EXIST, null);
        }
        Boolean flag = roleService.updateRole(role, productCode, userID);
        if (flag) {
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
        }
        return RespTools.getRespMsgModel(CodeTable.NOT_ALLOW, null);
    }

    /**
     * 批量删除角色
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public RespModel deleteRole(@RequestBody Map request) {
        Long userID = Long.parseLong(String.valueOf(request.get("userID")));
        String productCode = String.valueOf(request.get("productCode"));
        List<Long> ids = JSON.parseArray(String.valueOf(request.get("ids")), Long.TYPE);
        Boolean flag = roleService.deleteRoleByIds(ids, productCode, userID);
        if (flag) {
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
        }
        return RespTools.getRespMsgModel(CodeTable.NOT_ALLOW, null);
    }

    /**
     * 给当前角色添加用户
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/addUserToRole", method = RequestMethod.POST)
    @ResponseBody
    public RespModel addUserToRole(@RequestBody Map request) {
        Long userID = Long.parseLong(String.valueOf(request.get("userID")));
        String productCode = String.valueOf(request.get("productCode"));
        List<Long> userIds = JSON.parseArray(String.valueOf(request.get("userIds")), Long.TYPE);
        Long roleId = Long.parseLong(String.valueOf(request.get("roleId")));
        Boolean flag = roleService.addUserToRole(userIds, roleId, productCode, userID);
        if (flag) {
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
        }
        return RespTools.getRespMsgModel(CodeTable.NOT_ALLOW, null);
    }

    /**
     * 解除当前角色与用户的关系
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteUserToRole", method = RequestMethod.POST)
    @ResponseBody
    public RespModel deleteUserToRole(@RequestBody Map request) {
        Long userID = Long.parseLong(String.valueOf(request.get("userID")));
        String productCode = String.valueOf(request.get("productCode"));
        List<Long> userIds = JSON.parseArray(String.valueOf(request.get("userIds")), Long.TYPE);
        Long roleId = Long.parseLong(String.valueOf(request.get("roleId")));
        Boolean flag = roleService.deleteUserToRole(userIds, roleId, productCode, userID);
        if (flag) {
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
        }
        return RespTools.getRespMsgModel(CodeTable.NOT_ALLOW, null);
    }

    /**
     * 获取角色下的所有用户
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getUserByRole", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getUserByRole(@RequestParam Map request) {
        Long roleId = Long.parseLong(String.valueOf(request.get("roleID")));
        List<User> userByRole = roleService.getUserByRole(roleId);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, userByRole);
    }

    /**
     * 获取用户下的所有角色
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getRoleByUser", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getRoleByUser(@RequestParam Map request) {
        Long userID = Long.parseLong(String.valueOf(request.get("userID")));
        List<Role> userByRole = roleService.getRoleByUser(userID);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, userByRole);
    }

    /**
     * 给一批用户赋予多个角色
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/addUsersToRoles", method = RequestMethod.POST)
    @ResponseBody
    public RespModel addUsersToRoles(@RequestBody Map request) {
        List<Long> userIds = JSON.parseArray(String.valueOf(request.get("userIds")), Long.TYPE);
        List<Long> roleIds = JSON.parseArray(String.valueOf(request.get("roleIds")), Long.TYPE);
        Long userID = Long.parseLong(String.valueOf(request.get("userID")));
        String productCode = String.valueOf(request.get("productCode"));
        Boolean flag = roleService.addUsersToRoles(userIds, roleIds, userID, productCode);
        if (flag) {
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
        }
        return RespTools.getRespMsgModel(CodeTable.NOT_ALLOW, null);
    }

    /**
     * 判断当前角色是否拥有用户
     *
     * @param roleID
     * @return
     */
    @RequestMapping(value = "/isHaveUser", method = RequestMethod.GET)
    @ResponseBody
    public RespModel isHaveUser(@RequestParam Long roleID) {
        Boolean flag = roleService.isHaveUser(roleID);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, flag);
    }
}
