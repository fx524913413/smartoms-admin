package com.zorkdata.datasynchro.biz;


import com.zorkdata.center.common.biz.BaseBiz;
import com.zorkdata.datasynchro.entity.Computer;
import com.zorkdata.datasynchro.entity.User;
import com.zorkdata.datasynchro.mapper.ComputerMapper;
import com.zorkdata.datasynchro.mapper.UserMapper;
import com.zorkdata.datasynchro.vo.AppSystemVo;
import com.zorkdata.datasynchro.vo.BizVo;
import com.zorkdata.datasynchro.vo.BizVo2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Administrator
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ComputerBiz extends BaseBiz<ComputerMapper, Computer> {
    public List<Computer> getAllComputer() {
        return mapper.getAllComputer();
    }

    public List<BizVo2> getBizToComputer() {
        return mapper.getBizToComputer();
    }
}
