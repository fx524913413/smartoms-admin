package com.zorkdata.center.admin.util;

import com.zorkdata.center.admin.biz.*;
import com.zorkdata.center.admin.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 权限管理工具类
 */
@Component
public class PermissionUtil {
    @Autowired
    PermissionBiz permissionBiz;
    @Autowired
    ProductBiz productBiz;
    @Autowired
    UserRoleBiz userRoleBiz;
    @Autowired
    ActionBiz actionBiz;
    @Autowired
    RoleBiz roleBiz;

    /**
     * 获取该用户属于该系统下的角色id
     */
    public static Set<Long> getRoleIDForProduct(Set<Long> roleIds, List<Long> roleList) {
        HashSet<Long> set = new HashSet<>();
        for (Long roleID : roleIds) {
            if ((roleList.contains(roleID))) {
                set.add(roleID);
            }
        }
        return set;
    }

    /**
     * 判断用户是否具有可该功能
     *
     * @param actionCode
     * @param userID
     * @param productCode
     * @return
     */
    public Boolean haveActionRole(String actionCode, Long userID, String productCode) {
        List<Product> products = productBiz.getProductByOwnerID(userID);
        if (products != null&&products.size()>0) {
            for (Product product:products) {
                if(product.getCode().equals(productCode)){
                    return true;
                }
            }
        }
        Long actionID = actionBiz.getActionIDByActionCode(actionCode);
        //获取所有拥有该权限的用户
        List<Long> sourceIDByResourceID = permissionBiz.getSourceIDByResourceID(PermissionCode.ACTION, actionID, PermissionCode.USER);
        //获取该产品下所有角色id
        Integer productID = productBiz.getProductIDByProductCode(productCode);
        List<Long> roleIDByProductID = roleBiz.getRoleIDByProductID(productID);
        //获取该用户所有角色id
        Set<Long> roleIDByUserID = userRoleBiz.getRoleIDByUserID(userID);
        List<Long> roleIDByResourceID = permissionBiz.getSourceIDByResourceID(PermissionCode.ACTION, actionID, PermissionCode.ROLE);
        Set<Long> roleIDs = new HashSet<>();
        for (Long roleID : roleIDByUserID) {
            if (roleIDByProductID.contains(roleID)) {
                if (roleIDByResourceID.contains(roleID)) {
                    roleIDs.add(roleID);
                }
            }
        }
        if (sourceIDByResourceID.contains(userID) || (roleIDs != null && roleIDs.size() != 0)) {
            return true;
        }
        return false;
    }
}
