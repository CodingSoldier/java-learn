package ssm.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2018/3/3
 */
public class A_HelloJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("运行job任务");
    }
}