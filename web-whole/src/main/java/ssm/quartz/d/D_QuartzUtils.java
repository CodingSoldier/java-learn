package ssm.quartz.d;

import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2018/3/4
 */
@Component
public class D_QuartzUtils {
    private static Scheduler scheduler;
    static{
        try {
              scheduler = new StdSchedulerFactory().getScheduler();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 添加job
     */
    public static void addJob(D_QuartzModel qm) throws Exception{
        //创建jobdetail
        JobDetail jobDetail = JobBuilder.newJob(qm.getJobClass())
                .withIdentity(qm.getJobName(),qm.getJobGroupName())
                .build();
        //传入参数给job
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        Map<String, Object> param = qm.getParam();
        if(param != null){
            for (String key:param.keySet()){
                jobDataMap.put(key, param.get(key));
            }
        }

        //创建Trigger
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(qm.getTriggerName(), qm.getTriggerGroupName())
                .withSchedule(CronScheduleBuilder.cronSchedule(qm.getCron()))
                .build();

        //调度容器设置jobDetail、trigger
        scheduler.scheduleJob(jobDetail, trigger);

        if (!scheduler.isShutdown()){
            scheduler.start();
        }
    }

    /**
     * 修改Job触发时间
     */
    public static void modifyJobTime(D_QuartzModel qm) throws Exception{
        //创建一个triggerKey
        TriggerKey triggerKey = TriggerKey.triggerKey(qm.getTriggerName(), qm.getTriggerGroupName());
        //triggerKey是trigger在scheduler中的唯一标示，通过triggerKey可以找到trigger
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if (trigger == null){
            return;
        }

        String oldCron = trigger.getCronExpression();
        if (false == StringUtils.equals(oldCron, qm.getCron())){
            Trigger newTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(qm.getTriggerName(), qm.getTriggerGroupName())
                    .withSchedule(CronScheduleBuilder.cronSchedule(qm.getCron()))
                    .build();
            scheduler.rescheduleJob(triggerKey, newTrigger);
        }
    }
}
