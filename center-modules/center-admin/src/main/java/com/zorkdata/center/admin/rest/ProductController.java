package com.zorkdata.center.admin.rest;


import com.alibaba.fastjson.JSON;
import com.zorkdata.center.admin.entity.Product;
import com.zorkdata.center.admin.entity.Role;
import com.zorkdata.center.admin.rpc.service.Ifc.ProductServiceIfc;
import com.zorkdata.center.admin.vo.ProductInfo;
import com.zorkdata.center.admin.vo.ProductRoleTree;
import com.zorkdata.center.admin.vo.ProductVo;
import com.zorkdata.center.common.constant.CodeTable;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.core.RespTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: huziyue
 * @create: 2018/3/26 10:50
 */
@RestController
@RequestMapping("product")
public class ProductController {
    @Autowired
    private ProductServiceIfc productService;

    /**
     * 添加一个产品
     *
     * @param product
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public RespModel addProduct(@RequestBody Product product) {
        product.setCreateTime(new Date());
        productService.insert(product);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    /**
     * 编辑一个产品
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public RespModel updateProduct(@RequestBody Map request) {
        Long userID = Long.parseLong(String.valueOf(request.get("userID")));
        String productCode = String.valueOf(request.get("productCode"));
        Product product = JSON.parseObject(String.valueOf(request.get("product")), Product.class);
        Boolean flag = productService.updateByProductId(userID, productCode, product);
        if (flag) {
            return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
        }
        return RespTools.getRespMsgModel(CodeTable.NOT_ALLOW, null);
    }

    /**
     * 删除一个产品
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public RespModel deleteProduct(@RequestBody Map request) {
        Integer productID = Integer.parseInt(String.valueOf(request.get("productID")));
        productService.deleteByIds(productID);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, null);
    }

    /**
     * 获取所有产品
     *
     * @return
     */
    @RequestMapping(value = "/getProducts", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getProducts() {
        List<ProductVo> products = productService.getProducts();
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, products);
    }


    @RequestMapping(value = "/getBkurl", method = RequestMethod.GET)
    @ResponseBody
    public RespModel getBkurl(@RequestParam String productCode) {
        String bkurl=productService.getBkurl(productCode);
        return RespTools.getRespMsgModel(CodeTable.SUCCESS, bkurl);
    }
//    @RequestMapping(value = "/getProductRoleTree",method = RequestMethod.GET)
//    @ResponseBody
//    public RespModel getProductRoleTree(){
//        List<ProductInfo> productInfos=productService.getProductInfos();
//        ArrayList<ProductRoleTree> productRoleTrees = new ArrayList<>();
//        ProductRoleTree productRoleTree = new ProductRoleTree();
//        /**最高节点id=-1 最高节点父id为# 最高节点名字为所有产品 */
//        productRoleTree.setId(-1);
//        productRoleTree.setName("所有产品");
//        productRoleTree.setParentID("#");
//        productRoleTree.setChildren(productInfos);
//        productRoleTrees.add(productRoleTree);
//        return RespTools.getRespMsgModel(CodeTable.SUCCESS,productRoleTrees);
//    }
}
