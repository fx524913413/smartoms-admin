package com.zorkdata.center.admin.rpc.service.Ifc;

import com.zorkdata.center.admin.entity.Product;
import com.zorkdata.center.admin.entity.Role;
import com.zorkdata.center.admin.vo.ProductInfo;
import com.zorkdata.center.admin.vo.ProductVo;

import java.util.List;

/**
 * 产品服务层接口
 *
 * @author: huziyue
 * @create: 2018/3/26 10:52
 */
public interface ProductServiceIfc {

    /**
     * 添加一个产品
     *
     * @param product 产品数据
     */
    void insert(Product product);

    /**
     * 编辑产品数据
     *
     * @param product 产品数据
     */
    Boolean updateByProductId(Long userID, String productCode, Product product);

    /**
     * 根据产品id删除产品
     *
     * @param
     */
    void deleteByIds(Integer id);

    /**
     * 获取所有产品信息
     *
     * @return 所有产品信息的集合
     */
    List<ProductVo> getProducts();

//    /**
//     * 获取所有的产品以及其系统内角色集合
//     * @return
//     */
//    List<ProductInfo> getProductInfos();

    /**
     * 通过ownerid查询产品
     *
     * @param userID
     * @return
     */
    List<Product> getProductByOwnerID(Long userID);

    /**
     * 通过超级管理员id查询产品
     *
     * @param userID
     * @return
     */
    Product getProductBySuperAdminID(Long userID);

    /**
     * 获取所有owerid
     *
     * @return
     */
    List<Long> getOwerIds();

    /**
     * 获取配置平台接口
     * @param productCode
     * @return
     */
    String getBkurl(String productCode);
}
