package com.zorkdata.center.admin.mapper;

import com.zorkdata.center.admin.entity.Agent;
import com.zorkdata.center.admin.entity.ClusterComputerRelation;
import com.zorkdata.center.admin.entity.Computer;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Set;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/24 14:59
 */
@Repository
public interface ClusterComputerRelationMapper extends Mapper<ClusterComputerRelation> {
    List<Computer> getAllClusterComputers();

    void deleteByclusterID(Long clusterID);

    void insertBatch(ClusterComputerRelation clusterComputerRelation);

}
