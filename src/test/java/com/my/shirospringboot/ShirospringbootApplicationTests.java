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

import java.io.FileInputStream;
import java.util.*;

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

	@Test
	void getProperties(){
		try {
			Properties properties = new Properties();
			properties.load(this.getClass().getClassLoader().
					getResourceAsStream("authentication.properties"));
			Set<Map.Entry<Object, Object>> entries = properties.entrySet();
			Iterator<Map.Entry<Object, Object>> iterator = entries.iterator();
			while (iterator.hasNext()){
				Map.Entry<Object, Object> next = iterator.next();
				System.out.println(next.getKey());
				System.out.println(next.getValue());
			}
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	@Test
	void testBeanUtils(){
		ShiroUser shiroUser = new ShiroUser();
		shiroUser.setId("123");
		shiroUser.setLoginName("test1");

		ShUsers shUsers = (ShUsers)BeanUtils.toBean(shiroUser, ShUsers.class);
		System.out.println(shUsers);

	}


}
