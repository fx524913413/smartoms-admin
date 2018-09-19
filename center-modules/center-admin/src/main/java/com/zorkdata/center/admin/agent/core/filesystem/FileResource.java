package com.zorkdata.center.admin.agent.core.filesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/5/17 15:52
 */
public class FileResource {
    // 保存文件信息到磁盘
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
}
