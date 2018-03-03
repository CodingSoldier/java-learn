package ssm.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.matchers.KeyMatcher;

import java.util.Date;
import java.util.List;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2018/3/3
 */
public class B_Quartz {

    public static void main(String[] args) throws Exception {
        /* JobDetail 定义的是任务数据，而真正的执行逻辑是在Job中。 为什么设计成JobDetail + Job，不直接使用Job？这是因为任务是有可能并发执行，如果Scheduler直接使用Job，就会存在对同一个Job实例并发访问的问题。而JobDetail & Job 方式，scheduler每次执行，都会根据JobDetail创建一个新的Job实例，这样就可以规避并发访问的问题。*/
        //jobDetail和Trigger都有name、group。name是jobDetail、Trigger在scheduler里面的唯一标识。
        JobKey jobKey = new JobKey("dummyJobName", "group01");
        JobDetail jobDetail = JobBuilder.newJob(B_HelloJob.class)
                .withIdentity(jobKey).build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("dummyTriggerName","group01")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?")).build();

        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        //将监听器注册到scheduler，监听器new B_HelloJobListener()匹配满足KeyMatcher.keyEquals(jobKey)条件的job，本例子中new B_HelloJobListener()仅仅监听一个job任务。
        scheduler.getListenerManager().addJobListener(new B_HelloJobListener(), KeyMatcher.keyEquals(jobKey));
        scheduler.start();
        scheduler.scheduleJob(jobDetail, trigger);

        printAllJob(scheduler);
    }

    //列出调度器所有的job任务
    public static void printAllJob(Scheduler scheduler) throws Exception{
        for (String groupName:scheduler.getJobGroupNames()){
           for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))){
               String jobName = jobKey.getName();
               String jobGroup = jobKey.getGroup();
               System.out.println("jobName****" + jobName);
               System.out.println("jobGroup****" + jobGroup);

               List<Trigger> triggerList = (List<Trigger>)scheduler.getTriggersOfJob(jobKey);
               Date nextFireTime = triggerList.get(0).getNextFireTime();
               System.out.println(jobName+"****下一次运行时间****"+nextFireTime);
           }
        }
    }

}
