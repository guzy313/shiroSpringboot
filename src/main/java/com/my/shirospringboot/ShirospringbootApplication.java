package com.my.shirospringboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("com.my.shirospringboot.mapper")
@SpringBootApplication
public class ShirospringbootApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(ShirospringbootApplication.class, args);
		String[] beanDefinitionNames = run.getBeanDefinitionNames();
//		for (String s:beanDefinitionNames
//			 ) {
//			if(s.contains("ShiroRedisProperties")){
//				System.out.println(s);
//			}
//
//		}

	}

}
