package com.zorkdata.center.admin.util;
/**
 * @author SwinBlackSea
 * @date 2018/1/2 15:54
 */
public enum CommonEnum {
    /**
     * SUCCESS：SUCCESS
     * FAILED：FAILED
     * RENAME：RENAME
     * NO：NO
     */
    SUCCESS("success"),
    FAILED("failed"),
    YES("yes"),
    EXIST("exist"),
    STILL_EXIST("still exist"),
    RENAME("rename"),
    NO("no");






    String value;
    CommonEnum(String value) {
        this.value = value;
    }


    public String  getValue() {
        return value;
    }

    public static void main(String[] args) {
        System.out.println(SUCCESS);
    }
}