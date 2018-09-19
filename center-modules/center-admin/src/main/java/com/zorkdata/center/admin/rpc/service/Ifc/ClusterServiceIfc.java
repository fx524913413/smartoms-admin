package com.zorkdata.center.admin.rpc.service.Ifc;

import com.zorkdata.center.admin.entity.Cluster;
import com.zorkdata.center.admin.entity.Computer;
import com.zorkdata.center.admin.vo.ClusterInfo;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

/**
 * @description: 集群接口
 * @author: zhuzhigang
 * @create: 2018/3/24 10:40
 */

public interface ClusterServiceIfc {
    /**
     * 获取所有集群信息
     *
     * @return
     */
    List<Cluster> selectList();

    /**
     * 获取单个集群信息
     *
     * @param id 集群id
     * @return 集群对象
     */
    Cluster selectOne(Long id);

    /**
     * 添加一个集群数据
     *
     * @param cluser 集群对象
     */
    void insertSelective(Cluster cluser);

    /**
     * 编辑一个集群数据
     *
     * @param cluser 集群对象
     */
    void updateSelective(Cluster cluser) throws UnsupportedEncodingException;

    /**
     * 删除一个集群数据
     *
     * @param ids 集群id集合
     */
    void deleteClusterById(Set<Long> ids);

    /**
     * 根据clusterID获取computer信息
     *
     * @param clusterID
     * @return
     */
    List<Computer> getComputersByClusterID(Long clusterID);

    /**
     * 根据clusterID获取computer信息
     *
     * @param clusterID
     * @return
     */
    void addComputer2Cluster(Long clusterID, String computers);


    List<ClusterInfo> getClusters(Long userId);
}
