package com.zorkdata.center.common.util;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class FindCharacter {
    public Boolean search(File a, String x) throws IOException {
        //在文件a中的每行中查找x
        Scanner scan = null;
        try{
            scan = new Scanner(a, "gbk");
            int k = 0;
            while (true) {
                if (scan.hasNext() == false) {
                    break;
                }
                String s = scan.nextLine();
                k++;
                if (s.contains(x)) {
                    return true;
                }
            }
        }finally{
            if(scan!=null){
                scan.close();
            }
        }
        Scanner scan1 = null;
        try{
            scan1 = new Scanner(a, "utf-8");
            int k1 = 0;
            while (true) {
                if (scan1.hasNext() == false) {
                    break;
                }
                String s1 = scan1.nextLine();
                k1++;
                if (s1.contains(x)) {
                    return true;
                }
            }
        }finally {
            if(scan1!=null){
                scan1.close();
            }
        }
        return false;
    }

    public boolean f(File a, String s, boolean flag) throws IOException {//在a下所有文件中查找含有s的行

        File ff = a;
        if (ff == null) {
            return flag;
        }
        //若a是文件，直接查找
        if (ff.isFile()) {
            return search(ff, s);
        }
        return flag;
    }
//    public static void main(String[] args)throws IOException {
//        f(new File("d:\\"),"100%  119MB");
//
//    }
}
