package com.coq.rabbitmq.sp01;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vanrui.dto.EmpExternalDTO;
import com.vanrui.dto.JSONUtil;
import com.vanrui.dto.RabbitDTO;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Test
	public void testSender2() throws Exception {
		File file = new File("C:\\Users\\chenpq05\\Desktop\\万科城id.txt");

		String file1 = FileUtils.readFileToString(file);//前面两行是读取文件

		JSONArray jsonArray = JSON.parseArray(file1);

		for (int i=0; i<jsonArray.size(); i++) {

			Long empId = jsonArray.getLong(i);
			RabbitDTO rabbitDTO = new RabbitDTO();
			rabbitDTO.setAction(RabbitDTO.UPDATE);
			// 开发
			// rabbitDTO.setPrimaryKey(10648930L);

			rabbitDTO.setPrimaryKey(empId);

			EmpExternalDTO empExternalDTO = new EmpExternalDTO();
			empExternalDTO.setAction(RabbitDTO.UPDATE);
			empExternalDTO.setSpatialCode(null);

			rabbitDTO.setExternal(JSONUtil.JavaBeantoJSONString(empExternalDTO));

			TimeUnit.MILLISECONDS.sleep(500);

			rabbitTemplate.convertAndSend(RabbitDTO.MATERIAL_DIRECT_EXCHANGE, RabbitDTO.EMP_ROUTING_KEY, rabbitDTO);
			// System.out.println("###############end###############");
			String format = String.format("####end#### i=%s，empId=%s", i, empId);
			System.out.println(format);

		}

	}


}
