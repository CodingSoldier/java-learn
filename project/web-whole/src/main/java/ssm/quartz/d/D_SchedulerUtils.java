package ssm.quartz.d;

import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

public class D_SchedulerUtils {
    public static final Object LOCK = new Object();
    private static Scheduler scheduler;
    public static Scheduler getScheduler() {
        try {
            synchronized(LOCK){
                if (scheduler == null){
                    scheduler = new StdSchedulerFactory().getScheduler();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return scheduler;
    }

}
