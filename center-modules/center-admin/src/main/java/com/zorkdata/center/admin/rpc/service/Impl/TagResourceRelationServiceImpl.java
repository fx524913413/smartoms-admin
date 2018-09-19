package com.zorkdata.center.admin.rpc.service.Impl;

import com.zorkdata.center.admin.biz.TagResourceRelationBiz;
import com.zorkdata.center.admin.entity.TagResourceRelation;
import com.zorkdata.center.admin.rpc.service.Ifc.TagResourceRelationServiceIfc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/30 13:34
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TagResourceRelationServiceImpl implements TagResourceRelationServiceIfc {
    @Autowired
    TagResourceRelationBiz tagResourceRelationBiz;

    @Override
    public void insertTagResourceRelationEntity(TagResourceRelation tagResourceRelation) {
        tagResourceRelationBiz.insertTagResourceRelation(tagResourceRelation);
    }

    @Override
    public void updateTagResourceRelationEntity(TagResourceRelation tagResourceRelation) {
        tagResourceRelationBiz.updateTagResourceRelation(tagResourceRelation);
    }

    @Override
    public void deleteTagResourceRelationEntity(Long tagResourceRelationID) {
        tagResourceRelationBiz.deleteTagResourceRelation(tagResourceRelationID);
    }
}
