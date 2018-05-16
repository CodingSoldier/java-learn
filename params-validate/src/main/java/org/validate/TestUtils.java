package org.validate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:application-context.xml"})
public class TestUtils {
    @Autowired
    TestController testController;

    @Test
    public void t11() throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("c1", "abfa");
        map.put("c2", "2222");
        testController.request01(map);
    }
}
