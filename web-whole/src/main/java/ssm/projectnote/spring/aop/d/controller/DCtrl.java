package ssm.projectnote.spring.aop.d.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ssm.projectnote.spring.aop.DAnnoLog;
import ssm.projectnote.spring.aop.d.service.DService;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2017/12/30
 */
@Controller
public class DCtrl {
    @Autowired
    DService dService;

    @DAnnoLog(operationType = "ctrl操作", operationName = "ctrl名字")
    public void add(){
        dService.addUser();
    }
}
