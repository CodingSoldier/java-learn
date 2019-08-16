package com.example.springbootdemo;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootdemoApplicationTests {
    Logger log = LoggerFactory.getLogger(SpringbootdemoApplicationTests.class);

    @Autowired
    RestTemplate restTemplate;


    @Test
    public void testGet() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Resource> httpEntity = new HttpEntity<Resource>(headers);
        ResponseEntity<byte[]> response = restTemplate.exchange("http://pic16.nipic.com/20111006/6239936_092702973000_2.jpg", HttpMethod.GET, httpEntity, byte[].class);
        log.info("===状态码================");
        log.info(">> {}", response.getStatusCodeValue());
        log.info("===返回信息================");
        log.info(">> {}", response.getHeaders().getContentType());
        log.info(">> {}", response.getHeaders().getContentType().getSubtype());
        try {
            String t = "E:\\image001."+ response.getHeaders().getContentType().getSubtype();
            File tf = new File(t);
            FileUtils.writeByteArrayToFile(tf, response.getBody());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Test
    public void testGet111() {
        System.out.println(StringUtils.substringAfterLast("dfasdf.jpg", "."));
    }

}
