package com.my.shirospringboot.utils;

import com.my.shirospringboot.shiro.web.account.accountAction;
import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 */
public class ShiroRedissonSerialize {

    private static final Logger log = LoggerFactory.getLogger(ShiroRedissonSerialize.class);

    /**
     * 序列化方法
     * @param object
     * @return
     */
    public static String serialize(Object object){
        //判断对象是否为空
        if(ObjectUtils.isNullOrEmpty(object)){
            return null;
        }
        //流的操作
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        String encodeBase64 = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            oos.writeObject(null);//解决EOF的关键，加入一个空的对象
            encodeBase64 = EncodesUtils.encodeBase64(bos.toByteArray());
        }catch (IOException e){
            e.printStackTrace();
            log.error("流写入异常:{}",e);
        }finally {
            try {
                //关闭流
                bos.close();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return encodeBase64;
    }

    /**
     * 反序列化方法
     */
    public static Object deserialize(String str){
        //判断是否为空
        if(ObjectUtils.isNullOrEmpty(str)){
            return null;
        }
        //流的操作
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        Object object = null;
        try {
            bis = new ByteArrayInputStream(EncodesUtils.decodeBase64(str));
            ois = new ObjectInputStream(bis);
            //转换对象
            object = ois.readObject();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }finally {
            try {
                //关闭流
                bis.close();
                ois.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return object;
    }
}
