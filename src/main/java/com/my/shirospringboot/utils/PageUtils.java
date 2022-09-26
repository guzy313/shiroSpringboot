package com.my.shirospringboot.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 */
public class PageUtils {


    /**
     * object集合 分页加页码 转Map集合
     * @param list
     * @param pageSize
     * @param pageIndex
     * @return
     */
    public static List<Map<String,Object>> paging(List<?> list,Integer pageSize,Integer pageIndex){
        if(pageSize == null){
            throw new RuntimeException("调用分页方法时页面大小不能为空");
        }
        if(pageIndex == null){
            throw new RuntimeException("调用分页方法时页码不能为空");
        }
        //将对象集合转换成Map集合
        List<Map<String,Object>> listResult = BeanUtils.objectListToMapList(list);
        //开始分页
        int runNum = 1;
        for (Map<String,Object> m:listResult) {
            if(runNum >= pageSize * (pageIndex - 1) + 1 //当页第一条
                    && runNum <= pageSize * pageIndex //当页最后一条
                    && runNum <= list.size()
            )
            m.put("rowNo",runNum);
            runNum ++;
        }
        return listResult;
    }


}
