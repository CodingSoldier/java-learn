package com.example.bspringboot.h_properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ProfileCommandLineRnnuer implements CommandLineRunner {

    @Value("${spring.profiles.learn}")
    String springprofilesdefault;
    @Value("${test.include1}")
    String testinclude1;
    @Value("${test.include2}")
    String testinclude2;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("$profiles  "+springprofilesdefault);
        System.out.println("testinclude1  "+testinclude1);
        System.out.println("testinclude2  "+testinclude2);
    }
}
