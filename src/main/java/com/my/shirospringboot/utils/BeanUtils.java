package com.my.shirospringboot.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
           //获取本类字段
           Field[] oFields = o.getClass().getDeclaredFields();

           Map<String,Object> map = new HashMap<>();
           for (Field oF:oFields ) {
              map.put(oF.getName(),ObjectUtils.getFieldValueByName(oF.getName(),o));
           }
           //如果存在父类
           if(o.getClass().getSuperclass() != null){
               //获取父类字段
               Field[] oParentFields = o.getClass().getSuperclass().getDeclaredFields();
               for (Field oF:oParentFields ) {
                   map.put(oF.getName(),ObjectUtils.getFieldValueByName(oF.getName(),o));
               }
           }
           Field[] cFields = c.getDeclaredFields();
           Object newInstance = c.newInstance();
           for (Field cF:cFields) {
               cF.setAccessible(true);
               cF.set(newInstance,map.get(cF.getName()));
           }
           return newInstance;
       }catch (Exception e){
           throw new RuntimeException("类反射错误");
       }

    }


    /**
     * @Description 属性相似类赋值(解决继承情况,获取父类属性)（从类1对象中抽取另一个类2拥有的属性并创建类2对象返回）
     * @param o
     * @param c
     * @return Object (自己强转)
     */
    public static Object toBeanParent(Object o,Class<?> c)  {
        try {
            Field[] oFields = o.getClass().getDeclaredFields();
            Map<String,Object> map = new HashMap<>();
            //本类属性
            for (Field oF:oFields ) {
                map.put(oF.getName(),ObjectUtils.getFieldValueByName(oF.getName(),o));
            }

            Object newInstance = null;
            //如果c类存在父类
            if(c.getSuperclass() != null){
                //获取父类属性
                Field[] parentFields = c.getSuperclass().getDeclaredFields();
                //实例化本类
                newInstance = c.newInstance();
                //获取本类属性
                Field[] fields = newInstance.getClass().getDeclaredFields();
                //给本类实例属性赋值
                for (Field f:parentFields) {
                    f.setAccessible(true);
                    f.set(newInstance,map.get(f.getName()));
                }
            }

            return newInstance;
        }catch (Exception e){
            throw new RuntimeException("类反射错误");
        }

    }



}
