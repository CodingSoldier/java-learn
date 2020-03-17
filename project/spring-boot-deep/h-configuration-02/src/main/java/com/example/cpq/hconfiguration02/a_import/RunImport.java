package com.example.cpq.hconfiguration02.a_import;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;


@Import(ImportBean.class)
@EnableAutoConfiguration
public class RunImport {

    public static void main(String[] args) {
        ConfigurableApplicationContext app = new SpringApplicationBuilder(RunImport.class)
                .web(WebApplicationType.NONE)
                .run(args);

        //ImportBean importBean = app.getBean(ImportBean.class);

        // 使用@Import()导入bean，bean名称是全名
        ImportBean importBean = app.getBean("com.example.cpq.hconfiguration02.a_import.ImportBean", ImportBean.class);

        System.err.println(importBean);

        app.close();
    }

}
