package com.zorkdata.center.admin.rest;

import com.alibaba.fastjson.JSONArray;
import com.zorkdata.center.admin.biz.AgentBiz;
import com.zorkdata.center.admin.biz.ClusterComputerRelationBiz;
import com.zorkdata.center.admin.biz.MasterBiz;
import com.zorkdata.center.admin.entity.Cluster;
import com.zorkdata.center.admin.entity.Computer;
import com.zorkdata.center.admin.rpc.service.Ifc.*;
import com.zorkdata.center.admin.vo.ClusterInfo;
import com.zorkdata.center.admin.vo.ComputerAndComputerSaltType;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/24 11:00
 */
@RestController
@RequestMapping("cluster")
public class ClusterController {

    @Autowired
    private ClusterServiceIfc clusterServiceIfc;
    @Autowired
    private FindAllComputersInClusterIfc findAllComputersInClusterIfc;
    @Autowired
    private UpdataComputerInClusterIfc updataComputerInClusterIfc;
    @Autowired
    private ComputerServiceIfc computerServiceIfc;
    @Autowired
    private MasterBiz masterBiz;
    @Autowired
    private AgentBiz agentBiz;
    @Autowired
    private ClusterComputerRelationBiz clusterComputerRelationBiz;


    @RequestMapping(value = "/getClusters", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getClusters(Long userId) {
        List<ClusterInfo> clusterInfoList = clusterServiceIfc.getClusters(userId);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, clusterInfoList);
    }

