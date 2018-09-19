package com.zorkdata.datasynchro.mapper;

import com.zorkdata.datasynchro.entity.Computer;
import com.zorkdata.datasynchro.vo.AppSystemVo;
import com.zorkdata.datasynchro.vo.BizVo;
import com.zorkdata.datasynchro.vo.BizVo2;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ComputerMapper extends Mapper<Computer> {
    /**
     * 获取所有机器
     * @return
     */
    List<Computer> getAllComputer();

    /**
     * 获取业务到机器之间的关系
     * @return
     */
    List<BizVo2> getBizToComputer();
}
