package com.zorkdata.center.auth.mapper;

import com.zorkdata.center.auth.entity.Client;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/29 16:51
 */
public interface ClientMapper extends Mapper<Client> {
    List<String> selectAllowedClient(String serviceId);

    List<Client> selectAuthorityServiceInfo(int clientId);
}
