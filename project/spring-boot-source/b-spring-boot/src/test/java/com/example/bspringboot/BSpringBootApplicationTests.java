package com.example.bspringboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


//@SpringBootTest(properties = {"property.key=3、测试环境properties"})
@SpringBootTest
@TestPropertySource({"classpath:properties-test2.properties"})
class BSpringBootApplicationTests {

    @Value("${property.key}")
    String propertyKey;

    @Test
    void contextLoads() {
        System.out.println("$测试 "+ propertyKey);
    }

}
