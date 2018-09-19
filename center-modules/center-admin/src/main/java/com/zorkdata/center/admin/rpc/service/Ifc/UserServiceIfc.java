package com.zorkdata.center.admin.rpc.service.Ifc;

import com.zorkdata.center.admin.entity.User;
import com.zorkdata.center.admin.vo.UserInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 用户服务层接口
 * @author: huziyue
 * @create: 2018/3/21 15:23
 */
public interface UserServiceIfc {
    /**
     * 通过用户名，密码验证用户
     * @param userName 用户名
     * @param password 密码
     * @return 用户信息对象
     */
    UserInfo validate(String userName, String password,String productCode)throws Exception;

    /**
     * 为系统添加一个用户
     * @param userID
     * @param productCode
     * @param user
     * @param groupID
     *
     * @return
     */
    Boolean insert(Long userID,String productCode,User user,Long groupID);

    /**
     * 更改用户数据
     * @param userID
     * @param productCode
     * @param user
     *
     * @return
     */
    Boolean updateSelective(Long userID,String productCode,User user);

    /**
     * 根据传入的id数据删除对应的用户
     * @param ids 用户id数组
     */
    Boolean deleteUserByIds(Long userID,String productCode,List<Long> ids);

    /**
     * 根据传入的用户组id查找相应的组内用户
     * @param groupID 用户组id
     * @return 组内所有用户
     */
    List<User> getUserByGroupId(Long groupID);

    /**
     * 通过用户名查询用户
     * @param userName 用户名
     * @param userID 用户ID
     * @return
     */
    User getUserByUserName(String userName,Long userID);

    /**
     * 获取所有用户
     * @return
     */
    List<User> getAllUser();

    /**
     * 根据产品获取所有角色
     * @param productCode
     * @return
     */
    List<User> getUserByProduct(String productCode);

    /**
     * 将角色分配给用户
     * @param userID
     * @param roleIds
     */
    Boolean editRoleToUser(Long userID, Map<String, List<Long>> roleIds,Long editUserID,String productCode);

    /**
     * 判断该用户是否还在其他系统内关联
     * @param userID
     * @param productCode
     * @return
     */
    Boolean inOtherProduct(Long userID, String productCode);
}
