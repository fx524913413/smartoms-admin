package com.zorkdata.center.admin.biz;

import com.zorkdata.center.admin.entity.Cluster;
import com.zorkdata.center.admin.entity.Computer;
import com.zorkdata.center.admin.mapper.ClusterMapper;
import com.zorkdata.center.admin.vo.ClusterInfo;
import com.zorkdata.center.common.biz.BaseBiz;
import com.zorkdata.center.common.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/24 11:25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ClusterBiz extends BaseBiz<ClusterMapper, Cluster> {
    @Override
    public void insertSelective(Cluster entity) {
        EntityUtils.setCreatAndUpdatInfo(entity);
        mapper.insertSelectiveGetId(entity);
    }

    public void deleteByIds(Set<Long> ids) {
        mapper.deleteByIds(ids);
    }

    public List<Computer> getComputersByClusterID(Long clusterID) {
        if (clusterID == 0) {
            return mapper.getAllNoClusterComputers();
        } else {
            return mapper.getComputersByClusterID(clusterID);
        }
    }

    public Cluster getClusterByClusterID(Long clusterID) {
        Cluster cluster = new Cluster();
        return mapper.selectByPrimaryKey(clusterID);
    }

    public List<Cluster> getClusterList() {
        return mapper.selectAll();
    }

    public List<ClusterInfo> getMasterAndAgentInfoInCluster(Set<Long> comuputerIds) {
        if (comuputerIds != null && comuputerIds.size() != 0) {
            return mapper.getMasterAndAgentInfoInCluster(comuputerIds);
        } else {
            return null;
        }

    }

    public List<ClusterInfo> getNoMasterInfoInCluster() {
        return mapper.getNoMasterInfoInCluster();
    }


}
