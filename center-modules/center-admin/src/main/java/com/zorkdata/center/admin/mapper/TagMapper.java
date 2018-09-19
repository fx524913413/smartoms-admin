package com.zorkdata.center.admin.mapper;

import com.zorkdata.center.admin.entity.Tag;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description: ${todo}
 * @author: huziyue
 * @create: 2018/3/20 18:03
 */
@Repository
public interface TagMapper extends Mapper<Tag> {
    /**
     * 删除一个Tags数据
     *
     * @param ids 集群id集合
     */
    void deleteByIds(@Param("ids") List<Long> ids);

    /**
     * 通过tagid查询所有tag
     *
     * @param tagIds
     * @return
     */
    List<Tag> getTagByTagIds(@Param("tagIds") Set<Long> tagIds);

    /**
     * 通过tagid查询所有的
     *
     * @param tagIds
     * @return
     */
    List<Long> getResourceByTagIds(@Param("resourceType") String resourceType, @Param("tagIds") Set<Long> tagIds);

    /**
     * 通过tagname查询tag
     *
     * @param tagName
     * @return
     */
    Tag selectTagByTagName(@Param("tagName") String tagName, @Param("productID") Integer productID,@Param("tagID")Long tagID);

    /**
     * 编辑tag
     *
     * @param tag
     */
    void updateSelectiveByTagID(Tag tag);

    /**
     * 根据产品id获取所有的标签
     *
     * @param productID
     * @return
     */
    List<Tag> getTagByProductID(Integer productID);

    /**
     * 获取所有的系统标签
     *
     * @return
     */
    List<Tag> getSystemTag();

    /**
     * 获取产品标签
     *
     * @return
     */
    List<Tag> getProductTag(Integer productID);

    /**
     * 获取一系列tag中的公共tag
     *
     * @param tagIds
     * @return
     */
    List<Tag> getSystemTagByTagIds(@Param("tagIds") Set<Long> tagIds, @Param("productID") Integer productID);

    /**
     * 插入一条tag
     *
     * @param tag
     */
    void insertByTag(Tag tag);

    /**
     * 获取产品的内部标签
     *
     * @param tagIds
     * @param productID
     * @return
     */
    List<Tag> getProductTagByResourceID(@Param("tagIds") Set<Long> tagIds, @Param("productID") Integer productID);
}
