package com.zorkdata.center.admin.agent.core.scheduler;

import com.zorkdata.center.admin.config.FileConfiguration;
import com.zorkdata.center.admin.util.GitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

@Configuration
public class GitListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    GitUtil gitUtil;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        gitUtil.setupRepo();
    }
}
