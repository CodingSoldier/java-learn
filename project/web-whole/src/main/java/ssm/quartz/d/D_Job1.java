package ssm.quartz.d;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class D_Job1 implements Job{
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("D_Job1时间：  "+new Date());

        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        dataMap.forEach((key,value)->{
            System.out.println("key： "+key+"********value: "+value);
        });
    }
}

