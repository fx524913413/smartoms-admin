package com.zorkdata.center.admin.biz;

import com.zorkdata.center.admin.entity.Group;
import com.zorkdata.center.admin.entity.License;
import com.zorkdata.center.admin.mapper.LicenseMapper;
import com.zorkdata.center.common.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


/**
 * @author dingyu
 * @description: ${todo}
 * @create 2018/4/8 9:22
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LicenseBiz extends BaseBiz<LicenseMapper, License> {
    @Override
    public void insertSelective(License entity) {
        mapper.insertSelectiveGetId(entity);
    }


    public void updateByGroupId(License license) {
        mapper.updateByGroupId(license);
    }

    public License getLicenseById(Long groupID) {
        return mapper.getLicenseById(groupID);
    }

    public void deleteLicenseByLicenseId(License license) {
        mapper.deleteLicenseByLicenseId(license);
    }

    public List<License> getAllLicense() {
        return mapper.getAllLicense();
    }

    public void deleteLicenseByLicenseIds(Set<Long> licenselist) {
        mapper.deleteLicenseByLicenseIds(licenselist);
    }

    public List<Group> getAllGroupIDs() {
        return mapper.getAllGroupIDs();
    }
}
