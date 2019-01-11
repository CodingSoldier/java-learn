package ssm.quartz;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

//Job监听器
public class B_HelloJobListener implements JobListener {
    /*getName()返回一个字符串用以说明JobListener的名称对于注册为全局的监听器，getName() 主要用于记录日志，对于由特定 Job 引用的 JobListener，注册在 JobDetail 上的监听器名称必须匹配从监听器上 getName() 方法的返回值。*/
    @Override
    public String getName() {
        return "B_HelloJobListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
        String jobName = jobExecutionContext.getJobDetail().getKey().toString();
        System.out.println("监听***"+jobName+"***toBeExecuted");
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {

    }

    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {

        String jobName = jobExecutionContext.getJobDetail().getKey().toString();
        System.out.println("监听***"+jobName+"***wasExecuted");

        if(StringUtils.isNotEmpty(e.getMessage())){
            e.printStackTrace();
        }
    }
}
