package com.zorkdata.center.adminv2.mapper;

import com.zorkdata.center.adminv2.entity.AppProgram;
import com.zorkdata.center.adminv2.vo.AppProgramVo;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Repository
public interface AppProgramMapper extends Mapper<AppProgram> {
    List<AppProgramVo> getAppProgramInfo(Map<String, Object> map);
}