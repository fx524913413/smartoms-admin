package com.zorkdata.datasynchro.biz;


import com.zorkdata.center.common.biz.BaseBiz;
import com.zorkdata.datasynchro.entity.AppProgram;
import com.zorkdata.datasynchro.entity.User;
import com.zorkdata.datasynchro.mapper.AppProgramMapper;
import com.zorkdata.datasynchro.mapper.UserMapper;
import com.zorkdata.datasynchro.vo.AppProgramVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserBiz extends BaseBiz<UserMapper, User> {
    public List<User> getAllUser() {
        return mapper.getAllUser();
    }
}
