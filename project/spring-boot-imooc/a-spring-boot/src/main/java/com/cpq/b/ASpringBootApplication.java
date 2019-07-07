package com.cpq.b;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan("com.cpq.b.web.servlet")
public class ASpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(ASpringBootApplication.class, args);
	}

}
