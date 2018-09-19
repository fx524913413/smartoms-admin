package com.zorkdata.center.admin.mapper;

import com.zorkdata.center.admin.entity.Product;
import com.zorkdata.center.admin.vo.ProductInfo;
import com.zorkdata.center.admin.vo.ProductVo;
import feign.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @description: ${todo}
 * @author: huziyue
 * @create: 2018/3/20 16:33
 */
@Repository
public interface ProductMapper extends Mapper<Product> {

    /**
     * 编辑产品数据
     *
     * @param product 产品数据
     */
    void updateByProductId(Product product);

    /**
     * 根据产品id删除产品
     *
     * @param
     */
    void deleteById(Integer id);

    /**
     * 通过产品负责人id查询产品
     *
     * @param userID
     * @return
     */
    List<Product> getProductByOwnerID(@Param("ower") Long userID);

    /**
     * 通过超级管理员id查询产品
     *
     * @param userID
     * @return
     */
    Product getProductBySuperAdminID(@Param("ower") Long userID);

    /**
     * 获取所有产品及其负责人
     *
     * @return
     */
    List<ProductVo> selectAllProduct();

    /**
     * 获取所有owerid
     *
     * @return
     */
    List<Long> getOwerIds();

    /**
     * 根据产品code获得产品id
     *
     * @param productCode
     * @return
     */
    Integer getProductIDByProductCode(String productCode);

    /**
     * 获取配置平台url
     * @param productCode
     * @return
     */
    String getBkurl(String productCode);
}
