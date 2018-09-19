package com.zorkdata.center.adminv2.mapper;

import com.zorkdata.center.adminv2.entity.AppSystem;
import com.zorkdata.center.adminv2.vo.AppClusterVo;
import com.zorkdata.center.adminv2.vo.AppProgramVo;
import com.zorkdata.center.adminv2.vo.AppSystemInfoVo;
import com.zorkdata.center.adminv2.vo.AppSystemVo;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/6/5 13:16
 */
@Repository
public interface AppSystemMapper extends Mapper<AppSystem> {
    List<AppSystemInfoVo> getSystemInfo();

    List<AppSystemInfoVo> getSystemInfoByUserName(String userName);

    List<AppClusterVo> getClusterBySysInfo(Map<String, Object> map);
}
