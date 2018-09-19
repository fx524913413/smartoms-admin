//package com.zorkdata.center.admin.agent.core.scheduler;
//
//import javax.annotation.Resource;
//
//import org.quartz.CronScheduleBuilder;
//import org.quartz.CronTrigger;
//import org.quartz.JobDetail;
//import org.quartz.Scheduler;
//import org.quartz.SchedulerException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Configuration
//@EnableScheduling
//@Component
//public class ScheduleRefreshDatabase {
////    @Autowired
////    private ConfigRepository repository;
//
//    @Resource(name = "jobDetail")
//    private JobDetail jobDetail;
//
//    @Resource(name = "jobTrigger")
//    private CronTrigger cronTrigger;
//
//    @Resource(name = "scheduler")
//    private Scheduler scheduler;
//
//    @Scheduled(fixedRate = 5000) // 每隔5s查库，并根据查询结果决定是否重新设置定时任务
//    public void scheduleUpdateCronTrigger() throws SchedulerException {
//        System.out.println("fuck me");
//    }
//}