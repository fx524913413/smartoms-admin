package com.zorkdata.center.admin.mapper;

import com.zorkdata.center.admin.entity.Tag;
import com.zorkdata.center.admin.entity.TagResourceRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @description: ${todo}
 * @author: huziyue
 * @create: 2018/3/20 18:03
 */
@Repository
public interface TagResourceRelationMapper extends Mapper<TagResourceRelation> {

    /**
     * 解除resource与tag的关系
     *
     * @param tagID
     * @param resourceType
     * @param deleteIds
     */
    void deleteBySelective(@Param("resourceID") Long resourceID, @Param("resourceType") String resourceType, @Param("deleteIds") List<Long> deleteIds);

    /**
     * 通过标签id查询机器
     *
     * @param tagIds
     * @return
     */
    List<Long> getResourceByTagID(@Param("resourceType") String resourceType, @Param("tagIds") List<Long> tagIds);

    /**
     * 获取资源的所有标签
     *
     * @param resourceID
     * @param resourceType
     * @return
     */
    List<Long> getTagByResourceID(@Param("resourceID") Long resourceID, @Param("resourceType") String resourceType);
}
