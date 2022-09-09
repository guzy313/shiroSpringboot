package com.my.shirospringboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.my.shirospringboot.mapper")
@SpringBootApplication
public class ShirospringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShirospringbootApplication.class, args);
	}

}
