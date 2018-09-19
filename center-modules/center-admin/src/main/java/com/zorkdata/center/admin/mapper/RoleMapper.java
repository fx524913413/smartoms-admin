package com.zorkdata.center.admin.mapper;

import com.zorkdata.center.admin.entity.Role;
import com.zorkdata.center.admin.vo.RoleInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Set;

/**
 * @description: ${todo}
 * @author: huziyue
 * @create: 2018/3/20 15:22
 */
@Repository
public interface RoleMapper extends Mapper<Role> {

    /**
     * 为当前系统添加一个角色
     *
     * @param entity 新建角色数据
     */
    void insertSelectiveGetId(Role entity);

    /**
     * 编辑当前角色数据
     *
     * @param role 当前角色数据
     */
    void updateByRoleID(Role role);

    /**
     * 通过id数组批量删除角色
     *
     * @param ids id的数组
     */
    void deleteByIds(List<Long> ids);

    /**
     * 通过产品id查询该产品下所有角色
     *
     * @param productID
     * @return
     */
    List<Role> getRoleByProductID(Integer productID);

    /**
     * 通过产品id获取该产品下所有角色前端显示模型
     *
     * @param productID
     * @return
     */
    List<RoleInfo> getRoleInfoByProductID(Integer productID);

    /**
     * 通过产品id获取其下所有角色id
     *
     * @param productID
     * @return
     */
    List<Long> getRoleIDByProductID(Integer productID);

    /**
     * 根据角色id查询角色
     *
     * @param roleIds
     * @return
     */
    List<Role> getRoleByIds(@Param("roleIds") Set<Long> roleIds);

    /**
     * 通过角色名和产品id查询角色
     *
     * @param roleName
     * @param productID
     * @return
     */
    Role getRoleByRoleNameAndProductCode(@Param("roleName") String roleName, @Param("productID") Integer productID,@Param("roleID")Long roleID);
}
