package ssm.utils;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

//WebAppConfiguration测试环境使用，用来表示测试环境使用的ApplicationContext将是WebApplicationContext类型的；value指定web应用的根，默认是src/main/webapp
@WebAppConfiguration()
@RunWith(SpringJUnit4ClassRunner.class)   //使用junit4进行测试
//加载配置文件
@ContextConfiguration(locations = {"classpath:config/spring/application-context.xml","classpath:config/spring/springmvc-controller.xml"})

public class BaseTest {

}
