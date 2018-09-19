package com.zorkdata.center.admin.biz;

import com.zorkdata.center.admin.entity.SaltJob;
import com.zorkdata.center.admin.entity.SaltJobRet;
import com.zorkdata.center.admin.mapper.SaltJobMapper;
import com.zorkdata.center.admin.mapper.SaltJobRetMapper;
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
 * @create: 2018/5/24 22:08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SaltJobRetBiz extends BaseBiz<SaltJobRetMapper, SaltJobRet> {
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public void insertBatch(List<SaltJobRet> saltJobRets) {
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        SaltJobRetMapper mapper = sqlSession.getMapper(SaltJobRetMapper.class);
        for (SaltJobRet saltJobRet : saltJobRets) {
            mapper.insertBatch(saltJobRet);
        }
        sqlSession.commit();
    }
}
