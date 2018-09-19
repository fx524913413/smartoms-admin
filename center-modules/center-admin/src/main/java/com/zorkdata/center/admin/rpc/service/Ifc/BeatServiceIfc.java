package com.zorkdata.center.admin.rpc.service.Ifc;

import com.zorkdata.center.admin.vo.ComputerVo;
import com.zorkdata.center.common.core.RespModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 *
 * @author: huziyue
 * @create: 2018/6/08 14:40
 */
public interface BeatServiceIfc {
    /**
     *
     * @param typeName
     * @param agentName
     * @return
     */
    RespModel stopService(String typeName, String agentName);

    /**
     * 实现文件上传
     * @param typeName
     * @param fis
     * @param agentName
     * @return
     */
    RespModel ymlUpload(String typeName, MultipartFile fis, String agentName);
}
