package com.my.shirospringboot.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    /**
     * MD5 加密
     */
    public static String md5(String str) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] b = digest.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            if (Integer.toHexString(0xFF & b[i]).length() == 1)
                sb.append("0").append(Integer.toHexString(0xFF & b[i]));
            else
                sb.append(Integer.toHexString(0xFF & b[i]));
        }
        return sb.toString();
    }



}
