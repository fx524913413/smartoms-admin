package com.zorkdata.center.admin.mapper;

import com.zorkdata.center.admin.entity.Permission;
import com.zorkdata.center.admin.entity.SuperEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Set;

/**
 * @description: ${todo}
 * @author: huziyue
 * @create: 2018/3/20 16:10
 */
@Repository
public interface PermissionMapper extends Mapper<Permission> {

    /**
     * 解除角色关联菜单
     *
     * @param deleteIds  需要解除权限id
     * @param sourceID   解除对象的id
     * @param sourceType 标记当前解除对象类型
     * @param resource   需要解除的权限类型
     */
    void deletePermissionByIds(@Param("resource") String resource, @Param("deleteIds") List<Long> deleteIds, @Param("sourceID") Long sourceID, @Param("sourceType") String sourceType);

    /**
     * 根据角色或者用户组来查询所有资源id
     *
     * @param source    标记当前需要查询的是用户组还是角色
     * @param sourceIds 用户组id或角色id
     * @param resource  需要查询资源的类型
     * @return
     */
    List<Permission> getResourceAndResourceID(@Param("source") String source, @Param("sourceIds") Set<Long> sourceIds, @Param("resource") String resource);

    /**
     * 通过资源名和资源id获取资源
     *
     * @param resourceName 资源名
     * @param resourceIds  资源id集合
     * @return
     */
    List<SuperEntity> getResourceByResourceID(@Param("resourceName") String resourceName, @Param("resourceIds") Set<Long> resourceIds);

    /**
     * 根据source查询所有电脑的id
     *
     * @param source
     * @param sourceIds
     * @param resource
     * @return
     */
    List<Long> getResourceID(@Param("source") String source, @Param("sourceIds") Set<Long> sourceIds, @Param("resource") String resource);

    /**
     * 根据提供的source和sourceids查询所有权限
     *
     * @param source
     * @param sourceIds
     * @return
     */
    List<Permission> getPermissionBySource(@Param("source") String source, @Param("sourceIds") Set<Long> sourceIds);

    /**
     * 根据资源ID删除权限表中的相关资源数据
     *
     * @param resource
     * @param rescourceIds
     */
    void deleteByResourceIds(@Param("resource") String resource, @Param("resourceIds") Set<Long> resourceIds);

    /**
     * 根据权限的类型获取sourceid
     *
     * @param resource
     * @param resourceID
     * @param sourceType
     * @return
     */
    List<Long> getSourceIDByResourceID(@Param("resource") String resource, @Param("resourceID") Long resourceID, @Param("sourceType") String sourceType);

    /**
     * 删除某个角色或人的权限
     *
     * @param sourceType
     * @param sourceIds
     */
    void deleteBySourceIds(@Param("sourceType") String sourceType, @Param("sourceIds") List<Long> sourceIds);
}
