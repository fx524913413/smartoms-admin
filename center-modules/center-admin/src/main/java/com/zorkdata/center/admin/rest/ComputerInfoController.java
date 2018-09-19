package com.zorkdata.center.admin.rest;

import com.zorkdata.center.admin.biz.ComputerBiz;
import com.zorkdata.center.admin.biz.PermissionBiz;
import com.zorkdata.center.admin.biz.UserRoleBiz;
import com.zorkdata.center.admin.entity.Computer;
import com.zorkdata.center.admin.entity.Permission;
import com.zorkdata.center.admin.entity.Product;
import com.zorkdata.center.admin.rpc.service.Ifc.ComputerServiceIfc;
import com.zorkdata.center.admin.rpc.service.Ifc.ProductServiceIfc;
import com.zorkdata.center.admin.util.PermissionCode;
import com.zorkdata.center.admin.vo.ComputerInfo;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@MultipartConfig
@RestController
@RequestMapping("host")
public class ComputerInfoController {
    @Autowired
    private ComputerServiceIfc computerServiceIfc;

    @Autowired
    private UserRoleBiz userRoleBiz;

    @Autowired
    private ComputerBiz computerBiz;

    @Autowired
    private PermissionBiz permissionBiz;

    @Autowired
    private ProductServiceIfc productServiceIfc;

    @RequestMapping(value = "/getComputerInfo", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getComputerInfo(Long computerID) throws Exception {
        ComputerInfo returninfo = computerServiceIfc.findcomputerinfo(computerID);
        if (returninfo == null) {
            return RespTools.getRespMsgModel(CodeTable.FAILED, null);
        } else {
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, returninfo);
        }
    }

    @RequestMapping(value = "/getComputerByAgentID", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getComputerByAgentID(Long agentID) throws Exception {
        Computer computer = computerServiceIfc.getComputerByAgentID(agentID);
        if (computer == null) {
            return RespTools.getRespMsgModel(CodeTable.FAILED, null);
        } else {
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, computer);
        }
    }

    @RequestMapping(value = "/addComputerInfo", method = RequestMethod.POST)
    @ResponseBody
    public RespModel addComputerinfo(Long userId, @RequestBody Computer computer) throws Exception {
        boolean flag = true;
        flag = computerServiceIfc.insertComputerEntity(computer);
        if (flag == true) {
            try {
                Set<Long> rolesId = new HashSet<>();
                rolesId = userRoleBiz.getRoleIDByUserID(userId);
                String role = "role";
                if (rolesId == null) {
                    role = "userId";
                }
                computerBiz.insertSelective(computer);
            } catch (Exception e) {
                computerServiceIfc.deleteComputerEntity(computer.getComputerID());
                return RespTools.getRespMsgModel(CodeTable.FAILED, "插入用户和Computer关系表失败");
            }
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
        } else {
            return RespTools.getRespMsgModel(CodeTable.IP_DUPLICATION, "该IP已存在或者插入出错");
        }
    }

    @RequestMapping(value = "/updateComputerInfo", method = RequestMethod.POST)
    @ResponseBody
    public RespModel updateComputerinfo(Long userId, @RequestBody Computer computer) {
        boolean flag = true;
        flag = computerServiceIfc.updateComputerEntity(computer);
        if (flag) {
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
        } else {
            return RespTools.getRespMsgModel(CodeTable.IP_DUPLICATION, "修改后的IP和其他对象IP重复或者更新异常");
        }
    }

    @RequestMapping(value = "/delComputerInfo", method = RequestMethod.POST)
    @ResponseBody
    public RespModel deleteComputerinfo(@RequestBody Computer computer) {
        computerServiceIfc.deleteComputerEntity(computer.getComputerID());
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/delBatch", method = RequestMethod.POST)
    @ResponseBody
    public RespModel deleteComputerinfos(@RequestBody List<Long> computerIDList) {
        Set<Long> computerIds = new HashSet<>();
        for (Long computerId : computerIDList) {
            computerIds.add(computerId);
        }
        computerServiceIfc.deleteComputers(computerIds);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public RespModel uploadExcelComputerinfo(@RequestBody MultipartFile multipartFiles) {
        //filePath 要这种格式"D:\\readExcel.xlsx";
        computerServiceIfc.uploadExcelComputerinfo(multipartFiles);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ResponseBody
    public RespModel findAll(Long userId) {
        List<Computer> computerList = computerServiceIfc.getComputerList(userId);
//        if(computerList==null){
//            return RespTools.getRespMsgModel(CodeTable.SUCCESS_NODATA,"没有可以安装的机器，请先添加机器");
//        }
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, computerList);
    }

    @RequestMapping(value = "/findAllComputer", method = RequestMethod.GET)
    @ResponseBody
    public RespModel findAllComputer() {
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, computerServiceIfc.findAllComputer());
    }

    /**
     * 获取所有机器的同时获取tag
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/findComputerAndTag", method = RequestMethod.GET)
    @ResponseBody
    public RespModel findComputerAndTag(Long userID) {
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, computerServiceIfc.findComputerAndTag(userID));
    }

    @RequestMapping(value = "/getComputersSortByProductCode", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getComputersSortByProductCode(String productID,Long userID) {
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, computerServiceIfc.getComputersSortByProductCode(productID,userID));
    }
}
