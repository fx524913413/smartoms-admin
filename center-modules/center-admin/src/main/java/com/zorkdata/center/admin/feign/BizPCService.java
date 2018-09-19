package com.zorkdata.center.admin.feign;


import com.zorkdata.center.admin.config.FeignConfiguration;
import com.zorkdata.center.common.core.RespModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.InputStream;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/20 15:34
 */
@FeignClient(value = "center-admin-v2", configuration = FeignConfiguration.class)
public interface BizPCService {
    @RequestMapping(value = "/beats/stopservice", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    RespModel stopService(@RequestParam("typeName") String typeName, @RequestParam("worknodeid") String worknodeid);

    @RequestMapping(value = "/beats/sendfile", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    RespModel fileUpload(@RequestParam("filePath") String filePath, @RequestParam("worknodeid") String worknodeid);

    @RequestMapping(value = "/beats/sendexec", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    RespModel execUpload(@RequestParam("filePath") String filePath, @RequestParam("worknodeid") String worknodeid);

    @RequestMapping(value = "/beats/sendyml", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    RespModel metricUpload(@RequestParam("filePath") String filePath, @RequestParam("worknodeid") String worknodeid);

    @RequestMapping(value = "/appsystem/getAppSystemInfo", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
    RespModel getOldAgents();

    @RequestMapping(value = "/appsystem/getAppSystemInfoByUserName", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    String getOldAgentsByUserName(@RequestParam("userName") String userName);

    @RequestMapping(value = "/appprogram/getAppProgramInfo", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    String getAppProgramInfo(@RequestParam("systemid") String systemid);

    @RequestMapping(value = "/appsystem/getClusterBySys", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    String getClusterBySys(@RequestParam("systemId") String systemId);
}
