package com.zorkdata.center.admin.rpc.service.Ifc;

import com.zorkdata.center.admin.entity.Group;
import com.zorkdata.center.admin.entity.License;

import java.util.List;
import java.util.Set;


/**
 * 用户服务层接口
 *
 * @author: dingyu
 * @create: 2018/4/4 13:40
 */
public interface LicenseServiceIfc {
    /**
     * 为组添加一个证书
     *
     * @param license 用户数据
     */
    void insert(License license);

    /**
     * 更改license数据
     *
     * @param license 用户组license数据
     */
    void updateSelective(License license);

    /**
     * 获取license数据
     *
     * @param groupID 用户组ID
     */
    License getLicenseById(Long groupID);

    /**
     * 删除license数据
     *
     * @param license 用户组license数据
     */
    void deleteLicenseByLicenseId(License license);

    /**
     * 获取所有证书
     *
     * @return
     */
    List<License> getAllLicense();

    /**
     * 删除对应证书
     *
     * @return
     */
    void deleteLicenses(Set<Long> licenseIDlist);

    //查询未新增证书的组
    List<Group> getAllGroupIDs();
}
