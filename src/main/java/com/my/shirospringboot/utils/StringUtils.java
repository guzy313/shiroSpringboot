package com.my.shirospringboot.utils;

import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description 字符串工具类
 */
public class StringUtils extends org.springframework.util.StringUtils {

    /**
     * @Description: 将字符串集合转换为单引号拼接的形式(一般sql where in 用)
     * @return String example : 'str1','str2'...
     */
     public static String idsFormatToSqlIn(List<String> strList){
         StringBuffer str = new StringBuffer();
         for (int i = 0; i < strList.size(); i++) {
             str.append("'");
             str.append(strList.get(i));
             str.append("'");
             if(i != strList.size() - 1){
                 str.append(",");
             }
         }
         return str.toString();
     }

}
