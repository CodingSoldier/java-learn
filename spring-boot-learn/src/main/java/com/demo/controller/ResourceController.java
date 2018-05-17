/*
package com.demo.controller;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

@RestController
public class ResourceController {

    */
/**
     * 读取jar包中的xml
     *//*

    @GetMapping("/xml/custom")
    public String getXml() throws Exception{
        String r = "";
        //由于是子工程，classpath是java-learn，这种方式打成jar会报错
        //Document doc = new SAXReader()
        //        .read(new File("spring-boot-learn/src/main/resources/config/custom.xml"));

        //File类不能读取jar包中的文件
        //ClassLoader classLoader = getClass().getClassLoader();
        //String path = classLoader.getResource("config/custom.xml").getPath();
        //Document doc = new SAXReader().read(new File(path));
        //Element root = doc.getRootElement();
        //r = root.element("num").getText() + "\n 文件Path：" + path;


        */
/**
         getResource()从classpath下找文件，不加 / 开头
         不通过new File，通过read一个URL的方式也可以读取到jar包中的xml
         url值举例：jar:file:/E:/workspace/java-learn/文件/spring-boot-learn-1.0-SNAPSHOT.jar!/BOOT-INF/classes!/config/custom.xml
         *//*

        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("config/custom.xml");
        Document doc = new SAXReader().read(url);
        Element root = doc.getRootElement();
        r = root.element("num").getText() + "\n 文件URL：" + url;

        //InputStream is=this.getClass().getResourceAsStream("/config/custom.xml");
        //Document doc = new SAXReader().read(is);
        //Element root = doc.getRootElement();
        //r = root.element("num").getText();

        return r;
    }

    */
/**
     * 读取jar包中的json文件
     *//*

    @GetMapping("/json/custom")
    public String getJson() throws Exception{
        String path = "config/validate/validate.json";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(path);
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null){
            buffer.append(line);
        }
        String jsonStr = buffer.toString();

        //JSONObject jsonObject = JSONObject.fromObject(jsonStr);

        return jsonStr;
    }


}
*/
