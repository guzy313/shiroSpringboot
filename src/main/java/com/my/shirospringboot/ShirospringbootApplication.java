package com.my.shirospringboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@MapperScan("com.my.shirospringboot.mapper")
@SpringBootApplication
public class ShirospringbootApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(ShirospringbootApplication.class, args);

	}

}
