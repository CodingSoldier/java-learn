package ssm.quartz.d.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ssm.quartz.d.D_Job1;
import ssm.quartz.d.D_QuartzModel;
import ssm.quartz.d.D_QuartzUtils;
import ssm.utils.OutPutData;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2018/3/4
 */
@RequestMapping("/quartz")
@Controller
public class D_Controller {

    @RequestMapping("/addJob01")
    @ResponseBody
    public OutPutData addJob01(HttpServletRequest request) throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("k1", "v1");

        D_QuartzModel qm = new D_QuartzModel();
        qm.setCron("0/5 * * * * ? *");
        qm.setJobClass(D_Job1.class);
        qm.setJobName("job01");
        qm.setJobGroupName("group01");
        qm.setTriggerName("trigger01");
        qm.setTriggerGroupName("group01");
        qm.setParam(map);

        D_QuartzUtils.addJob(qm);

        return new OutPutData("", true);
    }

    @RequestMapping("/modifyJobTime01")
    @ResponseBody
    public OutPutData modifyJobTime01(HttpServletRequest request) throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("k1", "v1");

        D_QuartzModel qm = new D_QuartzModel();
        qm.setCron("0/11 * * * * ? *");
        qm.setJobClass(D_Job1.class);
        qm.setJobName("job01");
        qm.setJobGroupName("group01");
        qm.setTriggerName("trigger01");
        qm.setTriggerGroupName("group01");
        qm.setParam(map);

        D_QuartzUtils.modifyJobTime(qm);

        return new OutPutData("", true);
    }
}
