package com.my.shirospringboot;

import com.my.shirospringboot.mapper.UserMapper;
import com.my.shirospringboot.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ShirospringbootApplicationTests {
	@Autowired
	private UserMapper userMapper;


	@Test
	void contextLoads() {
		List<User> all = userMapper.findAll();
		System.out.println(all);
		User user = userMapper.selectById(1);
		System.out.println(user);

	}





}
