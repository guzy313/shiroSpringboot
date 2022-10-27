package com.my.shirospringboot.shiro.core.base;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author Gzy
 * @version 1.0
 * @Description redis 缓存的简单实现类
 */
public class SimpleMapCache implements Cache<Object,Object>,Serializable {
    private final Map<Object,Object> attributes;
    private final String name;

    public SimpleMapCache(Map<Object, Object> backingMap, String name) {
        if(name == null){
            throw new IllegalArgumentException("Cache name cannot be null !");
        }else if(backingMap == null){
            throw new IllegalArgumentException("backingMap cannot be null !");
        }else{
            this.attributes = backingMap;
            this.name = name;
        }
    }

    @Override
    public Object get(Object key) throws CacheException {
        return attributes.get(key);
    }

    @Override
    public Object put(Object key, Object value) throws CacheException {
        return attributes.put(key,value);
    }

    @Override
    public Object remove(Object key) throws CacheException {
        return attributes.remove(key);
    }

    @Override
    public void clear() throws CacheException {
        attributes.clear();
    }

    @Override
    public int size() {
        return attributes.size();
    }

    @Override
    public Set<Object> keys() {
        Set<Object> keys = attributes.keySet();
        if(!keys.isEmpty()){
            return Collections.unmodifiableSet(keys);
        }else{
            return Collections.emptySet();
        }
    }

    @Override
    public Collection<Object> values() {
        Collection<Object> values = attributes.values();
        if((values == null || values.isEmpty())){
            return Collections.unmodifiableCollection(values);
        }else{
            return Collections.emptySet();
        }
    }

    @Override
    public String toString() {
        return "SimpleMapCache{" +
                "attributes=" + attributes +
                ", name='" + name + '\'' +
                '}';
    }
}
