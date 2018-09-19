package com.zorkdata.center.admin.rest;

import com.zorkdata.center.admin.biz.TestBiz;
import com.zorkdata.center.admin.entity.Test;
import com.zorkdata.center.admin.rpc.service.PermissionService;
import com.zorkdata.center.admin.vo.TestInfo;
import com.zorkdata.center.common.rest.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/16 10:52
 */
@RestController
@RequestMapping("test")
// 测试http://localhost:8762/test/info?username=admin&password=admin
public class TestController extends BaseController<TestBiz, Test> {
    @Autowired
    private PermissionService permissionService;

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getUserInfo(String username, String password) throws Exception {
        TestInfo Info = permissionService.validate(username, password);
        if (Info == null) {
            return ResponseEntity.status(401).body(false);
        } else {
            return ResponseEntity.ok(Info);
        }
    }
}
