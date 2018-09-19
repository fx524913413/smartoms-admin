package com.zorkdata.center.admin.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zorkdata.center.admin.entity.Computer;
import com.zorkdata.center.admin.entity.Permission;
import com.zorkdata.center.admin.entity.Product;
import com.zorkdata.center.admin.entity.SuperEntity;
import com.zorkdata.center.admin.rpc.service.Ifc.PermissionServiceIfc;
import com.zorkdata.center.admin.rpc.service.Ifc.ProductServiceIfc;
import com.zorkdata.center.admin.rpc.service.Ifc.RoleServiceIfc;
import com.zorkdata.center.admin.util.PermissionCode;
import com.zorkdata.center.admin.vo.ComputerVo;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author: huziyue
 * @create: 2018/3/28 13:28
 */
@RequestMapping("permission")
@RestController
public class PermissionController {

    @Autowired
    private PermissionServiceIfc permissionService;

    @Autowired
    private ProductServiceIfc productService;

    @Autowired
    private RoleServiceIfc roleService;

    /**
     * 获取资源（中台数据返回具体数据，不属于中台数据则返回id）
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getResource", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getResource(@RequestParam Map request) {
        //如果用户id为空，说明是查询某个角色或用户组的resource
        SuperEntity resources = null;
        Long userID = Long.parseLong(String.valueOf(request.get("userID")));
        String productCode = String.valueOf(request.get("productCode"));
        resources = permissionService.getResourceByUserID(userID, productCode);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, resources);
    }

    /**
     * 获取单个角色权限（包含中台数据）
     *
     * @param roleID
     * @return
     */
    @RequestMapping(value = "/getResourceByRole", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getResourceByRole(@RequestParam Long roleID) {
        //如果用户id为空，说明是查询某个角色或用户组的resource
        Map<String, Set<Long>> resources = permissionService.getPermissionByRoleID(roleID);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, resources);
    }

    /**
     * 给某个角色、用户、用户组赋权
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/givePermission", method = RequestMethod.POST)
    @ResponseBody
    public RespModel givePermission(@RequestBody Map request) {
        Map<String, Map<String, List<Long>>> permissionMap =
                JSON.parseObject(String.valueOf(request.get("permission")), new TypeReference<Map<String, Map<String, List<Long>>>>() {
                });
        String sourceType = String.valueOf(request.get("sourceType"));
        Long sourceID = Long.parseLong(String.valueOf(request.get("sourceId")));
        String productCode = String.valueOf(request.get("productCode"));
        Long userID = Long.parseLong(String.valueOf(request.get("userID")));
        Boolean flag = permissionService.givePermission(permissionMap, sourceType, sourceID, userID, productCode);
        if (flag) {
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
        }
        return RespTools.getRespMsgModel(CodeTable.NOT_ALLOW, null);
    }

    /**
     * 获取当前用户拥有可赋权的权限
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/editResource", method = RequestMethod.GET)
    @ResponseBody
    public RespModel editResource(@RequestParam Map request) {
        SuperEntity resources;
        Long userID = Long.parseLong(String.valueOf(request.get("userID")));
        String productCode = String.valueOf(request.get("productCode"));
        resources = permissionService.getPermissionByUserID(productCode, userID);
        if (resources != null && resources.size() != 0) {
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, resources);
        } else {
            return RespTools.getRespMsgModel(CodeTable.NOT_ALLOW, null);
        }
    }

    /**
     * 获取权限（不属于中台的权限）
     * @param request
     * @return
     */
//    @RequestMapping(value = "/getPermission",method = RequestMethod.GET)
//    @ResponseBody
//    public RespModel getPermission(@RequestParam Map request){
//        Long userID = Long.parseLong(String.valueOf(request.get("userID")));
//        Integer productID = Integer.parseInt(String.valueOf(request.get("productID")));
//        Product product = productService.getProductByOwnerID(userID);
//        Map<String,Set<Long>> permissions;
//        if(product!=null&&product.getProductID()==productID){
//            //说明该用户是产品负责人，则自动获取该产品下所有权限
//            permissions=permissionService.getPermissionByOwer(userID);
//        }else {
//            //说明该用户不是产品负责人，则根据角色获取权限
//            permissions=permissionService.getPermissionByUserId(userID,productID);
//        }
//        return RespTools.getRespMsgModel(CodeTable.SUCCESS,permissions);
//    }

    /**
     * 获取用户的所有主机
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getComputerByUserID", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getComputerByUserID(@RequestParam Map request) {
        Long userID = Long.parseLong(String.valueOf(request.get("userID")));
        List<ComputerVo> computerVos = permissionService.getComputerVoByUserId(userID);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, computerVos);
    }
}
