package com.my.shirospringboot.shiro.core.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
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

    @Autowired
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
                .setId(sessionId)//jti-构建唯一标识，此时使用shiro生成的唯一ID做标识(存的时候源码中setId的key是jti)
                .setIssuedAt(new Date())//构建签发时间
                .setIssuer(iss)//颁发者
                .signWith(SignatureAlgorithm.HS256,base64EncodeSecretKey);//提供算法以及秘钥

        if(ttlMillis > 0){
            //如果剩余过期时间大于0
            long expMills = nowMills + ttlMillis; //超时时间戳 = 当前时间 + 过期时间
            Date expDate = new Date(expMills);
            jwtBuilder.setExpiration(expDate);
        }
        String compact = jwtBuilder.compact();//生成加密字符串
        return compact;
    }

    /**
     * @Descriptipon: 解析令牌
     * @param jwtToken
     * @return
     */
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

    /**
     * @Description: 校验令牌:1、头部信息和荷载信息是否被篡改 2、校验令牌是否过期
     * @param jwtToken 用户传过来的token
     * @return
     */
    public boolean isVerifyToken(String jwtToken){
        //获取加密签名
        String base64EncodeSecretKey =
                EncodesUtils.encodeBase64(jwtProperties.getBase64EncodeSecretKey().getBytes());
        //指定加密算法(HMAC256 对应HS256)
        Algorithm algorithm = Algorithm.
                HMAC256(jwtProperties.getBase64EncodeSecretKey().getBytes());
        //带着签名构建校验对象
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        //校验--如果校验失败会抛出异常
        jwtVerifier.verify(jwtToken);
        return true;
    }

}
