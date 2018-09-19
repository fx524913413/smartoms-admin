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
public class LdapConfiguration {

    @Value("${ldap.ldapHost}")
    private String ldapHost;

    @Value("${ldap.ldapPort}")
    private int ldapPort;

    @Value("${ldap.ldapBindDN}")
    private String ldapBindDN;

    @Value("${ldap.ldapPassword}")
    private String ldapPassword;

    @Value("${ldap.root}")
    private String root;

    @Value("${ldap.dc}")
    private String dc;

    @Value("${ldap.ou}")
    private String ou;

    @Value("${ldap.initPassword}")
    private String initPassword;

    @Value("${ldap.open}")
    private boolean open;


}
