package com.zorkdata.center.auth.feign;

import com.zorkdata.center.auth.configuration.FeignConfiguration;
import com.zorkdata.center.auth.vo.UserInfo;
import com.zorkdata.center.common.core.RespModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/20 15:34
 */
@FeignClient(value = "center-admin", configuration = FeignConfiguration.class)
public interface IUserService {
    @RequestMapping(value = "/api/user/validate", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public RespModel validate(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("productCode") String productCode);
    @RequestMapping(value = "/api/user/get_all_user", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
    public RespModel getAllUser();
}
