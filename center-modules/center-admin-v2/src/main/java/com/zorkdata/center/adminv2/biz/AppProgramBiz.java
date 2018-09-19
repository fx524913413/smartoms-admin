package com.zorkdata.center.adminv2.biz;


import com.zorkdata.center.adminv2.entity.AppProgram;
import com.zorkdata.center.adminv2.mapper.AppProgramMapper;
import com.zorkdata.center.adminv2.vo.AppProgramVo;
import com.zorkdata.center.common.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AppProgramBiz extends BaseBiz<AppProgramMapper, AppProgram> {
    public List<AppProgramVo> getSystemInfo(Map<String, Object> map) {
        return mapper.getAppProgramInfo(map);
    }
}
