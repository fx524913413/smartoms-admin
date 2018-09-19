package com.zorkdata.center.admin.biz;

import com.zorkdata.center.admin.entity.ClusterComputerRelation;
import com.zorkdata.center.admin.entity.Tag;
import com.zorkdata.center.admin.entity.TagResourceRelation;
import com.zorkdata.center.admin.mapper.TagMapper;
import com.zorkdata.center.admin.mapper.TagResourceRelationMapper;
import com.zorkdata.center.common.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/30 13:37
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TagResourceRelationBiz extends BaseBiz<TagResourceRelationMapper, TagResourceRelation> {
//    public TagResourceRelation getResourceByTagId(Long tagID){
//        TagResourceRelation tagResourceRelation = new TagResourceRelation();
//        tagResourceRelation.setId(tagID);
//        return mapper.selectOne(tagResourceRelation);
//    }

    public void insertTagResourceRelation(TagResourceRelation tagResourceRelation) {
        super.insertSelective(tagResourceRelation);
    }

    public void updateTagResourceRelation(TagResourceRelation tagResourceRelation) {
        super.updateSelectiveById(tagResourceRelation);
    }

    public void deleteTagResourceRelation(Long id) {
        super.deleteById(id);
    }

    public void deleteBySelective(Long resourceID, String resourceType, List<Long> deleteIds) {
        mapper.deleteBySelective(resourceID, resourceType, deleteIds);
    }

    public List<Long> getResourceByTagID(String resourceType, List<Long> tagIds) {
        return mapper.getResourceByTagID(resourceType, tagIds);
    }

    public List<Long> getTagByResourceID(Long resourceID, String resourceType) {
        return mapper.getTagByResourceID(resourceID, resourceType);
    }
}
