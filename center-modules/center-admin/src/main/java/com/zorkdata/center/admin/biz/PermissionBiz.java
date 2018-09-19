package com.zorkdata.center.admin.biz;

import com.zorkdata.center.admin.entity.Permission;
import com.zorkdata.center.admin.entity.SuperEntity;
import com.zorkdata.center.admin.mapper.*;
import com.zorkdata.center.common.biz.BaseBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/27 10:47
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionBiz extends BaseBiz<PermissionMapper, Permission> {

    public void deletePermissionByIds(String resource, List<Long> deleteIds, Long sourceID, String sourceType) {
        mapper.deletePermissionByIds(resource, deleteIds, sourceID, sourceType);
    }

    public Map<String, Set<Long>> getResourceAndResourceID(String source, Set<Long> sourceIds, String resource) {
        List<Permission> permissions = mapper.getResourceAndResourceID(source, sourceIds, resource);
        return getResourceMap(permissions);
    }

    //封装返回权限结果集
    private Map<String, Set<Long>> getResourceMap(List<Permission> permissions) {
        HashMap<String, Set<Long>> resourceMap = new HashMap<>();
        for (Permission permission : permissions) {
            if (resourceMap.containsKey(permission.getResource())) {
                resourceMap.get(permission.getResource()).add(permission.getResourceID());
            } else {
                Set<Long> set = new HashSet<>();
                set.add(permission.getResourceID());
                resourceMap.put(permission.getResource(), set);
            }
        }
        return resourceMap;
    }

    public List<SuperEntity> getResourceByResourceID(String resourceName, Set<Long> resourceIds) {
        return mapper.getResourceByResourceID(resourceName, resourceIds);
    }

    public List<Long> getResourceID(String source, Set<Long> sourceIds, String resource) {
        return mapper.getResourceID(source, sourceIds, resource);
    }

    public List<Permission> getPermissionBySource(String source, Set<Long> sourceIds) {
        return mapper.getPermissionBySource(source, sourceIds);
    }

    public void deleteByResourceIds(String resource, Set<Long> resourceIds) {
        mapper.deleteByResourceIds(resource, resourceIds);
    }

    public List<Long> getSourceIDByResourceID(String resource, Long resourceID, String sourceType) {
        return mapper.getSourceIDByResourceID(resource, resourceID, sourceType);
    }

    public void deleteBySourceIds(String sourceType, List<Long> sourceIds) {
        mapper.deleteBySourceIds(sourceType, sourceIds);
    }
}
