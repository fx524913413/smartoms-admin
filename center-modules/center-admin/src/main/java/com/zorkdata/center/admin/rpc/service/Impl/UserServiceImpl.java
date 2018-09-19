package com.zorkdata.center.admin.rpc.service.Impl;

import com.unboundid.ldap.sdk.*;
import com.unboundid.ldap.sdk.controls.SubentriesRequestControl;
import com.zorkdata.center.admin.biz.*;
import com.zorkdata.center.admin.config.LdapConfiguration;
import com.zorkdata.center.admin.entity.*;
import com.zorkdata.center.admin.rpc.service.Ifc.UserServiceIfc;
import com.zorkdata.center.admin.util.MyException;
import com.zorkdata.center.admin.vo.UserInfo;
import com.zorkdata.center.admin.util.PermissionCode;
import com.zorkdata.center.admin.util.PermissionUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.MessageDigest;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description: ${todo}
 * @author: huziyue
 * @create: 2018/3/20 10:25
 */
@Service
@Transactional(rollbackFor = Exception.class,noRollbackFor= MyException.class)
public class UserServiceImpl implements UserServiceIfc {
    @Autowired
    private UserBiz userBiz;
    @Autowired
    private RoleBiz roleBiz;
    @Autowired
    private UserGroupBiz userGroupBiz;
    @Autowired
    private UserRoleBiz userRoleBiz;
    @Autowired
    private ProductBiz productBiz;
    @Autowired
    private PermissionBiz permissionBiz;
    @Autowired
    private ActionBiz actionBiz;
    @Autowired
    PermissionUtil permissionUtil;
    @Autowired
    private LdapConfiguration ldapConfiguration;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Override
    public UserInfo validate(String userName,String password,String produceCode)throws Exception{
        UserInfo userInfo = new UserInfo();
        if(userName==null&&password==null){
            throw new Exception("用户不存在");
        }else {
            User user = userBiz.getUserByUserName(userName,null);
            if(user!=null){
                List<Product> products = productBiz.getProductByOwnerID(user.getUserID());
                if(products!=null&&products.size()>0){
                    if (encoder.matches(password,user.getPassword())){
                        BeanUtils.copyProperties(user,userInfo);
                        userInfo.setUserId(user.getUserID().toString());
                    }
                    return userInfo;
                }
                Integer productID = productBiz.getProductIDByProductCode(produceCode);
                List<Long> roleIds = roleBiz.getRoleIDByProductID(productID);
                List<Long> userIds = userRoleBiz.getUserIDByRoleIds(roleIds);
                if(userIds.contains(user.getUserID())){
                    if (encoder.matches(password,user.getPassword())){
                        BeanUtils.copyProperties(user,userInfo);
                        userInfo.setUserId(user.getUserID().toString());
                    }
                    return userInfo;
                }else {
                    throw new Exception("当前产品内不存在此用户");
                }
            }
            if (ldapConfiguration.isOpen()) {
                SearchResultEntry entry = query_ldap(userName);
                if (entry != null) {
                    insert_OA_user(entry);
                    throw new MyException("当前产品内不存在此用户");
                }
            }
            return null;
        }
    }

