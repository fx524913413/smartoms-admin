package com.zorkdata.center.admin.rpc.service.Ifc;

import com.zorkdata.center.admin.entity.Cluster;
import com.zorkdata.center.admin.entity.Computer;
import com.zorkdata.center.admin.entity.SuperEntity;
import com.zorkdata.center.admin.entity.Tag;
import com.zorkdata.center.admin.vo.ComputerTagVo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/30 13:15
 */
public interface TagServiceIfc {
    /**
     * 获取所有标签信息
     *
     * @return
     */
    List<Tag> selectList();

    /**
     * 获取单个标签信息
     *
     * @param id 标签id
     * @return 标签对象
     */
    Tag selectOne(Long id);

    /**
     * 添加一个标签数据
     *@param userID 操作用户ID
     * @param tag 标签对象
     * @param productCode 操作用户code
     */
    Boolean insertSelective(Long userID, Tag tag, String productCode);

    /**
     * 编辑一个标签数据
     *@param userID 操作用户ID
     * @param tag 标签对象
     * @param productCode 操作用户code
     */
    Boolean updateSelective(Long userID, String productCode, Tag tag);

    /**
     * 删除一个标签数据
     *@param productCode 操作用户code
     * @param userID 操作用户ID
     * @param ids 标签id集合
     */
    Boolean deleteTagById(Long userID, String productCode, List<Long> ids);

    /**
     * 根据TagID查询所有资源
     *
     * @param tagIds
     * @return
     */
    List<ComputerTagVo> getResourceByTagID(List<Long> tagIds);

    /**
     * 将资源与tag相关联
     *@param productCode 操作用户code
     * @param userID 操作用户ID
     * @param userID
     * @param resourceMap
     */
    Boolean giveResourceToTag(Long userID, String productCode, Long resourceID, Map<String, Map<String, List<Long>>> resourceMap);

    /**
     * 获取所有的系统标签
     *
     * @return
     */
    List<Tag> getSystemTag();

    /**
     * 通过tagname查询tag
     *
     * @param tagName
     * @param productCode
     * @param tagID
     * @return
     */
    Tag selectTagByTagName(String tagName, String productCode,Long tagID);

    /**
     * 获取产品标签
     *@param productCode
     * @return
     */
    List<Tag> getProductTag(String productCode);

    /**
     * 获取用户的tag
     *
     * @param userID
     * @param productCode
     * @return
     */
    List<Tag> getAllTag(Long userID, String productCode);

    /**
     * 获取用户的tag（其他产品）
     *
     * @param userID
     * @param productCode
     * @return
     */
    List<Tag> getTagByUser(Long userID, String productCode);

    /**
     * 获取资源的所有标签
     *
     * @param resourceID
     * @param resourceType
     * @return
     */
    List<Tag> getTagByResourceID(Long resourceID, String resourceType);
}
