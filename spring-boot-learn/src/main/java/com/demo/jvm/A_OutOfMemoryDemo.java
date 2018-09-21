package com.demo.jvm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 启动服务 nohup java -jar spring-boot-lean.jar &
 *
 * 查看java堆最大值，默认是服务器内存的1/4
 *      -XX:+PrintFlagsFinal -version | grep HeapSize
 * 查看java相关的端口
 *      ps -ef|head -n 1;ps -ef|grep -i java
 * 查看pid使用的内存
 *      top -p 服务的pid
 */

@RestController
@RequestMapping("/jvm")
public class A_OutOfMemoryDemo {
    Logger logger = LoggerFactory.getLogger(A_OutOfMemoryDemo.class);

    //内存异常测试
    static class OOMObject2{
        public byte[] placeholder = new byte[1024 * 1024 * 10];  //10M byte数组
    }
    public static void fillOutOfMemory(int num) throws InterruptedException{
        List<OOMObject2> list = new ArrayList<>();
        for (int i = 0; i < num ; i++){
            list.add(new OOMObject2());
        }
    }
    @GetMapping("/outOfMemory")
    public String outOfMemory(HttpServletRequest request) throws Exception{
        logger.info("***********************outOfMemory***************");
        boolean a = true;
        while (a){
            fillOutOfMemory(1000);
        }
        return "完成";
    }

    //内存溢出后是否还能请求接口
    @GetMapping("/afterOutOfMemory")
    public String afterOutOfMemory(HttpServletRequest request) throws Exception{
        logger.info("***********************outOfMemory***************");
        return "返回结果";
    }

}
