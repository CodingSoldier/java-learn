package com.cpq.paramsvalidateboot;

import com.cpq.paramsvalidateboot.validate.Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ParamsValidateBootApplicationTests {

	@Test
	public void contextLoads() {
		Util.readFileToMap("config/validate/json/json-post.json");
        //Set<>
	}

}
