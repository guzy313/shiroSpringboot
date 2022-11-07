package com.my.shirospringboot.utils;

import com.my.shirospringboot.shiro.constant.SuperConstant;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Gzy
 * @version 1.0
 * @Description 密码加密
 */
public class DigestUtil {

    /**
     * @Description 摘要算法
     * @param input 需要加密的内容
     * @param salt  盐值-干扰数据
     * @return 加密之后的字符串
     */
    public static String sha1(String input,String salt){
        return new SimpleHash(SuperConstant.SHA1,input,salt,SuperConstant.HASH_ITERATIONS).toString();
    }

    /**
     * @Description 随机生成salt
     * @return hex编码的salt
     */
    public static String generateSalt(){
        SecureRandomNumberGenerator secureRandomNumberGenerator = new SecureRandomNumberGenerator();
        return secureRandomNumberGenerator.nextBytes().toHex();
    }

    /**
     * @Description 生成(hash计算之后)密码 和 salt明文
     * @param password
     * @return salt, password
     */
    public static Map<String,String>  entryptPassword(String password){
        Map<String,String> map = new HashMap<>();
        String salt = generateSalt();
        String afterHashPassword = sha1(password, salt);
        map.put("password",afterHashPassword);
        map.put("salt",salt);
        return map;
    }


}
