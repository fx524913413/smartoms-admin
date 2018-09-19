package com.zorkdata.center.admin.rpc.service.Ifc;

import com.zorkdata.center.admin.entity.ClusterComputerRelation;
import com.zorkdata.center.admin.entity.TagResourceRelation;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/30 13:28
 */
public interface TagResourceRelationServiceIfc {
    void insertTagResourceRelationEntity(TagResourceRelation tagResourceRelation);

    void updateTagResourceRelationEntity(TagResourceRelation tagResourceRelation);

    void deleteTagResourceRelationEntity(Long tagResourceRelationID);
}
