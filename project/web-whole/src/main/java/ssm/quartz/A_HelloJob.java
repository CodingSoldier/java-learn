package ssm.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class A_HelloJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("运行job任务");

        //接收传入的参数，JobDataMap是一个集合，即使不传入参数，也不会为空，不用做非空判断，此处size是0
        JobDataMap map = jobExecutionContext.getJobDetail().getJobDataMap();
        map.forEach((key, value) ->{
            System.out.println(key);
            System.out.println(value);
        });
    }
}