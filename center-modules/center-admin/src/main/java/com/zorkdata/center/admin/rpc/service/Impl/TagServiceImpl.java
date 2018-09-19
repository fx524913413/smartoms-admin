package com.zorkdata.center.admin.rpc.service.Impl;

import com.zorkdata.center.admin.biz.*;
import com.zorkdata.center.admin.entity.*;
import com.zorkdata.center.admin.rpc.service.Ifc.TagServiceIfc;
import com.zorkdata.center.admin.util.PermissionCode;
import com.zorkdata.center.admin.util.PermissionUtil;
import com.zorkdata.center.admin.vo.ComputerTagVo;
import com.zorkdata.center.admin.util.PermissionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/30 13:17
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TagServiceImpl implements TagServiceIfc {
    @Autowired
    TagBiz tagBiz;
    @Autowired
    TagResourceRelationBiz tagResourceRelationBiz;
    @Autowired
    PermissionBiz permissionBiz;
    @Autowired
    private ProductBiz productBiz;
    @Autowired
    private ComputerBiz computerBiz;
    @Autowired
    private UserRoleBiz userRoleBiz;
    @Autowired
    private ActionBiz actionBiz;
    @Autowired
    private RoleBiz roleBiz;
    @Autowired
    PermissionUtil permissionUtil;

    @Override
    public List<Tag> selectList() {
        return tagBiz.selectListAll();
    }

    @Override
    public Tag selectOne(Long id) {
        return tagBiz.selectById(id);
    }

    @Override
    public Boolean insertSelective(Long userID, Tag tag, String productCode) {
        Boolean flag = permissionUtil.haveActionRole(PermissionCode.TAG_CONTROL, userID, productCode);
        if (flag) {
            tag.setProductID(productBiz.getProductIDByProductCode(productCode));
            tagBiz.insertByTag(tag);
            List<Product> products = productBiz.getProductByOwnerID(userID);
            if (products == null||products.size()==0) {
                Permission permission = new Permission();
                permission.setSourceID(userID);
                permission.setSourceType(PermissionCode.USER);
                permission.setResource(PermissionCode.TAG);
                permission.setResourceID(tag.getId());
                permission.setChmod(1);
                permissionBiz.insertSelective(permission);
            }
            return true;
        }
        return false;
    }

    @Override
    public Boolean updateSelective(Long userID, String productCode, Tag tag) {
        Boolean flag = permissionUtil.haveActionRole(PermissionCode.TAG_CONTROL, userID, productCode);
        if (flag) {
            tagBiz.updateSelectiveByTagID(tag);
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteTagById(Long userID, String productCode, List<Long> ids) {
        Boolean flag = permissionUtil.haveActionRole(PermissionCode.TAG_CONTROL, userID, productCode);
        if (flag) {
            tagBiz.deleteByIds(ids);
            Set<Long> tagIds = new HashSet<>();
            tagIds.addAll(ids);
            permissionBiz.deleteByResourceIds(PermissionCode.TAG, tagIds);
            return true;
        }
        return false;
    }

    @Override
    public List<ComputerTagVo> getResourceByTagID(List<Long> tagIds) {
        List<Long> computerIds = tagResourceRelationBiz.getResourceByTagID(PermissionCode.COMPUTER, tagIds);
        List<ComputerTagVo> computerByResourceID = null;
        Set<Long> setComputerID = new HashSet<>();
        setComputerID.addAll(computerIds);
        if (setComputerID != null && setComputerID.size() != 0) {
            computerByResourceID = computerBiz.getComputerAndTag(setComputerID);
        }
        return computerByResourceID;
    }

    @Override
    public Boolean giveResourceToTag(Long userID, String productCode, Long resourceID, Map<String, Map<String, List<Long>>> resourceMap) {
        Boolean flag = permissionUtil.haveActionRole(PermissionCode.MATION_CONTROL, userID, productCode);
        if (flag) {
            for (String resourceType : resourceMap.keySet()) {
                if (resourceMap.get(resourceType).get("insert") != null && resourceMap.get(resourceType).get("insert").size() != 0) {
                    List<Long> oldTagIds = tagResourceRelationBiz.getTagByResourceID(resourceID, resourceType);
                    List<Long> insertIds = resourceMap.get(resourceType).get("insert");
                    List<Long> insertTagIds = new ArrayList<>();
                    for (Long tagID : insertIds) {
                        if (oldTagIds.contains(tagID)) {
                            continue;
                        } else {
                            insertTagIds.add(tagID);
                        }
                    }
                    for (Long tagID : insertTagIds) {
                        TagResourceRelation tagResourceRelation = new TagResourceRelation();
                        tagResourceRelation.setTagID(tagID);
                        tagResourceRelation.setResourceType(resourceType);
                        tagResourceRelation.setResourceID(resourceID);
                        tagResourceRelationBiz.insertTagResourceRelation(tagResourceRelation);
                    }
                }
                if (resourceMap.get(resourceType).get("delete") != null && resourceMap.get(resourceType).get("delete").size() != 0) {
                    tagResourceRelationBiz.deleteBySelective(resourceID, resourceType, resourceMap.get(resourceType).get("delete"));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public List<Tag> getSystemTag() {
        return tagBiz.getSystemTag();
    }

    @Override
    public Tag selectTagByTagName(String tagName, String productCode,Long tagID) {
        Integer productID = productBiz.getProductIDByProductCode(productCode);
        return tagBiz.selectTagByTagName(tagName, productID,tagID);
    }

    @Override
    public List<Tag> getProductTag(String productCode) {
        return tagBiz.getProductTag(productBiz.getProductIDByProductCode(productCode));
    }

    @Override
    public List<Tag> getAllTag(Long userID, String productCode) {
        List<Tag> tags = null;
        List<Product> products = productBiz.getProductByOwnerID(userID);
        if(products!=null&&products.size()>0){
            for (Product product:products) {
                if (product.getCode().equals(productCode)) {
                    //说明用户是中台负责人，返回公共tag和中台tag
                    tags = tagBiz.getTagByProductID(product.getProductID());
                }
            }
        } else {
            //说明是普通用户则根据角色获取标签
            Set<Long> userIds = new HashSet<>();
            Set<Long> tagIds = new HashSet<>();
            userIds.add(userID);
            Set<Long> roleIds = userRoleBiz.getRoleIDByUserID(userID);
            if (roleIds != null && roleIds.size() != 0) {
                List<Long> tagIDByRole = permissionBiz.getResourceID(PermissionCode.ROLE, roleIds, PermissionCode.TAG);
                tagIds.addAll(tagIDByRole);
            }
            List<Long> tagIDByUser = permissionBiz.getResourceID(PermissionCode.USER, userIds, PermissionCode.TAG);
            tagIds.addAll(tagIDByUser);
            if (tagIds != null && tagIds.size() != 0) {
                Integer productID = productBiz.getProductIDByProductCode(productCode);
                tags = tagBiz.getSystemTagByTagIds(tagIds, productID);
            }
        }
        return tags;
    }

    @Override
    public List<Tag> getTagByUser(Long userID, String productCode) {
        List<Tag> tags = null;
        List<Product> products = productBiz.getProductByOwnerID(userID);
        if(products!=null&&products.size()>0){
            for (Product product:products) {
                if (product.getCode().equals(productCode)) {
                    //说明用户是中台负责人，返回公共tag和中台tag
                    tags = tagBiz.getTagByProductID(product.getProductID());
                }
            }
        } else {
            //说明是普通用户则根据角色获取标签
            Set<Long> userIds = new HashSet<>();
            Set<Long> tagIds = new HashSet<>();
            userIds.add(userID);
            Set<Long> roleIDByUserID = userRoleBiz.getRoleIDByUserID(userID);
            List<Long> roleIDByProductID = roleBiz.getRoleIDByProductID(productBiz.getProductIDByProductCode(productCode));
            Set<Long> roleIds = PermissionUtil.getRoleIDForProduct(roleIDByUserID, roleIDByProductID);
            if (roleIds != null && roleIds.size() != 0) {
                List<Long> tagIDByRole = permissionBiz.getResourceID(PermissionCode.ROLE, roleIds, PermissionCode.TAG);
                tagIds.addAll(tagIDByRole);
            }
            List<Long> tagIDByUser = permissionBiz.getResourceID(PermissionCode.USER, userIds, PermissionCode.TAG);
            tagIds.addAll(tagIDByUser);
            if (tagIds != null && tagIds.size() != 0) {
                Integer productID = productBiz.getProductIDByProductCode(productCode);
                tags = tagBiz.getProductTagByResourceID(tagIds, productID);
            }
        }
        return tags;
    }

    @Override
    public List<Tag> getTagByResourceID(Long resourceID, String resourceType) {
        List<Long> tagIds = tagResourceRelationBiz.getTagByResourceID(resourceID, resourceType);
        Set<Long> set = new HashSet<>();
        set.addAll(tagIds);
        if (set != null && set.size() != 0) {
            return tagBiz.getTagByTagIds(set);
        }
        return null;
    }
}
