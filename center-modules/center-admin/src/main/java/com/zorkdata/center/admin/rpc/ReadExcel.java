package com.zorkdata.center.admin.rpc;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.zorkdata.center.admin.biz.ComputerBiz;
import com.zorkdata.center.admin.entity.Computer;
import com.zorkdata.center.admin.rpc.service.Ifc.ComputerServiceIfc;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import static com.zorkdata.center.common.util.ReadExcel.getCellFormatValue;

public class ReadExcel {

    public static void main(String[] args) {
        Workbook wb = null;
        Sheet sheet = null;
        Row row = null;
        List<List<String>> list = null;
        String cellData = null;
        String filePath = "D:\\readExcel.xlsx";
        String columns[] = {"type", "hostname", "ip", "mac", "username", "password"};
        wb = readExcel(filePath);
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
        //遍历解析出来的list
        List<Computer> computerList = new ArrayList<>();
        for (List<String> computerinlist : list) {
            Computer computer = new Computer();
            computer.setComputerType(computerinlist.get(0));
            computer.setHostName(computerinlist.get(1));
            computer.setIp(computerinlist.get(2));
            computer.setMac(computerinlist.get(3));
            computer.setUserName(computerinlist.get(4));
            computer.setPassword(computerinlist.get(5));
            computerList.add(computer);
        }
    }

    //读取excel
    public static Workbook readExcel(String filePath) {
        Workbook wb = null;
        if (filePath == null) {
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            InputStream is = fileInputStream;
            if (".xls".equals(extString)) {
                return wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                return wb = new XSSFWorkbook(is);
            } else {
                return wb = null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fileInputStream!=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return wb;
    }

    public static Object getCellFormatValue(Cell cell) {
        Object cellValue = null;
        if (cell != null) {
            //判断cell类型
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC: {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_FORMULA: {
                    //判断cell是否为日期格式
                    if (DateUtil.isCellDateFormatted(cell)) {
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    } else {
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING: {
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }

}
