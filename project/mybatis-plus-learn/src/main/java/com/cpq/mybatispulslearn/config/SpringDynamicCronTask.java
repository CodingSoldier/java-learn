package com.cpq.mybatispulslearn.config;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cpq.mybatispulslearn.linyi.third_to_cottage.entity.ThirdToCottage;
import com.cpq.mybatispulslearn.linyi.third_to_cottage.mapper.ThirdToCottageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;


@Slf4j
@Component
public class SpringDynamicCronTask implements SchedulingConfigurer  {

    @Autowired
    private ThirdToCottageMapper thirdToCottageMapper;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                // 任务逻辑
                log.debug("##############...");
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                ThirdToCottage thirdToCottage = thirdToCottageMapper.selectOne(Wrappers.lambdaQuery());
                String cron = thirdToCottage.getThirdVillageCode();
                // 任务触发，可修改任务的执行周期
                CronTrigger trigger = new CronTrigger(cron);
                Date nextExec = trigger.nextExecutionTime(triggerContext);
                return nextExec;
            }
        });
    }
}