    @RequestMapping(value = "/addCluster", method = RequestMethod.POST)
    @ResponseBody
    public RespModel addCluster(@RequestBody Cluster cluster) {
        clusterServiceIfc.insertSelective(cluster);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, cluster.getClusterID());
    }

    @RequestMapping(value = "/delCluster", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public RespModel delCluster(@RequestBody Cluster cluster) throws Exception {
        Long clusterID = cluster.getClusterID();
        Set<Long> ids = new HashSet<>();
        ids.add(cluster.getClusterID());
        List<ComputerAndComputerSaltType> deletecomputerList = findAllComputersInClusterIfc.getComputerAndComputerTypeList(clusterID);
        if (deletecomputerList.size() != 0) {
            updataComputerInClusterIfc.deleteComputerInCluster(clusterID, deletecomputerList);
        }
        clusterServiceIfc.deleteClusterById(ids);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    //当clusterID为0时为获取闲置的机器用于配置与新的cluster中
    //不为0时为获取此cluster下的相关机器信息
    @RequestMapping(value = "/getComputerByClusterID", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getComputerByClusterID(Long clusterID, Long userId) throws Exception {
        List<Computer> computerList = clusterServiceIfc.getComputersByClusterID(clusterID);
        List<Computer> computerListByUserid = computerServiceIfc.getComputerList(userId);
        if (computerListByUserid == null) {
            return RespTools.getRespMsgModel(CodeTable.NoComputerToInstall, "没有可以安装的机器，请先添加机器");
        }
        if (clusterID == 0) {
            List<ComputerAndComputerSaltType> computerAndComputerSaltTypeList = new ArrayList<ComputerAndComputerSaltType>();
            for (Computer computerByUserid : computerListByUserid) {
                for (Computer computer : computerList) {
                    if (computerByUserid.getComputerID().equals(computer.getComputerID())) {
                        ComputerAndComputerSaltType computerAndComputerSaltType = new ComputerAndComputerSaltType();
                        computerAndComputerSaltType.setComputer(computer);
                        computerAndComputerSaltType.setComputerSaltType("4");
                        computerAndComputerSaltTypeList.add(computerAndComputerSaltType);
                    }
                }
            }
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, computerAndComputerSaltTypeList);
        } else {
            int count = 0;
            List<ComputerAndComputerSaltType> computerAndComputerSaltTypeList = new ArrayList<ComputerAndComputerSaltType>();
            computerAndComputerSaltTypeList = findAllComputersInClusterIfc.getComputerAndComputerTypeList(clusterID);
            for (ComputerAndComputerSaltType computerAndComputerSaltType : computerAndComputerSaltTypeList) {
                if (computerAndComputerSaltType.getComputerSaltType().equals("1") || computerAndComputerSaltType.getComputerSaltType().equals("3")) {
                    count += 1;
                }
            }
            if (count >= 2) {
                return RespTools.getRespMsgModel(CodeTable.MasterTooMuch, "此域内的Master数量已经超过或达到规定数量（可能是由于别的用于已经在此域内添加了Master）");
            }
            List<ComputerAndComputerSaltType> computerAndComputerSaltTypeListByUserid = new ArrayList<ComputerAndComputerSaltType>();
            for (Computer computerByUserid : computerListByUserid) {
                for (ComputerAndComputerSaltType computerAndComputerSaltType : computerAndComputerSaltTypeList) {
                    if (computerAndComputerSaltType.getComputer().getComputerID().equals(computerByUserid.getComputerID())) {
                        computerAndComputerSaltTypeListByUserid.add(computerAndComputerSaltType);
                    }
                }
            }
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, computerAndComputerSaltTypeListByUserid);
        }
    }

    @RequestMapping(value = "/getComputer", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getComputer(Long clusterID, Long userId) throws Exception {
        List<Computer> computerList = clusterServiceIfc.getComputersByClusterID(clusterID);
        List<Computer> computerListByUserid = computerServiceIfc.getComputerList(userId);
        if (computerListByUserid == null) {
            return RespTools.getRespMsgModel(CodeTable.NoComputerToInstall, "没有可以安装的机器，请先添加机器");
        }
        if (clusterID == 0) {
            List<ComputerAndComputerSaltType> computerAndComputerSaltTypeList = new ArrayList<ComputerAndComputerSaltType>();
            for (Computer computerByUserid : computerListByUserid) {
                for (Computer computer : computerList) {
                    if (computerByUserid.getComputerID().equals(computer.getComputerID())) {
                        ComputerAndComputerSaltType computerAndComputerSaltType = new ComputerAndComputerSaltType();
                        computerAndComputerSaltType.setComputer(computer);
                        computerAndComputerSaltType.setComputerSaltType("4");
                        computerAndComputerSaltTypeList.add(computerAndComputerSaltType);
                    }
                }
            }
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, computerAndComputerSaltTypeList);
        } else {
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
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, computerAndComputerSaltTypeListByUserid);
        }
    }

    @RequestMapping(value = "/getComputer4AgentInstall", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getComputer4AgentInstall(Long clusterID, Long userId) throws Exception {
        List<Computer> computerList = clusterServiceIfc.getComputersByClusterID(clusterID);
        List<Computer> computerListByUserid = computerServiceIfc.getComputerList(userId);
        if (computerListByUserid == null) {
            return RespTools.getRespMsgModel(CodeTable.NoComputerToInstall, "没有可以安装的机器，请先添加机器");
        }
        if (clusterID == 0) {
            List<ComputerAndComputerSaltType> computerAndComputerSaltTypeList = new ArrayList<ComputerAndComputerSaltType>();
            for (Computer computerByUserid : computerListByUserid) {
                for (Computer computer : computerList) {
                    if (computerByUserid.getComputerID().equals(computer.getComputerID())) {
                        ComputerAndComputerSaltType computerAndComputerSaltType = new ComputerAndComputerSaltType();
                        computerAndComputerSaltType.setComputer(computer);
                        computerAndComputerSaltType.setComputerSaltType("4");
                        computerAndComputerSaltTypeList.add(computerAndComputerSaltType);
                    }
                }
            }
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, computerAndComputerSaltTypeList);
        } else {
            List<ComputerAndComputerSaltType> computerAndComputerSaltTypeList = new ArrayList<ComputerAndComputerSaltType>();
            computerAndComputerSaltTypeList = findAllComputersInClusterIfc.getComputerAndComputerTypeList(clusterID);
            //仅仅用于判断这个agent所属域中是否有两台master则不用和userId挂钩
//            List<ComputerAndComputerSaltType> computerAndComputerSaltTypeListByUserid = new ArrayList<ComputerAndComputerSaltType>();
//            for(Computer computerByUserid:computerListByUserid){
//                for(ComputerAndComputerSaltType computerAndComputerSaltType:computerAndComputerSaltTypeList){
//                    if(computerAndComputerSaltType.getComputer().getComputerID().equals(computerByUserid.getComputerID())){
//                        computerAndComputerSaltTypeListByUserid.add(computerAndComputerSaltType);
//                    }
//                }
//            }
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, computerAndComputerSaltTypeList);
        }
    }


    //更改此cluster本身信息
    @RequestMapping(value = "/uptCluster", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public RespModel updataComputerByClusterID(@RequestBody Cluster cluster) throws Exception {
        clusterServiceIfc.updateSelective(cluster);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    //添加或删除cluster内部机器信息
    @RequestMapping(value = "/updateComputer2Cluster", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public RespModel updataComputerInCluster(@RequestBody Map request) throws Exception {
        Long clusterID = Long.parseLong(String.valueOf(request.get("clusterID")));
        List<ComputerAndComputerSaltType> deletecomputerList = JSONArray.parseArray(String.valueOf(request.get("delComputerList")), ComputerAndComputerSaltType.class);
        List<ComputerAndComputerSaltType> addcomputerList = JSONArray.parseArray(String.valueOf(request.get("addComputerList")), ComputerAndComputerSaltType.class);
        if (deletecomputerList.size() != 0) {
            updataComputerInClusterIfc.deleteComputerInCluster(clusterID, deletecomputerList);
        }
        if (addcomputerList.size() != 0) {
            updataComputerInClusterIfc.addComputerInCluster(clusterID, addcomputerList);
        }
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/addComputer2Cluster", method = RequestMethod.POST)
    @ResponseBody
//    public RespModel addComputer2Cluster(Long clusterID,String computers) throws Exception{
    public RespModel addComputer2Cluster(@RequestBody Map request) throws Exception {
        Long clusterID = Long.parseLong(String.valueOf(request.get("clusterID")));
        String computers = String.valueOf(request.get("computers"));
        if (computers == null) {
            return RespTools.getRespMsgModel(CodeTable.FAILED, "computers为空");
        }
        clusterServiceIfc.addComputer2Cluster(clusterID, computers);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    @RequestMapping(value = "/getComputer4InsAgentOrMaster", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getComputer4InsAgentOrMaster(Long userId) throws Exception {
        List<ComputerAndComputerSaltType> computerListByUserid = findAllComputersInClusterIfc.getComputerAndComputerTypeListByUserID(userId);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, computerListByUserid);
    }
}
