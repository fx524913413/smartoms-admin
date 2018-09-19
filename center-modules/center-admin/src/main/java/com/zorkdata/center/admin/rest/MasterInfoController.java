package com.zorkdata.center.admin.rest;

import com.zorkdata.center.admin.entity.Computer;
import com.zorkdata.center.admin.entity.Master;
import com.zorkdata.center.admin.mapper.MasterMapper;
import com.zorkdata.center.admin.rpc.service.Ifc.*;
import com.zorkdata.center.admin.vo.ComputerAndComputerSaltType;
import com.zorkdata.center.admin.vo.MasterInfo;
import com.zorkdata.center.admin.vo.MasterVo;
import com.zorkdata.center.admin.vo.TestPortResultsInSetup;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("master")
public class MasterInfoController {
    @Autowired
    private MasterServiceIfc masterServiceIfc;
    @Autowired
    private FileServiceIfc fileServiceIfc;
    @Autowired
    private TestportServiceIfc testportServiceIfc;
    @Autowired
    private ClusterServiceIfc clusterServiceIfc;
    @Autowired
    private ComputerServiceIfc computerServiceIfc;
    @Autowired
    private FindAllComputersInClusterIfc findAllComputersInClusterIfc;

    @RequestMapping(value = "/masterinfo", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getMasterinfo(Long masterID) {
        MasterInfo masterInfo = masterServiceIfc.findMasterinfo(masterID);
        if (masterInfo == null) {
            return RespTools.getRespMsgModel(CodeTable.FAILED, null);
        } else {
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, masterInfo);
        }
    }

    @RequestMapping(value = "/addmasterinfo", method = RequestMethod.POST)
    @ResponseBody
    public RespModel addMasterinfo(@RequestBody Master master) throws Exception {
        masterServiceIfc.insertMasterEntity(master);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/updatamasterinfo", method = RequestMethod.POST)
    @ResponseBody
    public RespModel updateMasterinfo(@RequestBody Master master) throws Exception {
        masterServiceIfc.insertMasterEntity(master);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/deltemasterinfo", method = RequestMethod.POST)
    @ResponseBody
    public RespModel deleteMasterinfo(Long masterid) throws Exception {
        masterServiceIfc.deleteMasterEntity(masterid);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/insMas", method = RequestMethod.POST)
    @ResponseBody
    public RespModel insMaster(@RequestBody MasterVo masterVo, Long userId, Long clusterID, String productCode) {
        //安装前先检测22和8000端口是否满足安装要求
        List<Computer> computerList1 = masterVo.getComputerList();
        List<TestPortResultsInSetup> resultsInSetups = testportServiceIfc.testportinsetup(computerList1);
        for (TestPortResultsInSetup testPortResultsInSetup : resultsInSetups) {
            if (testPortResultsInSetup.getPort().equals(22) && testPortResultsInSetup.getResult().equals(false)) {
                return RespTools.getRespMsgModel(CodeTable.FAILED, resultsInSetups);
            }
            //现在测试阶段，在8000端口通的情况下，依然允许安装,正式情况需要打开此处限制
//            if(testPortResultsInSetup.getPort().equals(8000)&&testPortResultsInSetup.getResult().equals(true)){
//                return RespTools.getRespMsgModel(CodeTable.FAILED,resultsInSetups);
//            }
        }
        //下面这套逻辑用于用户连续添加master时，在后台做master数量的验证，虽然在getComputerByClusterID此接口中也进行域中master数量校验
        //检测此域内Master数量是否超过规定
        List<Computer> computerListByUserid = computerServiceIfc.getComputerList(userId);
        if (computerListByUserid == null) {
            return RespTools.getRespMsgModel(CodeTable.NoComputerToInstall, "没有可以安装的机器，请先添加机器");
        }
        List<ComputerAndComputerSaltType> computerAndComputerSaltTypeList = new ArrayList<ComputerAndComputerSaltType>();
        computerAndComputerSaltTypeList = findAllComputersInClusterIfc.getComputerAndComputerTypeList(clusterID);
        List<ComputerAndComputerSaltType> computerAndComputerSaltTypeListByUserid = new ArrayList<ComputerAndComputerSaltType>();
        for (Computer computerByUserid : computerListByUserid) {
            for (ComputerAndComputerSaltType computerAndComputerSaltType : computerAndComputerSaltTypeList) {
                if (computerAndComputerSaltType.getComputer().getComputerID().equals(computerByUserid.getComputerID())) {
                    computerAndComputerSaltTypeListByUserid.add(computerAndComputerSaltType);
                }
            }
        }
        int count = 0;
        for (ComputerAndComputerSaltType computerAndComputerSaltType : computerAndComputerSaltTypeList) {
            if (computerAndComputerSaltType.getComputerSaltType().equals("1") || computerAndComputerSaltType.getComputerSaltType().equals("3")) {
                count += 1;
            }
        }
        if (count >= 2) {
            return RespTools.getRespMsgModel(CodeTable.MasterTooMuch, "此域内master数量已经饱和");
        }

        //如果没有问题，那就进行安装
        Boolean flag = masterServiceIfc.insMaster(masterVo, clusterID, userId, productCode);
        if (flag == null) {
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, "账号密码错误");
        }
        if (flag) {
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
        }
        return RespTools.getRespMsgModel(CodeTable.NOT_ALLOW, null);
    }

    @RequestMapping(value = "/batchInsMas", method = RequestMethod.POST)
    @ResponseBody
    public RespModel insMasterByHand(@RequestBody MasterVo masterVo, Long clusterID, Long userId, String productCode) {
        //检测此域内Master数量是否超过规定
        List<Computer> computerListByUserid = computerServiceIfc.getComputerList(userId);
        if (computerListByUserid == null) {
            return RespTools.getRespMsgModel(CodeTable.NoComputerToInstall, "没有可以安装的机器，请先添加机器");
        }
        List<ComputerAndComputerSaltType> computerAndComputerSaltTypeList = new ArrayList<ComputerAndComputerSaltType>();
        computerAndComputerSaltTypeList = findAllComputersInClusterIfc.getComputerAndComputerTypeList(clusterID);
        List<ComputerAndComputerSaltType> computerAndComputerSaltTypeListByUserid = new ArrayList<ComputerAndComputerSaltType>();
        for (Computer computerByUserid : computerListByUserid) {
            for (ComputerAndComputerSaltType computerAndComputerSaltType : computerAndComputerSaltTypeList) {
                if (computerAndComputerSaltType.getComputer().getComputerID().equals(computerByUserid.getComputerID())) {
                    computerAndComputerSaltTypeListByUserid.add(computerAndComputerSaltType);
                }
            }
        }
        int count = 0;
        for (ComputerAndComputerSaltType computerAndComputerSaltType : computerAndComputerSaltTypeList) {
            if (computerAndComputerSaltType.getComputerSaltType().equals("1") || computerAndComputerSaltType.getComputerSaltType().equals("3")) {
                count += 1;
            }
        }
        if (count >= 2) {
            return RespTools.getRespMsgModel(CodeTable.MasterTooMuch, "此域内master数量已经饱和");
        }
        List<Long> masterList = masterServiceIfc.batchInsMas(masterVo, clusterID, userId, productCode);
        if (masterList == null) {
            return RespTools.getRespMsgModel(CodeTable.NOT_ALLOW, null);
        }
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, masterList);
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ResponseBody
    public RespModel findAllMaster(Long userId) {
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, masterServiceIfc.findAllMaster(userId));
    }

    @RequestMapping(value = "/getCommand", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getCommand() {
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, masterServiceIfc.getCommand());
    }


}
