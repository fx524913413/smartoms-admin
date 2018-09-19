package com.zorkdata.datasynchro.biz;


import com.zorkdata.center.common.biz.BaseBiz;
import com.zorkdata.datasynchro.entity.AppProgram;
import com.zorkdata.datasynchro.mapper.AppProgramMapper;
import com.zorkdata.datasynchro.vo.AppProgramVo;
import com.zorkdata.datasynchro.vo.AppSystemVo;
import com.zorkdata.datasynchro.vo.UserAppSystem;
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
    public List<AppSystemVo> getSystemInfo() {
        return mapper.getAppProgramInfo();
    }

    public List<UserAppSystem> getUserAppSystem() {
        return mapper.getUserAppSystem();
    }

    public List<AppSystemVo> getBizToModule() {
        return mapper.getBizToModule();
    }
}
