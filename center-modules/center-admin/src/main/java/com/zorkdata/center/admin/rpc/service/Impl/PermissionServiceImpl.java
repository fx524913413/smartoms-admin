package com.zorkdata.center.admin.rpc.service.Impl;

import com.zorkdata.center.admin.biz.*;
import com.zorkdata.center.admin.entity.*;
import com.zorkdata.center.admin.rpc.service.Ifc.PermissionServiceIfc;
import com.zorkdata.center.admin.vo.ComputerVo;
import com.zorkdata.center.admin.util.PermissionCode;
import com.zorkdata.center.admin.util.PermissionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author: zhuzhigang
 * @create: 2018/3/27 10:56
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionServiceImpl implements PermissionServiceIfc {
    public static final List<String> RESOURCETYPE = new ArrayList<String>() {{
        add("action");
        add("tag");
        add("computer");
        add("menu");
    }};

    @Autowired
    private PermissionBiz permissionBiz;

    @Autowired
    private UserRoleBiz userRoleBiz;

    @Autowired
    private UserGroupBiz userGroupBiz;

    @Autowired
    private ActionBiz actionBiz;

    @Autowired
    private TagBiz tagBiz;

    @Autowired
    private ProductBiz productBiz;

    @Autowired
    private RoleBiz roleBiz;

    @Autowired
    private MenuBiz menuBiz;

    @Autowired
    private ComputerBiz computerBiz;

    @Autowired
    private PermissionUtil permissionUtil;

    @Override
    public SuperEntity getResourceByUserID(Long userID, String productCode) {
        SuperEntity permission = new SuperEntity();
        List<Product> products = productBiz.getProductByOwnerID(userID);
        if (products != null&&products.size()>0) {
            //如果存在产品说明是负责人 则根据负责人id获取权限
            for (Product product:products) {
                if (product.getCode().equals(productCode)) {
                    permission.setProperty(PermissionCode.COMPUTER, computerBiz.selectListAll());
                    permission.setProperty(PermissionCode.MENU, menuBiz.getMenuByProductID(product.getProductID()));
                    permission.setProperty(PermissionCode.ACTION, actionBiz.getActionByProductID(product.getProductID()));
                    permission.setProperty(PermissionCode.TAG, tagBiz.getTagByProductID(product.getProductID()));
                }
            }
        } else {
            //说明不是产品负责人
            Set<Long> userIds = new HashSet<>();
            userIds.add(userID);
            List<List<Permission>> permissions = new ArrayList<>();
            Set<Long> groupIds = userGroupBiz.getGroupIDByUserID(userID);
            List<Long> roleIDByProductID = roleBiz.getRoleIDByProductID(productBiz.getProductIDByProductCode(productCode));
            Set<Long> roleIDByUserID = userRoleBiz.getRoleIDByUserID(userID);
            Set<Long> roleIds = PermissionUtil.getRoleIDForProduct(roleIDByUserID, roleIDByProductID);
            if (groupIds != null && groupIds.size() != 0) {
                permissions.add(permissionBiz.getPermissionBySource(PermissionCode.GROUP, groupIds));
            }
            if (roleIds != null && roleIds.size() != 0) {
                permissions.add(permissionBiz.getPermissionBySource(PermissionCode.ROLE, roleIds));
            }
            permissions.add(permissionBiz.getPermissionBySource(PermissionCode.USER, userIds));
            Map<String, Set<Long>> permissionMap = getAllNewMapByOldList(permissions);
            if (permissionMap != null && permissionMap.size() != 0) {
                if (permissionMap.get(PermissionCode.TAG) != null && permissionMap.get(PermissionCode.TAG).size() != 0) {
                    permissionMap.get(PermissionCode.COMPUTER).addAll(tagBiz.getResourceIDByTagIds(PermissionCode.COMPUTER, permissionMap.get(PermissionCode.TAG)));
                }
                for (String resourceName : permissionMap.keySet()) {
                    if (RESOURCETYPE.contains(resourceName)) {
                        List<SuperEntity> resource = permissionBiz.getResourceByResourceID(resourceName, permissionMap.get(resourceName));
                        permission.setProperty(resourceName, resource);
                    } else {
                        permission.setProperty(resourceName, permissionMap.get(resourceName));
                    }
                }
            }
        }
        return permission;
    }

    @Override
    public SuperEntity getPermissionByUserID(String productCode, Long userID) {
        SuperEntity superEntity = new SuperEntity();
        List<Product> products = productBiz.getProductByOwnerID(userID);
        if (products != null && products.size() > 0) {
            //如果存在产品说明是负责人 则根据负责人id获取权限
            for (Product product : products) {
                if (product.getCode().equals(productCode)) {
                    superEntity.setProperty(PermissionCode.COMPUTER, computerBiz.selectListAll());
                    superEntity.setProperty(PermissionCode.MENU, menuBiz.getMenuByProductID(product.getProductID()));
                    superEntity.setProperty(PermissionCode.ACTION, actionBiz.getActionByProductID(product.getProductID()));
                    superEntity.setProperty(PermissionCode.TAG, tagBiz.getTagByProductID(product.getProductID()));
                }
            }
            return superEntity;
        } else {
            //判断用户是否具有可操作其他用户权限的功能
            Set<Long> userIds = new HashSet<>();
            userIds.add(userID);
            List<List<Permission>> permissions = new ArrayList<>();
            //获取该产品下所有角色id
            List<Long> roleIDByProductID = roleBiz.getRoleIDByProductID(productBiz.getProductIDByProductCode(productCode));
            //获取该用户所有角色id
            Set<Long> roleIDByUserID = userRoleBiz.getRoleIDByUserID(userID);
            //获取是否为他人赋权的功能id
            Long actionID = actionBiz.getActionIDByActionCode(PermissionCode.PERMISSION_CONTROL);
            //获取具有该功能的所有角色
            List<Long> roleIDByResourceID = permissionBiz.getSourceIDByResourceID(PermissionCode.ACTION, actionID, PermissionCode.ROLE);
            //获取用户具有该功能的角色id
            Set<Long> roleIds = new HashSet<>();
            for (Long roleID : roleIDByUserID) {
                if (roleIDByProductID.contains(roleID)) {
                    if (roleIDByResourceID.contains(roleID)) {
                        roleIds.add(roleID);
                    }
                }
            }
            //获取所有拥有该权限的用户
            List<Long> sourceIDByResourceID = permissionBiz.getSourceIDByResourceID(PermissionCode.ACTION, actionID, PermissionCode.USER);
            if (sourceIDByResourceID.contains(userID)) {
                permissions.add(permissionBiz.getPermissionBySource(PermissionCode.USER, userIds));
            }
            if (roleIds != null && roleIds.size() > 0) {
                permissions.add(permissionBiz.getPermissionBySource(PermissionCode.ROLE, roleIds));
                permissions.add(permissionBiz.getPermissionBySource(PermissionCode.USER, userIds));
            }
            Map<String, Set<Long>> permissionMap = getAllNewMapByOldList(permissions);
            if (permissionMap != null && permissionMap.size() != 0) {
                for (String resourceName : permissionMap.keySet()) {
                    if (RESOURCETYPE.contains(resourceName)) {
                        if (resourceName.equals(PermissionCode.COMPUTER)) {
                            continue;
                        } else {
                            List<SuperEntity> resource = permissionBiz.getResourceByResourceID(resourceName, permissionMap.get(resourceName));
                            superEntity.setProperty(resourceName, resource);
                        }
                    } else {
                        superEntity.setProperty(resourceName, permissionMap.get(resourceName));
                    }
                }
                return superEntity;
            }
            return null;
        }
    }


    @Override
    public SuperEntity getAllPermission(Integer productID) {
        SuperEntity superEntity = new SuperEntity();
        superEntity.setProperty(PermissionCode.TAG, tagBiz.getTagByProductID(productID));
        superEntity.setProperty(PermissionCode.ACTION, actionBiz.getActionByProductID(productID));
        superEntity.setProperty(PermissionCode.MENU, menuBiz.getMenuByProductID(productID));
        return superEntity;
    }

    @Override
    public List<Computer> getComputerByUserId(Long userID) {
        List<Product> products = productBiz.getProductByOwnerID(userID);
        if (products != null&&products.size()>0) {
            //如果存在产品说明是负责人 则直接获取所有主机
            return computerBiz.selectAllComputer();
        } else {
            //说明不是产品负责人
            Set<Long> userIds = new HashSet<>();
            userIds.add(userID);
            Set<Long> computerIds = new HashSet<>();
            Set<Long> tagIds = new HashSet<>();
            Set<Long> groupIds = userGroupBiz.getGroupIDByUserID(userID);
            Set<Long> roleIds = userRoleBiz.getRoleIDByUserID(userID);
            if (groupIds != null && groupIds.size() != 0) {
                tagIds.addAll(permissionBiz.getResourceID(PermissionCode.GROUP, groupIds, PermissionCode.TAG));
            }
            if (roleIds != null && roleIds.size() != 0) {
                tagIds.addAll(permissionBiz.getResourceID(PermissionCode.ROLE, roleIds, PermissionCode.TAG));
            }
            tagIds.addAll(permissionBiz.getResourceID(PermissionCode.USER, userIds, PermissionCode.TAG));
            if (tagIds != null && tagIds.size() != 0) {
                computerIds.addAll(tagBiz.getResourceIDByTagIds(PermissionCode.COMPUTER, tagIds));
            }
            computerIds.addAll(permissionBiz.getResourceID(PermissionCode.USER, userIds, PermissionCode.COMPUTER));
            if (computerIds != null && computerIds.size() != 0) {
                return computerBiz.getComputerByResourceID(computerIds);
            }
            return null;
        }
    }

    @Override
    public Boolean givePermission(Map<String, Map<String, List<Long>>> permissionMap, String sourceType, Long sourceID, Long userID, String productCode) {
        Boolean flag = permissionUtil.haveActionRole(PermissionCode.PERMISSION_CONTROL, userID, productCode);
        if (flag) {
            Set<Long> sourceIds = new HashSet<>();
            sourceIds.add(sourceID);
            for (String resource : permissionMap.keySet()) {
                if (permissionMap.get(resource).get("insert") != null && permissionMap.get(resource).get("insert").size() != 0) {
                    List<Long> resourceIds = permissionBiz.getResourceID(sourceType, sourceIds, resource);
                    for (Long resourceID : permissionMap.get(resource).get("insert")) {
                        if (resourceIds.contains(resourceID)) {
                            continue;
                        } else {
                            Permission permission = new Permission();
                            permission.setResource(resource);
                            permission.setSourceType(sourceType);
                            permission.setSourceID(sourceID);
                            permission.setResourceID(resourceID);
                            permission.setChmod(1);
                            permissionBiz.insertSelective(permission);
                        }
                    }
                }
                if (permissionMap.get(resource).get("delete") != null && permissionMap.get(resource).get("delete").size() != 0) {
                    permissionBiz.deletePermissionByIds(resource, permissionMap.get(resource).get("delete"), sourceID, sourceType);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Set<Long>> getPermissionByRoleID(Long roleID) {
        Set<Long> roleIds = new HashSet<>();
        List<List<Permission>> permissionList = new ArrayList<>();
        roleIds.add(roleID);
        permissionList.add(permissionBiz.getPermissionBySource(PermissionCode.ROLE, roleIds));
        if (permissionList != null && permissionList.size() != 0) {
            return getAllNewMapByOldList(permissionList);
        }
        return null;
    }

    @Override
    public List<ComputerVo> getComputerVoByUserId(Long userID) {
        List<Product> products = productBiz.getProductByOwnerID(userID);
        if (products != null&&products.size()>0) {
            //如果存在产品说明是负责人 则直接获取所有主机
            return computerBiz.selectAllComputerAndAgent();
        } else {
            //说明不是产品负责人
            Set<Long> userIds = new HashSet<>();
            userIds.add(userID);
            Set<Long> computerIds = new HashSet<>();
            Set<Long> tagIds = new HashSet<>();
            Set<Long> groupIds = userGroupBiz.getGroupIDByUserID(userID);
            Set<Long> roleIds = userRoleBiz.getRoleIDByUserID(userID);
            if (groupIds != null && groupIds.size() != 0) {
                tagIds.addAll(permissionBiz.getResourceID(PermissionCode.GROUP, groupIds, PermissionCode.TAG));
            }
            if (roleIds != null && roleIds.size() != 0) {
                tagIds.addAll(permissionBiz.getResourceID(PermissionCode.ROLE, roleIds, PermissionCode.TAG));
            }
            tagIds.addAll(permissionBiz.getResourceID(PermissionCode.USER, userIds, PermissionCode.TAG));
            if (tagIds != null && tagIds.size() != 0) {
                computerIds.addAll(tagBiz.getResourceIDByTagIds(PermissionCode.COMPUTER, tagIds));
            }
            computerIds.addAll(permissionBiz.getResourceID(PermissionCode.USER, userIds, PermissionCode.COMPUTER));
            if (computerIds != null && computerIds.size() != 0) {
                return computerBiz.getComputerAndAgentByResourceID(computerIds);
            }
            return null;
        }
    }

    //将多个list的数据进行去重(包含中台数据)
    private Map<String, Set<Long>> getAllNewMapByOldList(List<List<Permission>> permissionList) {
        Map<String, Set<Long>> newMap = new HashMap<>();
        for (List<Permission> permissions : permissionList) {
            if (permissions != null) {
                for (Permission permission : permissions) {
                    if (newMap.containsKey(permission.getResource())) {
                        newMap.get(permission.getResource()).add(permission.getResourceID());
                    } else {
                        HashSet<Long> set = new HashSet<>();
                        set.add(permission.getResourceID());
                        newMap.put(permission.getResource(), set);
                    }
                }
            }
        }
        return newMap;
    }
}
