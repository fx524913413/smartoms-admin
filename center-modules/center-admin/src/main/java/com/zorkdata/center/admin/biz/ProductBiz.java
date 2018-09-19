package com.zorkdata.center.admin.biz;

import com.zorkdata.center.admin.entity.Product;
import com.zorkdata.center.admin.mapper.ProductMapper;
import com.zorkdata.center.admin.vo.ProductInfo;
import com.zorkdata.center.admin.vo.ProductVo;
import com.zorkdata.center.common.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author huziyue
 * @description:
 * @create 2018/3/26 11:24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductBiz extends BaseBiz<ProductMapper, Product> {

    public void updateByProductId(Product product) {
        mapper.updateByProductId(product);
    }

    public void deleteById(Integer id) {
        mapper.deleteById(id);
    }

    public List<Product> getProductByOwnerID(Long userID) {
        return mapper.getProductByOwnerID(userID);
    }

    public Product getProductBySuperAdminID(Long userID) {
        return mapper.getProductBySuperAdminID(userID);
    }

    public List<ProductVo> selectAllProduct() {
        return mapper.selectAllProduct();
    }

    public List<Long> getOwerIds() {
        return mapper.getOwerIds();
    }

    public Integer getProductIDByProductCode(String productCode) {
        return mapper.getProductIDByProductCode(productCode);
    }

    public String getBkurl(String productCode) {
        return mapper.getBkurl(productCode);
    }
}
