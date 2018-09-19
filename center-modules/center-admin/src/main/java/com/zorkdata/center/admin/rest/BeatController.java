package com.zorkdata.center.admin.rest;

import com.alibaba.fastjson.JSON;
import com.zorkdata.center.admin.biz.ComputerBiz;
import com.zorkdata.center.admin.config.AgentVersionConfiguration;
import com.zorkdata.center.admin.config.FileConfiguration;
import com.zorkdata.center.admin.entity.Computer;
import com.zorkdata.center.admin.feign.BizPCService;
import com.zorkdata.center.admin.rpc.service.Ifc.BeatServiceIfc;
import com.zorkdata.center.admin.rpc.service.Impl.BkImpl;
import com.zorkdata.center.common.core.RespModel;
import com.zorkdata.center.common.util.HttpClientUtil;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/5/17 13:40
 */
@RestController
@RequestMapping("beatservice")
public class BeatController {
    enum os_type {
        Linux, Windows
    }
    @Autowired
    private BizPCService bizPCService;
    @Autowired
    private BeatServiceIfc beatService;
    @Autowired
    private AgentVersionConfiguration avc;
    @Autowired
    private FileConfiguration fileConfiguration;
    @Autowired
    private ComputerBiz computerBiz;
    @Autowired
    private BkImpl bk;

    @RequestMapping(value = "/stopservice", method = RequestMethod.POST)
    @ResponseBody
    public RespModel stopService(String typeName, String agentName,String version) {
        Boolean versionStar = version.startsWith("2");
        if (versionStar) {
            return bizPCService.stopService(typeName, agentName);
        } else {
            return beatService.stopService(typeName, agentName);
        }
    }

    @RequestMapping(value = "/sendfile", method = RequestMethod.POST)
    @ResponseBody
    public RespModel fileUpload(MultipartFile fis, String agentName,String version) {
        String typeName = "filebeat";
        Boolean versionStar = version.startsWith("2");
        if (versionStar) {
            Boolean result2 = false;
            // 保存文件信息到磁盘
            File targetFile = new File(fileConfiguration.getFilepath(), agentName+"filebeats.yml");
            Boolean isTransferTo = transferTo(fis, targetFile);
            if(!isTransferTo){
                System.out.println("false");
            }else{
                System.out.println("true");
            }
            String filePath = fileConfiguration.getFilepath()+agentName+"filebeats.yml";
            return bizPCService.fileUpload(filePath, agentName);
        } else {
            return beatService.ymlUpload(typeName, fis, agentName);
        }
    }

    @RequestMapping(value = "/sendfilemock", method = RequestMethod.GET)
    @ResponseBody
    public RespModel fileUploadmock(String agentName,String version) throws IOException {
        InputStream fis = new FileInputStream(new File(fileConfiguration.getTestfile()));
        String typeName = "filebeat";
        Boolean versionStar = version.startsWith("2");
        if (versionStar) {
            Boolean result2 = false;
            String filePath = fileConfiguration.getFilepath()+agentName+"filebeats.yml";
            result2 = writeToFile(fis, fileConfiguration.getFilepath()+agentName+"filebeats.yml");// 保存文件信息到磁盘
            return bizPCService.fileUpload(filePath, agentName);
        } else {
//            return beatService.ymlUpload(typeName, fis, agentName);
            return null;
        }
    }

