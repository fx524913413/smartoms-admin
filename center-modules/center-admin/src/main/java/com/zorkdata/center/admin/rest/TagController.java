package com.zorkdata.center.admin.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zorkdata.center.admin.entity.SuperEntity;
import com.zorkdata.center.admin.entity.Tag;
import com.zorkdata.center.admin.entity.Tag;
import com.zorkdata.center.admin.rpc.service.Ifc.TagServiceIfc;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/30 20:24
 */
@RestController
@RequestMapping("tag")
public class TagController {
    @Autowired
    private TagServiceIfc tagServiceIfc;

    /**
     * 添加一个标签
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public RespModel addTag(@RequestBody Map request) {
        Long userID = Long.parseLong(String.valueOf(request.get("userID")));
        String productCode = String.valueOf(request.get("productCode"));
        Tag tag = JSON.parseObject(String.valueOf(request.get("tag")), Tag.class);
        Tag tagByTagName = tagServiceIfc.selectTagByTagName(tag.getTagName(), productCode,null);
        if (tagByTagName != null) {
            return RespTools.getRespMsgModel(CodeTable.ALREADY_EXIST, null);
        } else {
            tag.setCreateTime(new Date());
            Boolean flag = tagServiceIfc.insertSelective(userID, tag, productCode);
            if (flag) {
                return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
            }
            return RespTools.getRespMsgModel(CodeTable.NOT_ALLOW, null);
        }
    }

    /**
     * 编辑一个标签
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public RespModel updateTag(@RequestBody Map request) {
        Long userID = Long.parseLong(String.valueOf(request.get("userID")));
        String productCode = String.valueOf(request.get("productCode"));
        Tag tag = JSON.parseObject(String.valueOf(request.get("tag")), Tag.class);
        Tag tagByTagName = tagServiceIfc.selectTagByTagName(tag.getTagName(), productCode,tag.getId());
        if (tagByTagName != null) {
            return RespTools.getRespMsgModel(CodeTable.ALREADY_EXIST, null);
        }
        Boolean flag = tagServiceIfc.updateSelective(userID, productCode, tag);
        if (flag) {
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
        }
        return RespTools.getRespMsgModel(CodeTable.NOT_ALLOW, null);
    }

    /**
     * 删除单个或一批标签
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public RespModel deleteTag(@RequestBody Map request) {
        Long userID = Long.parseLong(String.valueOf(request.get("userID")));
        String productCode = String.valueOf(request.get("productCode"));
        List<Long> ids = JSON.parseArray(String.valueOf(request.get("ids")), Long.TYPE);
        Boolean flag = tagServiceIfc.deleteTagById(userID, productCode, ids);
        if (flag) {
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
        }
        return RespTools.getRespMsgModel(CodeTable.NOT_ALLOW, null);
    }

    /**
     * 根据标签获取标签下的资源
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/getResourceByTagID", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getResourceByTagID(@RequestParam List<Long> tagIds) {
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, tagServiceIfc.getResourceByTagID(tagIds));
    }

    /**
     * 修改当前标签的范围
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/giveResourceToTag", method = RequestMethod.POST)
    @ResponseBody
    public RespModel giveResourceToTag(@RequestBody Map request) {
        Map<String, Map<String, List<Long>>> resourceMap =
                JSON.parseObject(String.valueOf(request.get("resource")), new TypeReference<Map<String, Map<String, List<Long>>>>() {
                });
        Long resourceID = Long.parseLong(String.valueOf(request.get("resourceID")));
        Long userID = Long.parseLong(String.valueOf(request.get("userID")));
        String productCode = String.valueOf(request.get("productCode"));
        Boolean flag = tagServiceIfc.giveResourceToTag(userID, productCode, resourceID, resourceMap);
        if (flag) {
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
        }
        return RespTools.getRespMsgModel(CodeTable.NOT_ALLOW, null);
    }

    /**
     * 获取所有的系统标签
     */
    @RequestMapping(value = "/getSystemTag", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getSystemTag() {
        List<Tag> tags = tagServiceIfc.getSystemTag();
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, tags);
    }

    /**
     * 获取产品标签
     */
    @RequestMapping(value = "/getProductTag", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getProductTag(@RequestParam String productCode) {
        List<Tag> tags = tagServiceIfc.getProductTag(productCode);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, tags);
    }

    /**
     * 获取用户所有的标签（中台）
     */
    @RequestMapping(value = "/getAllTag", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getAllTag(@RequestParam Long userID, @RequestParam String productCode) {
        List<Tag> tags = tagServiceIfc.getAllTag(userID, productCode);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, tags);
    }

    /**
     * 获取用户所有的标签（其他产品）
     */
    @RequestMapping(value = "/getTagByUser", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getTagByUser(@RequestParam Long userID, @RequestParam String productCode) {
        List<Tag> tags = tagServiceIfc.getTagByUser(userID, productCode);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, tags);
    }

    /**
     * 获取资源下的所有标签
     */
    @RequestMapping(value = "/getTagByResourceID", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getTagByResourceID(@RequestParam Long resourceID, @RequestParam String resourceType) {
        List<Tag> tags = tagServiceIfc.getTagByResourceID(resourceID, resourceType);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, tags);
    }
}
