package com.xxl.job.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxl.job.admin.controller.annotation.PermissionLimit;
import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.core.biz.model.ReturnT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 自定义API接口
 * @author chenpq05
 * @since 2023/3/28 16:33
 */
@RestController
@RequestMapping("/custom-api")
public class CustomApiController {

    private static Logger logger = LoggerFactory.getLogger(CustomApiController.class);

    @RequestMapping("/mytest")
    @PermissionLimit(limit = false)
    public ReturnT<String> mytest(@RequestBody XxlJobInfo jobInfo) throws Exception{
        logger.info("##################");
        String s = new ObjectMapper().writeValueAsString(jobInfo);
        logger.info(s);
        return new ReturnT();
    }
}
