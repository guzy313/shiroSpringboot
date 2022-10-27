package com.my.shirospringboot.utils;

import org.apache.ibatis.javassist.bytecode.ByteArray;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * @author Gzy
 * @version 1.0
 * @Description 编码解码工具
 */
public class EncodesUtils {

    /**
     * base64编码
     * @param byteArray
     * @return String
     */
    public static String encodeBase64(byte[] byteArray){
        final BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(byteArray);
    }

    /**
     * base64解码
     * @param str
     * @return byte[]
     */
    public static byte[] decodeBase64(String str){
        final BASE64Decoder decoder = new BASE64Decoder();
        try {
            return decoder.decodeBuffer(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
