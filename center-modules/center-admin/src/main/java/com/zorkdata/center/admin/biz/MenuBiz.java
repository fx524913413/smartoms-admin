package com.zorkdata.center.admin.biz;

import com.zorkdata.center.admin.entity.Menu;
import com.zorkdata.center.admin.mapper.MenuMapper;
import com.zorkdata.center.admin.vo.MenuVo;
import com.zorkdata.center.common.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/27 11:43
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MenuBiz extends BaseBiz<MenuMapper, Menu> {
    public List<Menu> getMenuByResourceID(List<Long> rescoureIds) {
        return mapper.getMenuByResourceID(rescoureIds);
    }

    public void updateByMenuId(Menu menu) {
        mapper.updateByMenuId(menu);
    }

    public void deleteMenuByIds(Long[] ids) {
        mapper.deleteMenuByIds(ids);
    }

    public List<MenuVo> getAllMenu() {
        return mapper.getAllMenu();
    }

    public List<Menu> getMenuByProductID(Integer productID) {
        return mapper.getMenuByProductID(productID);
    }
}
