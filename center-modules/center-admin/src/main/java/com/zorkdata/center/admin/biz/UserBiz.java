package com.zorkdata.center.admin.biz;

import com.zorkdata.center.admin.entity.User;
import com.zorkdata.center.admin.mapper.UserMapper;
import com.zorkdata.center.admin.util.MyException;
import com.zorkdata.center.common.biz.BaseBiz;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author huziyue
 * @description: ${todo}
 * @create 2018/3/20 9:56
 */
@Service
@Transactional(rollbackFor = Exception.class,noRollbackFor= MyException.class)
public class UserBiz extends BaseBiz<UserMapper, User> {
    @Override
    public void insertSelective(User entity) {
        String password = new BCryptPasswordEncoder(12).encode(entity.getPassword());
        entity.setPassword(password);
        mapper.insertSelectiveGetId(entity);
    }

    public User getUserByUserName(String userName,Long userID) {
        return mapper.getUserByUserName(userName,userID);
    }

    public List<User> getUserByGroupId(Long groupID) {
        return mapper.getUserByGroupId(groupID);
    }

    public void deleteByIds(Set<Long> ids) {
        mapper.deleteByIds(ids);
    }

    public void updateByUserID(User user) {
        mapper.updateByUserID(user);
    }

    public List<User> getUserByIds(List<Long> userIds) {
        return mapper.getUserByIds(userIds);
    }

    public List<User> getAllUser() {
        return mapper.getAllUser();
    }
}
