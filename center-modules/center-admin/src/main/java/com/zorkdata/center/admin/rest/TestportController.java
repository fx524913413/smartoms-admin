package com.zorkdata.center.admin.rest;

import com.zorkdata.center.admin.rpc.service.Ifc.TestportServiceIfc;
import com.zorkdata.center.admin.vo.TestPortResults;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("test")
public class TestportController {
    @Autowired
    TestportServiceIfc testportServiceIfc;

    @RequestMapping(value = "/test22port", method = RequestMethod.POST)
    @ResponseBody
    public RespModel test22port(@RequestBody String tarhost) {
        List<TestPortResults> results = testportServiceIfc.test22port(tarhost);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, results);
    }
}
