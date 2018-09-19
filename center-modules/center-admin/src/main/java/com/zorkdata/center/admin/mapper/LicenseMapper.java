package com.zorkdata.center.admin.mapper;

import com.zorkdata.center.admin.entity.Group;
import com.zorkdata.center.admin.entity.License;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Set;

/**
 * @description: ${todo}
 * @author: dingyu
 * @create: 2018/4/4 11:20
 */
@Repository
public interface LicenseMapper extends Mapper<License> {

    /**
     * 向证书表表插入一条数据并返回主键id
     *
     * @param license
     */
    void insertSelectiveGetId(License license);

    /**
     * 根据组id编辑证书数据
     *
     * @param license 更改license数据
     */
    void updateByGroupId(License license);

    /**
     * 通过组id获得用户
     *
     * @param groupID
     * @return
     */
    License getLicenseById(@Param("groupID") Long groupID);

    /**
     * 删除用户组license
     *
     * @param license
     * @return
     */
    void deleteLicenseByLicenseId(License license);

    /**
     * 获取所有用户
     *
     * @return
     */
    List<License> getAllLicense();

    void deleteLicenseByLicenseIds(@Param("licenseIDlist") Set<Long> licenseIDlist);

    /**
     * 获取所有用户组ID
     *
     * @return
     */
    List<Group> getAllGroupIDs();
}
