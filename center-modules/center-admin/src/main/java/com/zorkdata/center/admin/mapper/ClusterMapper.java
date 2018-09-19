package com.zorkdata.center.admin.mapper;

import com.zorkdata.center.admin.entity.Cluster;
import com.zorkdata.center.admin.entity.Computer;
import com.zorkdata.center.admin.vo.ClusterInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Set;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/24 10:42
 */
@Repository
public interface ClusterMapper extends Mapper<Cluster> {

    /**
     * 插入数据并返回ID
     *
     * @param cluster
     */
    void insertSelectiveGetId(Cluster cluster);

    /**
     * 删除一个集群数据
     *
     * @param ids 集群id集合
     */
    void deleteByIds(@Param("ids") Set<Long> ids);

    /**
     * 根据集群id获取计算机信息
     *
     * @param clusterID
     * @return
     */
    List<Computer> getComputersByClusterID(@Param("clusterID") Long clusterID);


    /**
     * 获取所有未分配的机器列表
     *
     * @return
     */
    List<Computer> getAllNoClusterComputers();

    List<ClusterInfo> getMasterAndAgentInfoInCluster(@Param("computerIds") Set<Long> computerIds);

    List<ClusterInfo> getNoMasterInfoInCluster();
}
