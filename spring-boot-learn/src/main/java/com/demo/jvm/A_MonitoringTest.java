package com.demo.jvm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/jvm")
public class A_MonitoringTest {
    Logger logger = LoggerFactory.getLogger(A_MonitoringTest.class);

    static class OOMObject{
        public byte[] placeholder = new byte[64 * 1024];
    }

    public static void fillHeap(int num) throws InterruptedException{
        List<OOMObject> list = new ArrayList<>();
        for (int i = 0; i < num ; i++){
            Thread.sleep(50);;
            list.add(new OOMObject());
        }
        System.gc();
    }

    // 117
    @GetMapping("/fill")
    public void fill(HttpServletRequest request, HttpServletResponse response) throws Exception{

        fillHeap(1000);

    }

}
