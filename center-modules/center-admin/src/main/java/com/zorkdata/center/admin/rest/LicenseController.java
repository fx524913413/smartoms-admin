package com.zorkdata.center.admin.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zorkdata.center.admin.biz.LicenseBiz;
import com.zorkdata.center.admin.entity.Group;
import com.zorkdata.center.admin.entity.User;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import com.zorkdata.center.common.rest.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zorkdata.center.admin.entity.License;
import com.zorkdata.center.admin.rpc.service.Ifc.LicenseServiceIfc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @description: ${todo}
 * @author: huziyue
 * @create: 2018/3/20 10:07
 */
@RestController
@RequestMapping("license")
public class LicenseController extends BaseController<LicenseBiz, License> {
    @Autowired
    private LicenseServiceIfc licenseService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public RespModel addLicense(@RequestBody License license) {

        licenseService.insert(license);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public RespModel updateLicense(@RequestBody License license) {
        licenseService.updateSelective(license);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/getLicenseByGroupId", method = RequestMethod.POST)
    @ResponseBody
    public RespModel getLicenseByGroupId(Long groupID) {
        License license = licenseService.getLicenseById(groupID);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, license);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public RespModel deleteLicense(@RequestBody License license) {
        licenseService.deleteLicenseByLicenseId(license);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ResponseBody
    public RespModel findAll() {
        List<License> licenses = licenseService.getAllLicense();

        return RespTools.getRespMsgModel(CodeTable.SUCCESS, licenses);
    }

    @RequestMapping(value = "/delBatch", method = RequestMethod.POST)
    @ResponseBody
    public RespModel deleteLicenses(@RequestBody List<Long> LicensesIDList) {
        Set<Long> LicensesIds = new HashSet<>();
        for (Long LicenseId : LicensesIDList) {
            LicensesIds.add(LicenseId);
        }
        licenseService.deleteLicenses(LicensesIds);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/findGroupID", method = RequestMethod.GET)
    @ResponseBody
    public RespModel findGroupID() {
        List<Group> licenses = licenseService.getAllGroupIDs();

        return RespTools.getRespMsgModel(CodeTable.SUCCESS, licenses);
    }
}
