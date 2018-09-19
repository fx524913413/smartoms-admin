package com.zorkdata.center.admin.biz;

import com.zorkdata.center.admin.entity.Tag;
import com.zorkdata.center.admin.mapper.TagMapper;
import com.zorkdata.center.common.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/30 13:13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TagBiz extends BaseBiz<TagMapper, Tag> {

    public static final String COMPUTER = "computer";
    public static final String MENU = "menu";

    public void deleteByIds(List<Long> ids) {
        mapper.deleteByIds(ids);
    }

    public List<Tag> getTagByTagIds(Set<Long> tagIds) {
        return mapper.getTagByTagIds(tagIds);
    }

    public Map<String, Set<Long>> getAllResourceByTagIds(Set<Long> tagIds) {
        Map<String, Set<Long>> stringSetMap = new HashMap<>();
        HashSet<Long> set1 = new HashSet<>();
        HashSet<Long> set2 = new HashSet<>();
        List<Long> computerIds = mapper.getResourceByTagIds(COMPUTER, tagIds);
        List<Long> menuIds = mapper.getResourceByTagIds(MENU, tagIds);
        set1.addAll(computerIds);
        set2.addAll(menuIds);
        stringSetMap.put(COMPUTER, set1);
        stringSetMap.put(MENU, set2);
        return stringSetMap;
    }

    public List<Long> getResourceIDByTagIds(String resourceName, Set<Long> tagIds) {
        return mapper.getResourceByTagIds(resourceName, tagIds);
    }

    public Tag selectTagByTagName(String tagName, Integer productID,Long tagID) {
        return mapper.selectTagByTagName(tagName, productID,tagID);
    }

    public void updateSelectiveByTagID(Tag tag) {
        mapper.updateSelectiveByTagID(tag);
    }

    public List<Tag> getTagByProductID(Integer productID) {
        return mapper.getTagByProductID(productID);
    }

    public List<Tag> getSystemTag() {
        return mapper.getSystemTag();
    }

    public List<Tag> getProductTag(Integer productID) {
        return mapper.getProductTag(productID);
    }

    public List<Tag> getSystemTagByTagIds(Set<Long> tagIds, Integer productID) {
        return mapper.getSystemTagByTagIds(tagIds, productID);
    }

    public void insertByTag(Tag tag) {
        mapper.insertByTag(tag);
    }

    public List<Tag> getProductTagByResourceID(Set<Long> tagIds, Integer productID) {
        return mapper.getProductTagByResourceID(tagIds, productID);
    }
}
