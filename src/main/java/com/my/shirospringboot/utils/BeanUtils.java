package com.my.shirospringboot.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.shirospringboot.shiro.vo.PermissionVo;
import com.my.shirospringboot.shiro.web.account.MenuAction;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.util.*;

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
                   if("serialVersionUID".equals(oF.getName()))continue;//序列化字段忽略
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
                if("serialVersionUID".equals(oF.getName()))continue;//序列化字段忽略
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

    /**
     * 只考虑本类，不考虑继承父类
     * @param list
     * @return
     */
    public static List<Map<String,Object>> objectListToMapList(List<?> list){
        List<Map<String,Object>> listResult = new ArrayList<>();
        for (Object o:list) {
            listResult.add(BeanUtils.objectToMap(o));
        }
        return listResult;
    }

    /**
     * 只考虑本类，不考虑继承父类
     * @param o
     * @return
     */
    public static Map<String,Object> objectToMap(Object o){
        Map<String,Object> map = new HashMap<>();
        Field[] declaredFields = o.getClass().getDeclaredFields();
        for (Field f:declaredFields ) {
            if("serialVersionUID".equals(f.getName()))continue;//序列化字段忽略
            map.put(f.getName(),ObjectUtils.getFieldValueByName(f.getName(), o));
        }
        return map;
    }

    /**
     * @Description: 复制对象,将src复制给target,忽视null值字段
     * @param src
     * @param newObject
     */
    public static void copyPropertiesIgnoreNull(Object src, Object newObject){
        org.springframework.beans.BeanUtils.copyProperties(src, newObject, getNullPropertyNames(src));
    }

    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }




}