    @Override
    public Boolean insert(Long userID,String productCode,User user,Long groupID) {
        List<Product> products = productBiz.getProductByOwnerID(userID);
        if (products != null&&products.size()>0) {
            userBiz.insertSelective(user);
            UserGroup userGroup = new UserGroup();
            userGroup.setUserID(user.getUserID());
            userGroup.setGroupID(groupID);
            userGroupBiz.insertSelective(userGroup);
            if (groupID!=1){
                UserGroup userGroup1 = new UserGroup();
                userGroup1.setGroupID(1L);
                userGroup1.setUserID(user.getUserID());
                userGroupBiz.insertSelective(userGroup1);
            }
            return true;
        }
        Long actionID = actionBiz.getActionIDByActionCode(PermissionCode.PERMISSION_CONTROL);
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
            userBiz.insertSelective(user);
            UserGroup userGroup = new UserGroup();
            userGroup.setUserID(user.getUserID());
            userGroup.setGroupID(groupID);
            userGroupBiz.insertSelective(userGroup);
            if (groupID!=1){
                UserGroup userGroup1 = new UserGroup();
                userGroup1.setGroupID(1L);
                userGroup1.setUserID(user.getUserID());
                userGroupBiz.insertSelective(userGroup1);
            }
            return true;
        }
        return false;
    }
    @Override
    public Boolean updateSelective(Long userID,String productCode,User user) {
        List<Product> products = productBiz.getProductByOwnerID(userID);
        if (products != null&&products.size()>0) {
            userBiz.updateByUserID(user);
            return true;
        }
        Long actionID = actionBiz.getActionIDByActionCode(PermissionCode.PERMISSION_CONTROL);
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
            userBiz.updateByUserID(user);
            return true;
        }
        return false;
    }
    @Override
    public Boolean deleteUserByIds(Long userID,String productCode,List<Long> ids) {
        Boolean flag = permissionUtil.haveActionRole(PermissionCode.PERMISSION_CONTROL, userID, productCode);
        if(flag){
            Set<Long> set = new HashSet<>();
            for (Long id:ids) {
                set.add(id);
            }
            userRoleBiz.deleteByUserIds(ids);
            userGroupBiz.deleteByUserIds(set);
            permissionBiz.deleteBySourceIds(PermissionCode.USER,ids);
            userBiz.deleteByIds(set);
            return true;
        }
       return false;
    }

    @Override
    public List<User> getUserByGroupId(Long groupID) {
        return userBiz.getUserByGroupId(groupID);
    }

    @Override
    public User getUserByUserName(String userName,Long userID) {
        return userBiz.getUserByUserName(userName,userID);
    }

    @Override
    public List<User> getAllUser() {
        return userBiz.getAllUser();
    }

    @Override
    public List<User> getUserByProduct(String productCode) {
        List<Long> roleIds = roleBiz.getRoleIDByProductID(productBiz.getProductIDByProductCode(productCode));
        List<Long> userIds=userRoleBiz.getUserIDByRoleIds(roleIds);
        return userBiz.getUserByIds(userIds);
    }

    @Override
    public Boolean editRoleToUser(Long userID, Map<String, List<Long>> roleIds,Long editUserID,String productCode) {
        Boolean flag = permissionUtil.haveActionRole(PermissionCode.PERMISSION_CONTROL, userID, productCode);
        if(flag){
            if(roleIds.get("insert")!=null&&roleIds.get("insert").size()!=0){
                for (Long roleID:roleIds.get("insert")) {
                    UserRole userRole = new UserRole();
                    userRole.setRoleID(roleID);
                    userRole.setUserID(editUserID);
                    userRoleBiz.insertSelective(userRole);
                }
            }
            if(roleIds.get("delete")!=null&&roleIds.get("delete").size()!=0) {
                userRoleBiz.deleteByUserIDAndRoleIds(editUserID, roleIds.get("delete"));
            }
            return true;
        }
        return false;
    }

    @Override
    public Boolean inOtherProduct(Long userID, String productCode) {
        Set<Long> roleIds = userRoleBiz.getRoleIDByUserID(userID);
        Integer productID= productBiz.getProductIDByProductCode(productCode);
        if(roleIds!=null&&roleIds.size()!=0){
            List<Role> roles = roleBiz.getRoleByIds(roleIds);
            Set<Integer> productIds = new HashSet<>();
            for (Role role:roles) {
                productIds.add(role.getProductID());
            }
            if(productIds.size()==1&&productIds.contains(productID)){
                return false;
            }else {
                return true;
            }
        }
        return false;
    }


    public SearchResultEntry query_ldap(String username){
        String ldapHost = ldapConfiguration.getLdapHost();
        int ldapPort = ldapConfiguration.getLdapPort();
        String ldapBindDN = ldapConfiguration.getLdapBindDN();
        String ldapPassword = ldapConfiguration.getLdapPassword();
        LDAPConnection connection = null;
        try {
            connection = new LDAPConnection(ldapHost, ldapPort, ldapBindDN, ldapPassword);
        } catch (Exception e) {
            System.out.println("连接LDAP出现错误：\n" + e.getMessage());
            return null;
        }
        String root = ldapConfiguration.getRoot();
        String dc = ldapConfiguration.getDc();
        String ou = ldapConfiguration.getOu();
        String filter = "(&(objectClass=top)(objectClass=person)(uid="+username+"))";
        SearchResultEntry entrys=null;
        try {
            SearchRequest searchRequest = new SearchRequest("dc=" + ou + ",dc=" + dc + ",dc=" + root, SearchScope.SUB, filter);
            searchRequest.addControl(new SubentriesRequestControl());
            SearchResult searchResult = connection.search(searchRequest);
            for (SearchResultEntry entry : searchResult.getSearchEntries()) {
                entrys=entry;
            }
        }catch (Exception e){
            System.out.println("查询错误，错误信息如下：\n" + e.getMessage());
            return null;
        }
        return entrys;
    }
    public void insert_OA_user(SearchResultEntry entry ){
        User user = new User();
        user.setUserName(entry.getAttributeValue("uid"));
        user.setTrueName(entry.getAttributeValue("cn"));
        user.setPassword(MD5(ldapConfiguration.getInitPassword()));
        userBiz.insertSelective(user);
        UserGroup userGroup = new UserGroup();
        userGroup.setUserID(user.getUserID());
        userGroup.setGroupID(1L);
        userGroupBiz.insertSelective(userGroup);
    }
    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}
