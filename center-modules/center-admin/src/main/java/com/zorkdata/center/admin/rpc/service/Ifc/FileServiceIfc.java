package com.zorkdata.center.admin.rpc.service.Ifc;

import com.zorkdata.center.admin.util.ResultInfo;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/27 17:00
 */
public interface FileServiceIfc {
    public void downloadFile(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam String filename) throws Exception;

    public Map<Long, Map<String, List<String>>> asyncSendFile(Map<String, Object> map);

    public Map<String, String> syncSendFile(Map<String, Object> map);

    public Map<String, String> syncSendFile4AnyLocation(Map<String, Object> map);

    public ResultInfo fastFileUpload(MultipartFile file, String isOverride, String name, String creator);

    public Boolean transferTo(MultipartFile sourFile, File tarFile);




}
