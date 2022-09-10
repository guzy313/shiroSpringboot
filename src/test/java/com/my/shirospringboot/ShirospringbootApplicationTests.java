package com.my.shirospringboot;

import com.my.shirospringboot.mapper.ShUsersMapper;
import com.my.shirospringboot.pojo.ShUsers;
import com.my.shirospringboot.pojo.User;
import com.my.shirospringboot.shiro.core.base.ShiroUser;
import com.my.shirospringboot.shiro.core.bridge.ShUsersBridgeService;
import com.my.shirospringboot.shiro.core.bridge.impl.ShUsersBridgeServiceImpl;
import com.my.shirospringboot.utils.BeanUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ShirospringbootApplicationTests {
	@Autowired
	private ShUsersMapper shUsersMapper;

	@Autowired
	private ShUsersBridgeServiceImpl shUsersBridgeService;


	@Test
	void contextLoads() {

	}


	@Test
	void addUser(){
		ShUsers shUsers = new ShUsers();
		shUsers.setLoginName("admin");
		shUsers.setRealName("管理员");
		shUsers.setNickName("测试昵称");
		shUsers.setPassword("123");
		shUsersMapper.insert(shUsers);

	}

	@Test
	void query(){
//		ShUsers admin = shUsersBridgeService.findUserByLoginName("admin");
//		System.out.println(admin);
		ShUsers user = shUsersBridgeService.findUserByLoginName("admin");



		//构建认证令牌对象
		ShiroUser shiroUser = (ShiroUser)BeanUtils.toBean(user,ShiroUser.class);
		System.out.println(shiroUser);

	}


}
