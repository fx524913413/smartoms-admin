package com.zorkdata.center.admin.rpc.service.Impl;

import com.zorkdata.center.admin.biz.*;
import com.zorkdata.center.admin.config.FileConfiguration;
import com.zorkdata.center.admin.entity.Computer;
import com.zorkdata.center.admin.entity.Product;
import com.zorkdata.center.admin.rpc.service.Ifc.ComputerServiceIfc;
import com.zorkdata.center.admin.vo.ComputerInfo;
import com.zorkdata.center.admin.vo.ComputerTagVo;
import com.zorkdata.center.admin.util.PermissionCode;
import com.zorkdata.center.admin.util.PermissionUtil;
import com.zorkdata.center.admin.vo.ComputersSortByProductCode;
import com.zorkdata.center.admin.vo.WorkerNode;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.zorkdata.center.common.util.ReadExcel.getCellFormatValue;
import static com.zorkdata.center.common.util.ReadExcel.readExcel;

@Service
public class ComputerServiceImpl implements ComputerServiceIfc {
    @Autowired
    private ComputerBiz computerBiz;
    @Autowired
    private FileConfiguration fileConfiguration;
    @Autowired
    private MasterBiz masterBiz;
    @Autowired
    private AgentBiz agentBiz;
    @Autowired
    private PermissionBiz permissionBiz;
    @Autowired
    private ProductBiz productBiz;
    @Autowired
    private UserRoleBiz userRoleBiz;
    @Autowired
    private TagBiz tagBiz;

    @Override
    public ComputerInfo findcomputerinfo(Long computerID) {
        ComputerInfo info = new ComputerInfo();
        Computer computer = computerBiz.getComputerByComputerId(computerID);
        BeanUtils.copyProperties(computer, info);
        return info;
    }

    @Override
    public Computer getComputerByAgentID(Long agentID) {
        return computerBiz.getComputerByAgentID(agentID);
    }

    @Override
    public List<WorkerNode> getComputerAgentInfo() {
        return computerBiz.getComputerAgentInfo();
    }


