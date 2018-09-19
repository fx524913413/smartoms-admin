package com.zorkdata.center.admin.biz;

import com.zorkdata.center.admin.entity.ClusterComputerRelation;
import com.zorkdata.center.admin.mapper.ClusterComputerRelationMapper;
import com.zorkdata.center.common.biz.BaseBiz;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ClusterComputerRelationBiz extends BaseBiz<ClusterComputerRelationMapper, ClusterComputerRelation> {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public ClusterComputerRelation getClusterByClusterId(Long clusterID) {
        ClusterComputerRelation clusterComputerRelation = new ClusterComputerRelation();
        clusterComputerRelation.setClusterID(clusterID);
        return mapper.selectOne(clusterComputerRelation);
    }

    public void insertClusterComputerRelation(ClusterComputerRelation clusterComputerRelation) {
        super.insertSelective(clusterComputerRelation);
    }

    public void updateClusterComputerRelation(ClusterComputerRelation clusterComputerRelation) {
        super.updateSelectiveById(clusterComputerRelation);
    }

    public void deleteClusterComputerRelation(Long clusterID) {
        mapper.deleteByclusterID(clusterID);
    }

    public void insertBatch(List<ClusterComputerRelation> clusterComputerRelationList) {
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        ClusterComputerRelationMapper clusterComputerRelationMapper = sqlSession.getMapper(ClusterComputerRelationMapper.class);
        for (ClusterComputerRelation clusterComputerRelation : clusterComputerRelationList) {
            clusterComputerRelationMapper.insertBatch(clusterComputerRelation);
        }
        sqlSession.commit();
    }
}
