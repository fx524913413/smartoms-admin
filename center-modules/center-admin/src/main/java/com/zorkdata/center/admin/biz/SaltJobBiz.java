package com.zorkdata.center.admin.biz;

import com.zorkdata.center.admin.entity.SaltJob;
import com.zorkdata.center.admin.mapper.SaltJobMapper;
import com.zorkdata.center.common.biz.BaseBiz;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/5/24 22:07
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SaltJobBiz extends BaseBiz<SaltJobMapper, SaltJob> {
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public void insertBatch(List<SaltJob> saltJobs) {
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        SaltJobMapper mapper = sqlSession.getMapper(SaltJobMapper.class);
        for (SaltJob saltJob : saltJobs) {
            mapper.insertBatch(saltJob);
        }
        sqlSession.commit();
    }
}
