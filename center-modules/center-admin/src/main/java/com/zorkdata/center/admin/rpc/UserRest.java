package com.zorkdata.center.admin.rpc;

import com.zorkdata.center.admin.entity.User;
import com.zorkdata.center.admin.rpc.service.Ifc.UserServiceIfc;
import com.zorkdata.center.admin.vo.UserInfo;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description: ${todo}
 * @author: huziyue
 * @create: 2018/3/20 18:25
 */
@RestController
@RequestMapping("api")
public class UserRest {
    @Autowired
    private UserServiceIfc userService;

    @RequestMapping(value = "/user/validate", method = RequestMethod.POST)
    @ResponseBody
    public RespModel validate(String username, String password, String productCode) {
        UserInfo userInfo = null;
        try {
            userInfo = userService.validate(username, password, productCode);
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, userInfo);
        } catch (Exception e) {
            String message = e.getMessage();
            if ("用户不存在".equals(message)) {
                return RespTools.getRespMsgModel(CodeTable.ACOUNT_NOTEXIST, userInfo);
            } else {
                return RespTools.getRespMsgModel(CodeTable.NOT_IN_PRODUCT, userInfo);
            }
        }
    }

    @RequestMapping(value = "/user/get_all_user", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getAllUser() {
        try {
            List<User> user = userService.getAllUser();
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, user);
        } catch (Exception e) {
            return RespTools.getRespMsgModel(CodeTable.UNKNOWN_ERROR, null);
        }
    }
}
