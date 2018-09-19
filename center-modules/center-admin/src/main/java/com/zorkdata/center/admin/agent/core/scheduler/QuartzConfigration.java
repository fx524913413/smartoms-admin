//package com.zorkdata.center.admin.agent.core.scheduler;
//
//import com.zorkdata.center.admin.agent.core.scheduler.ScheduleTask;
//import org.quartz.Trigger;
//import org.quartz.ee.servlet.QuartzInitializerListener;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
//import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
//import org.springframework.scheduling.quartz.SchedulerFactoryBean;
//
//@Configuration
//public class QuartzConfigration {
//    /**
//     * attention:
//     * Details：配置定时任务
//     */
//    @Bean(name = "jobDetail")
//    public MethodInvokingJobDetailFactoryBean detailFactoryBean(ScheduleTask task) {// ScheduleTask为需要执行的任务
//        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
//        /**
//         *  是否并发执行
//         *  例如每5s执行一次任务，但是当前任务还没有执行完，就已经过了5s了，
//         *  如果此处为true，则下一个任务会执行，如果此处为false，则下一个任务会等待上一个任务执行完后，再开始执行
//         */
//        jobDetail.setConcurrent(false);
//        // 设置任务的名字
//        jobDetail.setName("updateAgentStatus");
//        // 设置任务的分组，这些属性都可以存储在数据库中，在多任务的时候使用
//        jobDetail.setGroup("zork-omsagent");
//
//        /**
//         * 为需要执行的实体类对应的对象
//         */
//        jobDetail.setTargetObject(task);
//
//        /**
//         * updateAgentStatus为需要执行的方法
//         * 通过这几个配置，告诉JobDetailFactoryBean我们需要执行定时执行ScheduleTask类中的updateAgentStatus方法
//         */
//        jobDetail.setTargetMethod("updateAgentStatus");
//        return jobDetail;
//    }
//    /**
//     * attention:
//     * Details：配置定时任务的触发器，也就是什么时候触发执行定时任务
//     */
//    @Bean(name = "jobTrigger")
//    public CronTriggerFactoryBean cronJobTrigger(MethodInvokingJobDetailFactoryBean jobDetail) {
//        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
//        tigger.setJobDetail(jobDetail.getObject());
//        // 初始时的cron表达式
//        tigger.setCronExpression("0/30 * * * * ?");
//        // trigger的name
//        tigger.setName("updateAgentStatus");
//        return tigger;
//
//    }
//    /**
//     * attention:
//     * Details：定义quartz调度工厂
//     */
//    @Bean(name = "scheduler")
//    public SchedulerFactoryBean schedulerFactory(Trigger cronJobTrigger) {
//        SchedulerFactoryBean bean = new SchedulerFactoryBean();
//        // 用于quartz集群,QuartzScheduler 启动时更新己存在的Job
//        bean.setOverwriteExistingJobs(true);
//        // 延时启动，应用启动1秒后
//        bean.setStartupDelay(1);
//        // 注册触发器
//        bean.setTriggers(cronJobTrigger);
//        return bean;
//    }
//
//    /**
//     * quartz初始化监听器
//     */
////    @Bean
////    public QuartzInitializerListener executorListener() {
////        return new QuartzInitializerListener();
////    }
//}