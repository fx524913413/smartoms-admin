package com.zorkdata.center.admin.rpc;

import com.zorkdata.center.admin.rpc.service.PermissionService;
import com.zorkdata.center.admin.vo.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/16 11:02
 */
@RestController
@RequestMapping("api")
public class TestRest {
    @Autowired
    private PermissionService permissionService;

    @RequestMapping(value = "/test/validate", method = RequestMethod.POST)
    public @ResponseBody
    TestInfo validate(String username, String password) {
        return permissionService.validate(username, password);
    }
}
