package com.my.shirospringboot;

import com.my.shirospringboot.mapper.ShRolesMapper;
import com.my.shirospringboot.mapper.ShUsersMapper;
import com.my.shirospringboot.pojo.ShRoles;
import com.my.shirospringboot.pojo.ShUsers;
import com.my.shirospringboot.pojo.User;
import com.my.shirospringboot.shiro.constant.SuperConstant;
import com.my.shirospringboot.shiro.core.base.ShiroUser;
import com.my.shirospringboot.shiro.core.bridge.ShUsersBridgeService;
import com.my.shirospringboot.shiro.core.bridge.impl.ShUsersBridgeServiceImpl;
import com.my.shirospringboot.utils.BeanUtils;
import com.my.shirospringboot.utils.StringUtils;
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
	private ShRolesMapper shRolesMapper;

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


	@Test
	void testIds(){

//		for (int i = 0; i < 10; i++) {
//			ShRoles shRoles = new ShRoles();
//			shRoles.setRoleName("测试角色"+i);
//			shRoles.setEnableFlag(SuperConstant.NO);
//			shRolesMapper.insert(shRoles);
//		}
//		List<String> s = new ArrayList<>();
//		s.add("1571034803604295681");
//		s.add("1571034803688181762");
//		s.add("1571034803688181763");
//		s.add("1571034803688181764");
//		s.add("1571034803688181765");
//		s.add("1571034803755290625");
//
//		int i = shRolesMapper.updateEnableFlagByIds(s, SuperConstant.NO);
//		System.out.println(i);
//		List<ShRoles> shRolesByIds = shRolesMapper.getShRolesByIds(s);
//		System.out.println(shRolesByIds);
		Map<String,Object> columnMap = new HashMap<>();
		columnMap.put("roleName","测试角色0");
		List<ShRoles> list = shRolesMapper.selectByMap(columnMap);
		System.out.println(list);

	}



}
