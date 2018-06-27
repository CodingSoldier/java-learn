package com.mybatis.wunao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mybatis.wunao.*.mapper")
public class WunaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WunaoApplication.class, args);
	}
}
