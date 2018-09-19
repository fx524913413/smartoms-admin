package com.zorkdata.datasynchro.rest;

import com.zorkdata.center.common.util.HttpClientUtil;
import com.zorkdata.datasynchro.biz.AppProgramBiz;
import com.zorkdata.datasynchro.biz.UserBiz;
import com.zorkdata.datasynchro.entity.BKToken;
import com.zorkdata.datasynchro.entity.User;
import com.zorkdata.datasynchro.login.BKLoginUtil;
import com.zorkdata.datasynchro.vo.AppProgramVo;
import com.zorkdata.datasynchro.vo.AppSystemVo;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {
    final private static String USERNAME="admin";
    final private static String PASSWORD="zorkdata";
    @Autowired
    private AppProgramBiz appProgramBiz;

    @Autowired
    private UserBiz userBiz;


    @GetMapping("test")
    public String test(){
        List<AppSystemVo> systemInfo = appProgramBiz.getSystemInfo();
        return "success";
    }

    @RequestMapping(value = "/getBiz", method = RequestMethod.GET)
    public List<AppSystemVo> getBiz(){
        List<AppSystemVo> systemInfo = appProgramBiz.getBizToModule();
        return systemInfo;
    }

    @RequestMapping(value = "/getuser", method = RequestMethod.POST)
    public void getuser(){
        List<User> allUser = userBiz.getAllUser();
        BKToken bkToken = BKLoginUtil.Login(USERNAME, PASSWORD);
        Map<String, String> header = new HashMap<>();
        header.put("X-CSRFToken", bkToken.getBklogin_csrftoken());
        header.put("Cookie", "bk_token=" + bkToken.getBk_token() + ";" + "bklogin_csrftoken=" + bkToken.getBklogin_csrftoken());;
        for (User user:allUser){
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", user.getUsername()));
            params.add(new BasicNameValuePair("chname", user.getChname()));
            params.add(new BasicNameValuePair("qq", user.getQQ()));
            params.add(new BasicNameValuePair("phone", "11111111111"));
            params.add(new BasicNameValuePair("role", "0"));
            if(user.getEmail()==null){
                params.add(new BasicNameValuePair("email", "1@qq.com"));
            }else {
                params.add(new BasicNameValuePair("email", user.getEmail().replace(",","").trim()));
            }
            try {
                HttpClientUtil.sendHttpPostForm("http://paas.blueking.com/login/accounts/save_user/", params, header);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}

