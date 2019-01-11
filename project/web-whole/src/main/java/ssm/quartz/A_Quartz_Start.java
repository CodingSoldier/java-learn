package ssm.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * https://www.yiibai.com/quartz/quartz-helloworld.html#
 */


public class A_Quartz_Start {

    public static void main(String[] args) throws Exception{

        //定义job任务
        JobDetail jobDetail = JobBuilder.newJob(A_HelloJob.class)
                .withIdentity("dummyJobName", "group01").build();
        //jobdetail传入参数给具体job使用。
        jobDetail.getJobDataMap().put("key1", "value1");

        //定义一个trigger触发器
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("dummyTriggerName", "group01")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
                .build();

        //调度器将job和trigger连接在一起并执行
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(jobDetail, trigger);
    }
}









