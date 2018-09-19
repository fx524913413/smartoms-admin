package com.zorkdata.center.admin.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/27 18:34
 */
@Configuration
@Data
public class FileConfiguration {

    @Value("${file.path}")
    private String filepath;

    @Value("${file.script}")
    private String scriptPath;

    @Value("${file.yml}")
    private String ymlPath;

    @Value("${sh.command}")
    private String shcommand;

    @Value("${shagentnotinmaster.command}")
    private String shagentnotinmastercommand;

    @Value("${shagentinmaster.command}")
    private String shagentinmastercommand;

    @Value("${jsch.file}")
    private String jschfile;

    @Value("${jsch2.file}")
    private String jschfile2;

    @Value("${jsch2.filemaster}")
    private String jschfilemaster;

    @Value("${icon.complete}")
    private String iconcomplete;

    @Value("${icon2.wrong}")
    private String icon2wrong;

    @Value("${salt.URL}")
    private String saltURL;

    @Value("${salt.UserName}")
    private String saltUserName;

    @Value("${salt.Password}")
    private String saltPassword;

    @Value("${socket.Timeout}")
    private Integer socketTimeout;

    @Value("${salt.Controller}")
    private String saltController;

    @Value("${file.testfile}")
    private String testfile;

}
