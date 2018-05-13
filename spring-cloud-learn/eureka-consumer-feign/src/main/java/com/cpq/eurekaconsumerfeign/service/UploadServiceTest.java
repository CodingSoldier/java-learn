package com.cpq.eurekaconsumerfeign.service;


import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UploadServiceTest {

    @Autowired
    private UploadService uploadService;

    @Test
    public void handleFileUpload() throws Exception{
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        File file = new File(path+"application.properties");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file-name", file.getName(), "text/plain", IOUtils.toByteArray(input));

        System.out.println("**************"+uploadService.handleFileUpload(multipartFile));
    }
}
