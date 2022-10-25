package com.my.shirospringboot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.my.shirospringboot.mapper.ShRolesMapper;
import com.my.shirospringboot.mapper.ShUsersMapper;
import com.my.shirospringboot.pojo.ShRoles;
import com.my.shirospringboot.pojo.ShUsers;
import com.my.shirospringboot.pojo.User;
import com.my.shirospringboot.shiro.constant.SuperConstant;
import com.my.shirospringboot.shiro.core.base.ShiroUser;
import com.my.shirospringboot.shiro.core.bridge.ShUsersBridgeService;
import com.my.shirospringboot.shiro.core.bridge.impl.ShUsersBridgeServiceImpl;
import com.my.shirospringboot.shiro.service.impl.UserServiceImpl;
import com.my.shirospringboot.shiro.vo.UserVo;
import com.my.shirospringboot.utils.BeanUtils;
import com.my.shirospringboot.utils.MD5Util;
import com.my.shirospringboot.utils.StringUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
class ShirospringbootApplicationTests {
	@Autowired
	private ShUsersMapper shUsersMapper;

	@Autowired
	private ShRolesMapper shRolesMapper;

	@Autowired
	private ShUsersBridgeServiceImpl shUsersBridgeService;

	@Autowired
	private UserServiceImpl userService;


	@Test
	void contextLoads() {
	}


	@Test
	void addUser() throws Exception{
		UserVo userVo = new UserVo();
		userVo.setLoginName("admin");
		userVo.setRealName("管理员");
		userVo.setNickName("adminHH");
		userVo.setPassword("123");
		userVo.setSex("男");
//		shUsersMapper.insert(shUsers);
		userService.saveOrUpdateUser(userVo);
//		for (int i = 0; i < 10; i++) {
//			UserVo userVo = new UserVo();
//			userVo.setLoginName("test"+i);
//			userVo.setRealName("测试用户"+i);
//			userVo.setNickName("测试昵称"+i);
//			userVo.setPassword("123");
//			userVo.setSex("男");
////		shUsersMapper.insert(shUsers);
//			userService.saveOrUpdateUser(userVo);
//		}

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



	@Test
	public void test(){
		String url = "https://dev-service.z7z7z7.cn/enterprise-service-platform/common/ums-site-third/clientToken";
		String timeStamp = String.valueOf(System.currentTimeMillis());
		String clientId = "e18cecc5ff404827a2689c0f79ae3f51";
		String clientSecret = "ifzxbgyiai";
		String site = "4";

		JSONObject json = new JSONObject();
		json.put("clientId", "e18cecc5ff404827a2689c0f79ae3f51");
		json.put("clientSecret", "ifzxbgyiai");
		json.put("timeStamp", timeStamp);
		String md5 = MD5Util.md5(JSON.toJSONString(json));
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("clientId", "e18cecc5ff404827a2689c0f79ae3f51");
		jsonParam.put("timeStamp", timeStamp);
		jsonParam.put("sign",md5);
		String s = sendPostRequest(url, jsonParam.toJSONString(), site);
		System.out.println("====================================");
		System.out.println(s);
		System.out.println("====================================");
	}

	/**
	 * 向目的URL发送post请求
	 * @param url       目的url
	 * @param params    发送的参数
	 * @return  AdToutiaoJsonTokenData
	 */
	public static String sendPostRequest(String url,String params,String site){
		System.out.println(params);

		RestTemplate client = new RestTemplate();
		//新建Http头，add方法可以添加参数
		HttpHeaders headers = new HttpHeaders();
		headers.add("site",site);
		//设置请求发送方式
		HttpMethod method = HttpMethod.POST;
		// 以表单的方式提交
		headers.setContentType(MediaType.APPLICATION_JSON);
		//将请求头部和参数合成一个请求
		HttpEntity<Object> requestEntity = new HttpEntity<>(params, headers);
		//执行HTTP请求，将返回的结构使用String 类格式化（可设置为对应返回值格式的类）
		ResponseEntity<String> response = client.exchange(url, method, requestEntity, String.class);

		return response.getBody();
	}

}
