package com.my.shirospringboot.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author guzy
 * @version 1.0
 * @description
 */
public class BeanUtils {

    /**
     * @Description 属性相似类赋值（从类1对象中抽取另一个类2拥有的属性并创建类2对象返回）
     * @param o
     * @param c
     * @return Object (自己强转)
     */
    public static Object toBean(Object o,Class<?> c)  {
       try {
           Field[] oFields = o.getClass().getDeclaredFields();
           Map<String,Object> map = new HashMap<>();
           for (Field oF:oFields ) {
              map.put(oF.getName(),ObjectUtils.getFieldValueByName(oF.getName(),o));
           }
           Class<?> cClass = Class.forName(c.getName());
           Field[] cFields = cClass.getDeclaredFields();
           Object newInstance = cClass.newInstance();
           for (Field cF:cFields) {
               cF.setAccessible(true);
               cF.set(newInstance,map.get(cF.getName()));
           }
           return newInstance;
       }catch (Exception e){
           throw new RuntimeException("类反射错误");
       }

    }




}
