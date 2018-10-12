package com.demo.thread;

import com.alibaba.fastjson.JSONObject;
import com.demo.SpringbootApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringbootApplication.class)
public class BaseTest {

    Logger logger = LoggerFactory.getLogger(BaseTest.class);

    @Autowired
    OutBean outBean;

    @Test
    public void test1() throws Exception{
        //TimeUnit.SECONDS.sleep(10L);
        for (int i=0; i<500; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //System.out.println(outBean.test().toString());
                    System.out.println(outBean.test2().toString());
                    //System.out.println(outBean.l2().toString());
                }
            }).start();
        }
    }

    @Test
    public void test2() throws Exception{
        for (int i=0; i<500; i++){

            new Thread(new Runnable() {

                ThreadLocal<String> mThreadLocal = new ThreadLocal<String>() {
                    @Override
                    protected String initialValue() {
                        return Thread.currentThread().getName();
                    }
                };

                @Override
                public void run() {
                    System.out.println(mThreadLocal.get());
                }
            }).start();

        }
    }

    @Test
    public void test4() throws Exception{
        HashMap map = new HashMap();
        map.put("key", 234234);
        String result = "{\"ksdfl\":\"dhfapjdf\"}";
        logger.error(String.format("异常url：%s\n参数：%s\n结果：%s", "/api/spt/web/refund/orderRefundApply/audit", JSONObject.toJSONString(map), result));
    }

}

