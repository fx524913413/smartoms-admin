package com.zorkdata.center.adminv2.biz;

import com.zorkdata.center.adminv2.entity.AppSystem;
import com.zorkdata.center.adminv2.mapper.AppSystemMapper;
import com.zorkdata.center.adminv2.vo.AppClusterVo;
import com.zorkdata.center.adminv2.vo.AppSystemInfoVo;
import com.zorkdata.center.common.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/6/5 14:51
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AppSystemBiz extends BaseBiz<AppSystemMapper, AppSystem> {
    public List<AppSystemInfoVo> getSystemInfo() {
        return mapper.getSystemInfo();
    }

    public List<AppSystemInfoVo> getSystemInfoByUserName(String userName) {
        return  mapper.getSystemInfoByUserName(userName);
    }

    public List<AppClusterVo> getClusterBySysInfo(Map<String, Object> map){
        return mapper.getClusterBySysInfo(map);
    }
}
