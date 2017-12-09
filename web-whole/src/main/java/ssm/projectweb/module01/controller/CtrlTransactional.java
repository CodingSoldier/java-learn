package ssm.projectweb.module01.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ssm.projectweb.module01.service.ServiceTransactional;
import ssm.utils.OutPutData;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2017/11/25
 */
@Controller
@RequestMapping("/transactional")
public class CtrlTransactional {
    @Resource
    private ServiceTransactional service;

    // http://localhost:8080/transactional/io?id=1
    @RequestMapping("/io")
    @ResponseBody
    public OutPutData t1(HttpServletRequest request, @RequestParam String id) throws Exception{
        service.io();
        OutPutData outPutData = new OutPutData();
        outPutData.setSuccess(true);
        return outPutData;
    }
}
