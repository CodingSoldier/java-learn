package ssm.quartz.d;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2018/3/4
 */
@Service
public class D_Job2 implements Job{
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        dataMap.forEach((key,value)->{
            System.out.println("key： "+key+"********value: "+value);
        });
    }
}
