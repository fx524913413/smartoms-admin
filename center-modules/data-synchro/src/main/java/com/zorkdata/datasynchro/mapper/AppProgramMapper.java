package com.zorkdata.datasynchro.mapper;

import com.zorkdata.datasynchro.entity.AppProgram;
import com.zorkdata.datasynchro.vo.AppProgramVo;
import com.zorkdata.datasynchro.vo.AppSystemVo;
import com.zorkdata.datasynchro.vo.UserAppSystem;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Repository
public interface AppProgramMapper extends Mapper<AppProgram> {
    List<AppSystemVo> getAppProgramInfo();

    List<UserAppSystem> getUserAppSystem();

    List<AppSystemVo> getBizToModule();
}