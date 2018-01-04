package com.utils;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)   //使用junit4进行测试
//加载配置文件
@ContextConfiguration(locations = "/config/spring/application-context.xml")

public class BaseTest {

}
