package com.my.shirospringboot.utils;

import java.util.*;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 */
public class PropertiesUtils {

    /**
     * @Description 通过文件名获取 Properties 对象
     * @param fileName
     * @return Properties
     */
    public static Properties getPropertiesKvByFileName(String fileName){
        try {
            Properties properties = new Properties();
            properties.load(PropertiesUtils.class.getClassLoader().
                    getResourceAsStream(fileName));
            return properties;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * 通过文件名获取 Properties 的key value 字符串集合(有序 LinkedHashMap)
     * @param fileName
     * @return Map<String,String>
     */
    public static Map<String,String> getPropertiesMapByFileName(String fileName){
        Properties properties = getPropertiesKvByFileName(fileName);
        Map<String,String> map = new LinkedHashMap<>();
        Set<Map.Entry<Object, Object>> entries = properties.entrySet();
        Iterator<Map.Entry<Object, Object>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> next = iterator.next();
            map.put(String.valueOf(next.getKey()),String.valueOf(next.getValue()));
        }
        return map;
    }


}
