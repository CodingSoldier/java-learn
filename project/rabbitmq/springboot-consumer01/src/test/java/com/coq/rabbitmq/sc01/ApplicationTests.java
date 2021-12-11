package com.coq.rabbitmq.sc01;

import com.coq.rabbitmq.sc01.conusmer.RabbitReceiver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    RabbitReceiver rabbitReceiver;

    @Test
    public void contextLoads() throws Exception {

        rabbitReceiver.onOrderMessage("{\"id\": 12345, \"name\": \"中文名\"}", null, new HashMap<>());


    }

}
