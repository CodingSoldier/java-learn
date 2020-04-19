package com.example.springbootdemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
//@SpringBootTest(properties = {"test.property=3、测试中的@SpringBootTestproperties注解属性"})
@TestPropertySource({"classpath:application-test-property-source.properties"})
class SpringBootDemoApplicationTests {
    @Test
    void contextLoads() {
    }
}
