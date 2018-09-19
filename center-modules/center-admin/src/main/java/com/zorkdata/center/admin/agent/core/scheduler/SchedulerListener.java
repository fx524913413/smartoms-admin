package com.zorkdata.center.admin.agent.core.scheduler;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class SchedulerListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private MyScheduler scheduler;
    @Autowired
    private MyJobFactory jobFactory;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            scheduler.scheduleJobs();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(jobFactory);
        return schedulerFactoryBean;
    }

}
