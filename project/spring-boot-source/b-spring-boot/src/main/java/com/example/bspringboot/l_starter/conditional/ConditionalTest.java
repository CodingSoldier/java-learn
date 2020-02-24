package com.example.bspringboot.l_starter.conditional;

import com.example.bspringboot.util.ApplicationUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ConditionalTest {

    @Test
    void contextLoads() {
        System.out.println("##########"+ApplicationUtil.getApplicationContext().getBean("conBean"));
    }

}
