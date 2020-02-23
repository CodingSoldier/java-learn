package com.example.bspringboot.g_startup;


import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class ApplicationRunnerFirst implements ApplicationRunner {
    /**
     添加Program arguments： --test=value01
     ApplicationRunner.run参数是ApplicationArguments对象
     原始的String... args通过 ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
     源码org.springframework.boot.DefaultApplicationArguments#DefaultApplicationArguments(java.lang.String[])
     CommandLineRunner.run参数是原始的String... args，其实是通过args.getSourceArgs()得到
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("#######启动加载器--ApplicationRunnerFirst");
    }
}
