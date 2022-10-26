package com.my.shirospringboot;

import com.my.shirospringboot.shiro.config.ShiroRedisProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("com.my.shirospringboot.mapper")
@ComponentScan(basePackages = "com.my.shirospringboot.shiro")
@SpringBootApplication
//@EnableConfigurationProperties({ShiroRedisProperties.class})
public class ShirospringbootApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(ShirospringbootApplication.class, args);

	}

}
