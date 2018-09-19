package com.zorkdata.center.admin.rpc.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.zorkdata.center.admin.biz.LicenseBiz;
import com.zorkdata.center.admin.entity.Group;
import com.zorkdata.center.admin.entity.License;
import com.zorkdata.center.admin.rpc.service.Ifc.LicenseServiceIfc;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;

/**
 * @description: ${todo}
 * @author: dingyu
 * @create: 2018/4/4 13:38
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LicenseServiceImpl implements LicenseServiceIfc {
    @Autowired
    private LicenseBiz licenseBiz;

    private Base64 BASE64 = new Base64();
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    @Override
    public void insert(License license) {
        JsonObject object = new JsonObject();
        object.addProperty("user_amount", license.getUser_amount());
        object.addProperty("expire_date", license.getExpire_date());
        object.addProperty("machine_amount", license.getMachine_amount());
        license.setLicenseMessage(object.toString());
        byte[] bytes = BASE64.encode(license.getLicenseMessage().getBytes(DEFAULT_CHARSET));
        license.setLicenseMessage(new String(bytes, DEFAULT_CHARSET));
        licenseBiz.insertSelective(license);
    }

    @Override
    public void updateSelective(License license) {
        JsonObject object = new JsonObject();
        object.addProperty("user_amount", license.getUser_amount());
        object.addProperty("expire_date", license.getExpire_date());
        object.addProperty("machine_amount", license.getMachine_amount());
        license.setLicenseMessage(object.toString());
        byte[] bytes = BASE64.encode(license.getLicenseMessage().getBytes(DEFAULT_CHARSET));
        license.setLicenseMessage(new String(bytes, DEFAULT_CHARSET));
        licenseBiz.updateByGroupId(license);
    }

    @Override
    public License getLicenseById(Long groupID) {
        License license = licenseBiz.getLicenseById(groupID);
        byte[] bytes = BASE64.decode(license.getLicenseMessage().getBytes(DEFAULT_CHARSET));
        license.setLicenseMessage(new String(bytes, DEFAULT_CHARSET));
        return license;
    }

    @Override
    public void deleteLicenseByLicenseId(License license) {
        licenseBiz.deleteLicenseByLicenseId(license);
    }

    @Override
    public List<License> getAllLicense() {
        List<License> licenses = licenseBiz.getAllLicense();
        for (License license : licenses) {
            byte[] bytes = BASE64.decode(license.getLicenseMessage().getBytes(DEFAULT_CHARSET));
            JSONObject json = JSONObject.parseObject(new String(bytes, DEFAULT_CHARSET));
            license.setExpire_date(json.getString("expire_date"));
            license.setMachine_amount(json.getString("machine_amount"));
            license.setUser_amount(json.getString("user_amount"));
        }
        return licenseBiz.getAllLicense();
    }

    @Override
    public void deleteLicenses(Set<Long> licenseIDlist) {
        licenseBiz.deleteLicenseByLicenseIds(licenseIDlist);
    }

    @Override
    public List<Group> getAllGroupIDs() {
        return licenseBiz.getAllGroupIDs();
    }

}