    /**
     * 将添加一条数据同步到蓝鲸用接口
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/sendfilemock2", method = RequestMethod.GET)
    @ResponseBody
    public RespModel fileUploadmock2(String bk_token,String ip,String hostname,String computertype,String url) throws IOException {
//        List<Computer> computerList = computerBiz.getComputerList(10L);
        List<Computer> computerList = new ArrayList<>();
        Computer computer2 = new Computer();
        computer2.setIp(ip);
        computer2.setHostName(hostname);
        computer2.setComputerType(computertype);
        computerList.add(computer2);
        Map<String, String> map = new HashMap<>();
        map.put("Cookie", "bk_token=" + bk_token);
        Map<String, Object> params = new HashMap<>();
        params.put("bk_app_code", "zork-nodeman");
        params.put("bk_app_secret", "d749a5e9-0f4d-449c-b793-9080dab9b090");
//        params.put("bk_token", bkToken.getBk_token());
        params.put("bk_supplier_id", 0);
        Map<String, Map<String, Object>> host_info = new HashMap<>();
        int i = 0;
        for (Computer computer : computerList) {
            if (computer.getIp().indexOf(",") > 0) {
                for (String ip2 : computer.getIp().split(",")) {
                    Map<String, Object> computermap = new HashMap<>();
                    computermap.put("bk_host_innerip", computer.getIp());
                    computermap.put("bk_host_name",computer.getHostName());
                    computermap.put("bk_cloud_id", 0);
                    if(computer.getComputerType().startsWith("w")){
                        computermap.put("bk_os_type",os_type.Windows);
                    }else{
                        computermap.put("bk_os_type",os_type.Linux);
                    }
                    host_info.put("" + i, computermap);
                    i++;
                }
            } else {
                Map<String, Object> computermap = new HashMap<>();
                computermap.put("bk_host_innerip", computer.getIp());
                computermap.put("bk_host_name",computer.getHostName());
                computermap.put("bk_cloud_id", 0);
                if(computer.getComputerType().startsWith("w")){
                    computermap.put("bk_os_type",os_type.Windows);
                }else{
                    computermap.put("bk_os_type",os_type.Linux);
                }
                host_info.put("" + i, computermap);
            }
            if (i % 100 == 0) {
                params.put("host_info", host_info);
                String paramsTemp = JSON.toJSONString(params);
//                String apiGateWay = "http://192.168.1.93:8083/api/v3/hosts/add";
                String apiGateWay = url;
                String res = "";
                try {
                    res = HttpClientUtil.sendHttpPost(apiGateWay, paramsTemp, map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(res);
                host_info.clear();
            }
            i++;
        }
        params.put("host_info", host_info);
        String paramsTemp = JSON.toJSONString(params);
        String apiGateWay = url;
//        String a = "http://cmdb.blueking.com/api/c/compapi/v2/cc/add_host_to_resource/";
        String res = "";
        try {
            res = HttpClientUtil.sendHttpPost(apiGateWay, paramsTemp, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(res);
        return null;
    }

    /**
     * 将中台数据同步到蓝鲸用接口
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/sendfilemock3", method = RequestMethod.GET)
    @ResponseBody
    public RespModel fileUploadmock3(String bk_token,String url) throws IOException {
        List<Computer> computerList = computerBiz.getComputerList(10L);
        Map<String, String> map = new HashMap<>();
        map.put("Cookie", "bk_token=" + bk_token);
        Map<String, Object> params = new HashMap<>();
        params.put("bk_app_code", "zork-nodeman");
        params.put("bk_app_secret", "d749a5e9-0f4d-449c-b793-9080dab9b090");
//        params.put("bk_token", bkToken.getBk_token());
        params.put("bk_supplier_id", 0);
        Map<String, Map<String, Object>> host_info = new HashMap<>();
        int i = 0;
        for (Computer computer : computerList) {
            if (computer.getIp()!=null&&computer.getIp().indexOf(",") > 0) {
                for (String ip2 : computer.getIp().split(",")) {
                    Map<String, Object> computermap = new HashMap<>();
                    computermap.put("bk_host_innerip", ip2);
                    computermap.put("bk_host_name", computer.getHostName());
                    computermap.put("bk_cloud_id", 0);
                    if (computer.getComputerType().startsWith("w")) {
                        computermap.put("bk_os_type", os_type.Windows);
                    } else {
                        computermap.put("bk_os_type", os_type.Linux);
                    }
                    host_info.put("" + i, computermap);
                }
            }
            else if(computer.getIp()!=null){
                Map<String, Object> computermap = new HashMap<>();
                computermap.put("bk_host_innerip", computer.getIp());
                computermap.put("bk_host_name", computer.getHostName());
                computermap.put("bk_cloud_id", 0);
                if (computer.getComputerType() != null && computer.getComputerType().startsWith("w")) {
                    computermap.put("bk_os_type", os_type.Windows);
                } else if (computer.getComputerType() != null && computer.getComputerType().startsWith("l")) {
                    computermap.put("bk_os_type", os_type.Linux);
                }
                host_info.put("" + i, computermap);
            }
            if (i % 100 == 0) {
                params.put("host_info", host_info);
                String paramsTemp = JSON.toJSONString(params);
//                String apiGateWay = "http://192.168.1.93:8083/api/v3/hosts/add";
                String apiGateWay = url;
                String res = "";
                try {
                    res = HttpClientUtil.sendHttpPost(apiGateWay, paramsTemp, map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(res);
                host_info.clear();
            }
            i++;
        }
        params.put("host_info", host_info);
        String paramsTemp = JSON.toJSONString(params);
        String apiGateWay = url;
//        String a = "http://cmdb.blueking.com/api/c/compapi/v2/cc/add_host_to_resource/";
        String res = "";
        try {
            res = HttpClientUtil.sendHttpPost(apiGateWay, paramsTemp, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(res);
        return null;
    }

    @RequestMapping(value = "/sendfilemock4", method = RequestMethod.GET)
    @ResponseBody
    public RespModel fileUploadmock4(String bk_token) throws IOException {
        List<Computer> computerList = new ArrayList<>();
        for(int i=0;i<100;i++){
            Computer computer = new Computer();
            computer.setComputerType("linux");
            computer.setHostName(i+"");
            computer.setIp("1.1.1."+i);
            computerList.add(computer);
        }
        Map<String, String> map = new HashMap<>();
        map.put("Cookie", "bk_token=" + bk_token);
        Map<String, Object> params = new HashMap<>();
        params.put("bk_app_code", "zork-nodeman");
        params.put("bk_app_secret", "d749a5e9-0f4d-449c-b793-9080dab9b090");
//        params.put("bk_token", bkToken.getBk_token());
        params.put("bk_supplier_id", 0);
        Map<String, Map<String, Object>> host_info = new HashMap<>();
        int i = 0;
        for (Computer computer : computerList) {
            Map<String, Object> computermap = new HashMap<>();
            computermap.put("bk_host_innerip", computer.getIp());
            computermap.put("bk_host_name",computer.getHostName());
            computermap.put("bk_cloud_id", 0);
            if(computer.getComputerType()!=null&&computer.getComputerType().startsWith("w")){
                computermap.put("bk_os_type",os_type.Windows);
            }else if(computer.getComputerType()!=null&&computer.getComputerType().startsWith("l")){
                computermap.put("bk_os_type",os_type.Linux);
            }
            host_info.put("" + i, computermap);

            if (i % 100 == 0) {
                params.put("host_info", host_info);
                String paramsTemp = JSON.toJSONString(params);
//                String apiGateWay = "http://192.168.1.93:8083/api/v3/hosts/add";
                String apiGateWay = "http://192.168.1.93:8083/api/v3/hosts/add";
                String res = "";
                try {
                    res = HttpClientUtil.sendHttpPost(apiGateWay, paramsTemp, map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(res);
                host_info.clear();
            }
            i++;
        }
        params.put("host_info", host_info);
        String paramsTemp = JSON.toJSONString(params);
        String apiGateWay = "http://192.168.1.93:8083/api/v3/hosts/add";
//        String a = "http://cmdb.blueking.com/api/c/compapi/v2/cc/add_host_to_resource/";
        String res = "";
        try {
            res = HttpClientUtil.sendHttpPost(apiGateWay, paramsTemp, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(res);
        return null;
    }

    @RequestMapping(value = "/sendexec", method = RequestMethod.POST)
    @ResponseBody
    public RespModel execUpload(MultipartFile fis, String agentName,String version) {
        String typeName = "execbeat";
        Boolean versionStar = version.startsWith("2");
        if (versionStar) {
            Boolean result2 = false;
            // 保存文件信息到磁盘
            File targetFile = new File(fileConfiguration.getFilepath() , agentName+"execbeat.yml");
            Boolean isTransferTo = transferTo(fis, targetFile);
            if(!isTransferTo){
                System.out.println("false");
            }else{
                System.out.println("true");
            }
            String filePath = fileConfiguration.getFilepath()+agentName+"execbeat.yml";
            return bizPCService.execUpload(filePath, agentName);
        } else {
            return beatService.ymlUpload(typeName, fis, agentName);
        }
    }

    @RequestMapping(value = "/sendmetric", method = RequestMethod.POST)
    @ResponseBody
    public RespModel metricUpload(MultipartFile fis, String agentName, String version) {
        if(fis==null){
            System.out.println("fis is null");
        }
        String typeName = "metricbeat";
        Boolean versionStar = version.startsWith("2");
        if (versionStar) {
            Boolean result2 = false;
            // 保存文件信息到磁盘
            File targetFile = new File(fileConfiguration.getFilepath() , agentName+"metricbeat.yml");
            Boolean isTransferTo = transferTo(fis, targetFile);
            if(!isTransferTo){
                System.out.println("false");
            }else{
                System.out.println("true");
            }
//            result2 = writeToFile(fis, fileConfiguration.getFilepath()+"\\tmpfiles\\"+agentName+"filebeats.yml");
            String filePath = fileConfiguration.getFilepath()+agentName+"metricbeat.yml";

            return bizPCService.metricUpload(filePath, agentName);
        } else {
            return beatService.ymlUpload(typeName, fis, agentName);
        }
    }

    public static Boolean writeToFile(InputStream is, String uploadedFileLocation) {
        // TODO Auto-generated method stub
        File file = new File(uploadedFileLocation);
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int read = 0;
            byte buffer[] = new byte[1024];
            while ((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(os!=null){
                    os.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(String.format("Path：%s 大小%s字节", file.toPath(), file.length()));
        if (file.length() < 1) {
            file.delete();
            return false;
        }
        return true;
    }

    public Boolean transferTo(MultipartFile sourFile, File tarFile) {

        try {
            sourFile.transferTo(tarFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