    @Override
    public boolean insertComputerEntity(Computer computer) {
        boolean flag = false;
        computer.setLastModifyTime(new Date());
        if (computerBiz.getComputerByComputerIP(computer.getIp()) == null) {
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean updateComputerEntity(Computer computer) {
        boolean flag = false;
        computer.setLastModifyTime(new Date());
        Computer computer1 = computerBiz.getComputerByComputerIP(computer.getIp());
        if (computer1 == null || computer1.getComputerID().equals(computer.getComputerID())) {
            flag = true;
            computerBiz.updateComputer(computer);
        }
        return flag;
    }

    @Override
    public void deleteComputerEntity(Long computerid) {
        Set<Long> computerIDset = new HashSet<>();
        computerIDset.add(computerid);
        computerBiz.deleteComputer(computerid);
        masterBiz.deleteMasterByComputerID(computerid);
        agentBiz.deleteAgentByComputerID(computerid);
        permissionBiz.deleteByResourceIds("computer", computerIDset);
    }

    @Override
    public List<Computer> getComputerList(Long userId) {
        List<Computer> computerList = computerBiz.getComputerList(userId);
        if (computerList == null) {
            return null;
        }
        for (Computer computer : computerList) {
            if (computer == null) {
                System.out.println("  ");
            }
        }
        return computerList;
    }

    @Override
    public void uploadExcelComputerinfo(MultipartFile myfile) {
        if (myfile.isEmpty()) {
            System.out.println("文件未上传");
        } else {
            System.out.println("文件长度: " + myfile.getSize());
            System.out.println("文件类型: " + myfile.getContentType());
            System.out.println("文件名称: " + myfile.getName());
            System.out.println("文件原名: " + myfile.getOriginalFilename());
            System.out.println("========================================");
            //如果用的是Tomcat服务器，则文件会上传到  {服务发布位置}\\WEB-INF\\upload\\文件夹中
            String realPath = fileConfiguration.getFilepath() + File.separator;
            //这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉，我是看它的源码才知道的
            try {
                FileUtils.copyInputStreamToFile(myfile.getInputStream(), new File(realPath, myfile.getOriginalFilename()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Workbook wb = null;
        Sheet sheet = null;
        Row row = null;
        List<List<String>> list = null;
        String cellData = null;
        String columns[] = {"type", "hostname", "ip", "mac", "username", "password"};
        wb = readExcel(fileConfiguration.getFilepath() + File.separator + myfile.getOriginalFilename());
        if (wb != null) {
            //用来存放表中数据
            list = new ArrayList<List<String>>();
            //获取第一个sheet
            sheet = wb.getSheetAt(0);
            //获取最大行数
            int rownum = sheet.getPhysicalNumberOfRows();
            //获取第一行
            row = sheet.getRow(0);
            //获取最大列数
            int colnum = row.getPhysicalNumberOfCells();
            for (int i = 1; i < rownum; i++) {
                List<String> computer = new ArrayList<>();
                row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < colnum; j++) {
                        cellData = (String) getCellFormatValue(row.getCell(j));
                        computer.add(cellData);
                    }
                } else {
                    break;
                }
                list.add(computer);
            }
        }
        List<Computer> computerList = new ArrayList<>();
        for (List<String> computerinlist : list) {
            Computer computer = new Computer();
            computer.setComputerType(computerinlist.get(0));
            computer.setHostName(computerinlist.get(1));
            computer.setIp(computerinlist.get(2));
            computer.setMac(computerinlist.get(3));
            computer.setUserName(computerinlist.get(4));
            computer.setPassword(computerinlist.get(5));
            computer.setLastModifyTime(new Date());
            computerList.add(computer);
        }
        for (Computer computer : computerList) {
            computerBiz.insertComputer(computer);
        }
    }

    @Override
    public void deleteComputers(Set<Long> computerIDlist) {
        computerBiz.deleteByIds(computerIDlist);
        agentBiz.deleteByComputerIds(computerIDlist);
        masterBiz.deleteByComputerIds(computerIDlist);
        permissionBiz.deleteByResourceIds("computer", computerIDlist);
    }

    @Override
    public List<Computer> findAllComputer() {
        return computerBiz.selectListAll();
    }

    @Override
    public List<ComputerTagVo> findComputerAndTag(Long userID) {
        List<Product> products = productBiz.getProductByOwnerID(userID);
        if (products != null&&products.size()>0) {
            //如果存在产品说明是负责人 则直接获取所有主机
            return computerBiz.getComputerAndTag(null);
        } else {
            //说明不是产品负责人
            Set<Long> userIds = new HashSet<>();
            userIds.add(userID);
            Set<Long> computerIds = new HashSet<>();
            Set<Long> tagIds = new HashSet<>();
            Set<Long> roleIds = userRoleBiz.getRoleIDByUserID(userID);
            if (roleIds != null && roleIds.size() != 0) {
                tagIds.addAll(permissionBiz.getResourceID(PermissionCode.ROLE, roleIds, PermissionCode.TAG));
            }
            tagIds.addAll(permissionBiz.getResourceID(PermissionCode.USER, userIds, PermissionCode.TAG));
            if (tagIds != null && tagIds.size() != 0) {
                computerIds.addAll(tagBiz.getResourceIDByTagIds(PermissionCode.COMPUTER, tagIds));
            }
            computerIds.addAll(permissionBiz.getResourceID(PermissionCode.USER, userIds, PermissionCode.COMPUTER));
            if (computerIds != null && computerIds.size() != 0) {
                return computerBiz.getComputerAndTag(computerIds);
            }
            return null;
        }
    }

    @Override
    public List<ComputersSortByProductCode> getComputersSortByProductCode(String productCode,Long userID) {
        List<Computer> computerList = computerBiz.getComputerList(userID);
        List<ComputersSortByProductCode> computersSortByProductCodes = computerBiz.getComputersSortByProductCode(productCode);
        List<ComputersSortByProductCode> computersSortByProductCodeList = new ArrayList<>();
        for(ComputersSortByProductCode computersSortByProductCode:computersSortByProductCodes){
            for(Computer computer:computerList){
                if(computer.getComputerID().equals(computersSortByProductCode.getComputerID())){
                    computersSortByProductCodeList.add(computersSortByProductCode);
                }
            }
        }
        return computersSortByProductCodeList;
    }
}
