package com.zorkdata.center.admin.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zorkdata.center.admin.biz.UserBiz;
import com.zorkdata.center.admin.entity.User;
import com.zorkdata.center.admin.rpc.service.Ifc.ProductServiceIfc;
import com.zorkdata.center.admin.rpc.service.Ifc.UserServiceIfc;
import com.zorkdata.center.admin.vo.UserInfo;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import com.zorkdata.center.common.rest.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description: ${todo}
 * @author: huziyue
 * @create: 2018/3/20 10:07
 */
@RestController
@RequestMapping("user")
public class UserController extends BaseController<UserBiz, User> {
    @Autowired
    private UserServiceIfc userService;

    @Autowired
    private ProductServiceIfc productService;
    //测试地址：http://localhost:8762/user/info?userName=lisi&password=123456

    /**
     * 获取单个用户信息
     * @param userName
     * @param password
     * @return
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getUserInfo(String userName, String password,String productCode) {
        UserInfo userInfo = null;
        try {
            userInfo = userService.validate(userName, password,productCode);
            if (userInfo == null) {
                return RespTools.getRespMsgModel(CodeTable.FAILED, null);
            } else {
                return RespTools.getRespMsgModel(CodeTable.SUCCESS, userInfo);
            }
        } catch (Exception e) {
            return RespTools.getRespMsgModel(CodeTable.FAILED, null);
        }
    }

    /**
     * 添加一个用户
     * @param
     * @return
     */
    @RequestMapping(value = "/add", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public RespModel addUser(@RequestBody Map request) {
        User user = JSON.parseObject(String.valueOf(request.get("user")), User.class);
        Long userIds = Long.parseLong(String.valueOf(request.get("userIds")));
        String productCode = String.valueOf(request.get("productCode"));
        Long groupID = Long.parseLong(String.valueOf(request.get("groupID")));
        User user1 = userService.getUserByUserName(user.getUserName(),null);
        if (user1 != null) {
            return RespTools.getRespMsgModel(CodeTable.ALREADY_EXIST, null);
        }
        user.setCreateTime(new Date());
        Boolean flag = userService.insert(userIds, productCode, user, groupID);
        if(flag){
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
        }
        return RespTools.getRespMsgModel(CodeTable.NOT_ALLOW, null);
    }

    /**
     * 编辑用户
     * @param
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public RespModel updateUser(@RequestBody Map request) {
        User user = JSON.parseObject(String.valueOf(request.get("user")), User.class);
        Long userIds = Long.parseLong(String.valueOf(request.get("userIds")));
        String productCode = String.valueOf(request.get("productCode"));
        User user1 = userService.getUserByUserName(user.getUserName(),user.getUserID());
        if (user1 != null&& user1.getUserName().equals(user.getUserName())) {
            return RespTools.getRespMsgModel(CodeTable.ALREADY_EXIST, null);
        }
        Boolean flag = userService.updateSelective(userIds, productCode, user);
        if(flag){
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
        }
        return RespTools.getRespMsgModel(CodeTable.NOT_ALLOW, null);
    }

    /**
     * 删除用户
     * @param request
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public RespModel deleteUser(@RequestBody Map request) {
        Long userID = Long.parseLong(String.valueOf(request.get("userID")));
        String productCode = String.valueOf(request.get("productCode"));
        List<Long> ids = JSON.parseArray(String.valueOf(request.get("ids")), Long.TYPE);
        Boolean flag = userService.deleteUserByIds(userID, productCode, ids);
        if(flag){
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
        }
        return RespTools.getRespMsgModel(CodeTable.NOT_ALLOW, null);
    }

    /**
     * 根据组id获取用户
     * @param groupID
     * @return
     */
    @RequestMapping(value = "/getUserByGroupId", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getUserByGroupId(Long groupID) {
        List<User> users = userService.getUserByGroupId(groupID);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, users);
    }

    /**
     * 获取所有的用户
     * @return
     */
    @RequestMapping(value = "/getAllUser", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getAllUser() {
        List<User> users = userService.getAllUser();
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, users);
    }

    /**
     * 根据产品id获取用户
     * @param request
     * @return
     */
    @RequestMapping(value = "/getUserByProduct", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getUserByProduct(@RequestParam Map request) {
        String productCode = String.valueOf(request.get("productCode"));
        List<User> users = userService.getUserByProduct(productCode);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, users);
    }

    /**
     * 修改角色与用户的关系
     * @param request
     * @return
     */
    @RequestMapping(value = "/editRoleToUser", method = RequestMethod.POST)
    @ResponseBody
    public RespModel editRoleToUser(@RequestBody Map request) {
        Long userID = Long.parseLong(String.valueOf(request.get("userID")));
        Map<String, List<Long>> roleIds =
                JSON.parseObject(String.valueOf(request.get("roleIds")), new TypeReference<Map<String, List<Long>>>() {});
        Long editUserID = Long.parseLong(String.valueOf(request.get("editUserID")));
        String productCode = String.valueOf(request.get("productCode"));
        Boolean flag=userService.editRoleToUser(userID,roleIds,editUserID,productCode);
        if(flag){
            return RespTools.getRespMsgModel(CodeTable.SUCCESS,null);
        }
        return RespTools.getRespMsgModel(CodeTable.NOT_ALLOW,null);
    }

    /**
     * 判断用户是否绑定其他产品
     * @param
     * @return
     */
    @RequestMapping(value = "/inOtherProduct", method = RequestMethod.GET)
    @ResponseBody
    public RespModel inOtherProduct(@RequestParam Long userID,@RequestParam String productCode) {
        Boolean flag=userService.inOtherProduct(userID,productCode);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, flag);
    }
}
