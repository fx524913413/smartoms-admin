package com.zorkdata.datasynchro.mapper;

import com.zorkdata.datasynchro.entity.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface UserMapper extends Mapper<User> {
    /**
     * 获取所有用户
     * @return
     */
    List<User> getAllUser();
}
