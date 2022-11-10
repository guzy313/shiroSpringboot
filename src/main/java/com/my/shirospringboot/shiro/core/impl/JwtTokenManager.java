package com.my.shirospringboot.shiro.core.impl;

import com.my.shirospringboot.shiro.config.JwtProperties;
import com.my.shirospringboot.utils.EncodesUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gzy
 * @version 1.0
 * @Description:自定义jwtToken管理器
 * 作用：令牌的颁发、解析、校验
 */
@Service("jwtTokenManager")
@EnableConfigurationProperties({JwtProperties.class})
public class JwtTokenManager {

    private JwtProperties jwtProperties;

    /**
     * @Description:签发令牌
     * @param iss 签发者
     * @param ttlMillis 过期时间-多久过期
     * @param sessionId 会话ID
     * @param claims 存储的非隐私信息
     * @return
     */
    public String issuedToken(String iss, long ttlMillis, String sessionId,
                              Map<String,Object> claims){
        if(claims == null){
            claims = new HashMap<>();
        }
        //获取当前时间
        long nowMills = System.currentTimeMillis();
        //获取加密签名-进行base64编码
        String base64EncodeSecretKey =
                EncodesUtils.encodeBase64(jwtProperties.getBase64EncodeSecretKey().getBytes());
        //构建令牌
        JwtBuilder jwtBuilder = Jwts.builder()
                .setClaims(claims)//构建非隐私信息
                .setId(sessionId)//jti-构建唯一标识，此时使用shiro生成的唯一ID做标识
                .setIssuedAt(new Date())//构建签发时间
                .setIssuer(iss)//颁发者
                .signWith(SignatureAlgorithm.HS512,base64EncodeSecretKey);//提供算法以及秘钥

        if(ttlMillis > 0){
            //如果剩余过期时间大于0
            long expMills = nowMills + ttlMillis; //超时时间戳 = 当前时间 + 过期时间
            Date expDate = new Date(expMills);
            jwtBuilder.setExpiration(expDate);
        }
        String compact = jwtBuilder.compact();//生成加密字符串
        return compact;
    }

    //解析令牌
    public Claims decodeToken(String jwtToken){
        //获取加密签名-进行base64编码
        String base64EncodeSecretKey =
                EncodesUtils.encodeBase64(jwtProperties.getBase64EncodeSecretKey().getBytes());
        //带着签名去解析字符串
        Claims claims = Jwts.parser()
                .setSigningKey(base64EncodeSecretKey)
                .parseClaimsJws(jwtToken)
                .getBody();
        return claims;
    }

    //校验令牌
    //TODO

}
