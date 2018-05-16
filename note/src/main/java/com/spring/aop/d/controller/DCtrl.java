package com.spring.aop.d.controller;

import com.commonsjar.beanutils.User;
import com.spring.aop.DAnnoLog;
import com.spring.aop.d.service.DService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

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


    @DAnnoLog(operationType = "add操作", operationName = "add名字")
    public Map<String, Object> add(HttpServletRequest request, Map<String, Object> map){
        System.out.println("Ctrl1使用@Log注解");
        dService.addUser();
        //提供返回值
        Map<String, Object> r = new HashMap<String, Object>();
        r.put("key1", "val1");
        return r;
    }


    @DAnnoLog
    public User add(HttpServletRequest request, Map<String, Object> map, String str){
        System.out.println("Ctrl1使用@Log注解");
        dService.addUser();
        return new User();
    }
}
