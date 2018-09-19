package com.zorkdata.center.admin.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtil {
    /*
    *  处理不规范的Json：存在"="号以及key没加""
    * */
    public static String getFormatRegularJson(String json) {
        if (StringUtils.isBlank(json)) {
            return "";
        }

        String temp = json.replace("=","\"=\"")
                    .replace("=",":")
                    .replace("}","\"}")
                    .replace("{","{\"")
                    .replace(",","\",")
                    .replaceAll(",\\s",", \"")
                    .replaceAll("\"\\{","{")
                    .replaceAll(":\"\\[",":\\[")
                    .replaceAll("\\}\\]\"","\\}\\]")
                    .replaceAll("\\}\"","\\}");

        // 处理特殊数据，存在多个ip使用","隔开
        Pattern p = Pattern.compile("\\d\",\\d");
        Matcher m = p.matcher(temp);
        String regularJson = "";
        while(m.find()) {
            regularJson = temp.replaceAll(m.group(0), m.group(0).replace("\"",""));
        }
        return regularJson;
    }
}
