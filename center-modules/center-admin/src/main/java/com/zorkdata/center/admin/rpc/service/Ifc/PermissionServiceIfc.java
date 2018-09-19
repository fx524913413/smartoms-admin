package com.zorkdata.center.admin.rpc.service.Ifc;

import com.zorkdata.center.admin.entity.Computer;
import com.zorkdata.center.admin.entity.Permission;
import com.zorkdata.center.admin.entity.Product;
import com.zorkdata.center.admin.entity.SuperEntity;
import com.zorkdata.center.admin.vo.ComputerVo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/27 10:49
 */
public interface PermissionServiceIfc {


//    /**
//     * 根据角色或者用户组来查询所有资源
//     * @param source 标记当前需要查询的是用户组还是角色
//     * @param sourceID 用户组id或角色id
//     * @param resource 需要查询资源的类型
//     * @return
//     */
//    SuperEntity getResource(String source, List<Long> sourceID, String resource);

    /**
     * 通过用户id查询用户所具有的资源
     *
     * @param userID
     * @param productCode
     * @return
     */
    SuperEntity getResourceByUserID(Long userID, String productCode);

    /**
     * 通过管理员id或者产品负责人id获取所有可支配权限
     *
     * @param productCode
     * @param userID
     * @return
     */
    SuperEntity getPermissionByUserID(String productCode, Long userID);

    /**
     * 获取所有权限
     *
     * @return
     */
    SuperEntity getAllPermission(Integer productID);

    /**
     * 根据用户id查询所有机器
     *
     * @param userID
     * @return
     */
    List<Computer> getComputerByUserId(Long userID);
    /**
     * 根据产品负责人获取所有权限
     * @param userID 用户id
     * @return
     */
    //Map<String,Set<Long>> getPermissionByOwer(Long userID);

    /**
     * 根据userid获取其所有权限
     * @param userID
     * @return
     */
    //Map<String,Set<Long>> getPermissionByUserId(Long userID, Integer productID);

    /**
     * 根据前端传来的类型进行赋权
     *
     * @param permissionMap
     * @param sourceType
     * @param sourceID
     */
    Boolean givePermission(Map<String, Map<String, List<Long>>> permissionMap, String sourceType, Long sourceID, Long userID, String productCode);

    /**
     * 根据角色id获取权限
     *
     * @param roleID
     * @return
     */
    Map<String, Set<Long>> getPermissionByRoleID(Long roleID);

    /**
     * 根据用户id获取机器
     *
     * @param userID
     * @return
     */
    List<ComputerVo> getComputerVoByUserId(Long userID);
}
