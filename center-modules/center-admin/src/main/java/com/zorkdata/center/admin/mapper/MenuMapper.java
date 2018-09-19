package com.zorkdata.center.admin.mapper;

import com.zorkdata.center.admin.entity.Menu;
import com.zorkdata.center.admin.vo.MenuVo;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/27 11:42
 */
@Repository
public interface MenuMapper extends Mapper<Menu> {
    /**
     * 通过权限表的资源id查询菜单
     *
     * @param rescoureIds
     * @return
     */
    List<Menu> getMenuByResourceID(List<Long> rescoureIds);

    /**
     * 编辑菜单栏
     *
     * @param menu
     */
    void updateByMenuId(Menu menu);

    /**
     * 通过id批量删除菜单
     *
     * @param ids
     */
    void deleteMenuByIds(Long[] ids);

    /**
     * 获取所有菜单
     *
     * @return
     */
    List<MenuVo> getAllMenu();

    /**
     * 根据产品获取菜单
     *
     * @param productID
     * @return
     */
    List<Menu> getMenuByProductID(Integer productID);
}
