package com.zorkdata.center.admin.rpc.service.Impl;

import com.zorkdata.center.admin.biz.ProductBiz;
import com.zorkdata.center.admin.biz.RoleBiz;
import com.zorkdata.center.admin.biz.UserRoleBiz;
import com.zorkdata.center.admin.entity.Product;
import com.zorkdata.center.admin.entity.Role;
import com.zorkdata.center.admin.rpc.service.Ifc.ProductServiceIfc;
import com.zorkdata.center.admin.vo.ProductInfo;
import com.zorkdata.center.admin.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProductServiceImpl implements ProductServiceIfc {

    @Autowired
    private ProductBiz productBiz;

    @Autowired
    private RoleBiz roleBiz;

    @Autowired
    private UserRoleBiz userRoleBiz;

    @Override
    public void insert(Product product) {
        productBiz.insertSelective(product);
    }

    @Override
    public Boolean updateByProductId(Long userID, String productCode, Product product) {
        List<Product> products = productBiz.getProductByOwnerID(userID);
        if(products!=null&&products.size()>0){
            for (Product product1:products) {
                if (product1.getCode().equals(productCode)) {
                    productBiz.updateByProductId(product);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void deleteByIds(Integer id) {
        List<Long> roleIds = roleBiz.getRoleIDByProductID(id);
        if (roleIds != null && roleIds.size() != 0) {
            userRoleBiz.deleteByRoleIds(roleIds);
            roleBiz.deleteByIds(roleIds);
        }
        productBiz.deleteById(id);
    }

    @Override
    public List<ProductVo> getProducts() {
        return productBiz.selectAllProduct();
    }

//    @Override
//    public List<ProductInfo> getProductInfos() {
//        List<ProductInfo> productInfos = productBiz.getProductInfos();
//        for (ProductInfo productInfo:productInfos) {
//            productInfo.setParentID(-1);
//            productInfo.setChildren(roleBiz.getRoleInfoByProductID(productInfo.getId()));
//        }
//        return productInfos;
//    }

    @Override
    public List<Product> getProductByOwnerID(Long userID) {
        return productBiz.getProductByOwnerID(userID);
    }

    @Override
    public Product getProductBySuperAdminID(Long userID) {
        return productBiz.getProductBySuperAdminID(userID);
    }

    @Override
    public List<Long> getOwerIds() {
        return productBiz.getOwerIds();
    }

    @Override
    public String getBkurl(String productCode) {
        return productBiz.getBkurl(productCode);
    }
}
