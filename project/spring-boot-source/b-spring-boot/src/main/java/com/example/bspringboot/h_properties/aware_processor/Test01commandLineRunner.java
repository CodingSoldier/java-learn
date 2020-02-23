package com.example.bspringboot.h_properties.aware_processor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Test01commandLineRunner implements CommandLineRunner, MyAware {

    private Flag flag;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("##aware_processor.Test01commandLineRunner.setFlag "+flag.toString());
    }

    @Override
    public void setFlag(Flag flag) {
        this.flag = flag;
    }
}
