package com.zorkdata.center.admin.mapper;

import com.zorkdata.center.admin.entity.Computer;
import com.zorkdata.center.admin.entity.Master;
import com.zorkdata.center.admin.entity.Test;
import com.zorkdata.center.admin.vo.MasterInfoVo;
import com.zorkdata.center.admin.vo.MasterVo2;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface MasterMapper extends Mapper<Master> {
    @Override
    List<Master> selectAll();

    void deleteByComputerID(@Param("computerID") Long computerID);

    Master selectByComputerID(@Param("computerID") Long computerID);

    Master selectByComputerIP(@Param("computerIP") String computerIP);

    Long insertMaster(Master master);

    void deleteByComputerIds(@Param("computerIDlist") Set<Long> computerIDlist);

    void updateMaster(Map<String, Object> map);

    void updateMasterbyIP(Map<String, Object> map);

    List<Map<String, Object>> getApiInfo();

    List<MasterVo2> getMasterVo2();

    List<Computer> getMasterComputerByClusterID(@Param("clusterID")Long clusterID);

    /**
     * 根据agentname获取master的ip和操作系统
     * @param agentName
     * @return
     */
    MasterInfoVo getMasterIPAndOstypeByAgentName(String agentName);
}
