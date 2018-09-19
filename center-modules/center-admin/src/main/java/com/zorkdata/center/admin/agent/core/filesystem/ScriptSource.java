//package com.zorkdata.center.admin.agent.core.filesystem;
//
//import com.zorkdata.center.admin.config.FileConfiguration;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class ScriptSource implements ApplicationRunner {
//    @Autowired
//    FileConfiguration fileConfiguration;
//
//    public static Map<String, String> scriptMap = new HashMap<>();
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        File file = new File(fileConfiguration.getScriptPath());
//
//        File[] files = file.listFiles();
//        if (files != null) {
//            for (File file1 : files) {
//                FileReader fileReader = new FileReader(file1);
//                try{
//                    BufferedReader reader = new BufferedReader(fileReader);
//                    String str = null;
//                    String scriptCode = "";
//                    while ((str = reader.readLine()) != null) {
//                        scriptCode += (str + "\n");
//                    }
//                    reader.close();
//                    scriptMap.put(file1.getName(), scriptCode);
//                }finally {
//                    if(fileReader!=null){
//                        fileReader.close();
//                    }
//                }
//            }
//            System.out.println("脚本加载完成");
//        } else {
//            System.out.println("文件为空");
//        }
//    }
//}
