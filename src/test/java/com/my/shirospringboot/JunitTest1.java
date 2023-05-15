package com.my.shirospringboot;

import org.junit.Test;

import java.time.ZonedDateTime;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/2/9
 */
public class JunitTest1 {

    @Test
    public void test1(){
        ZonedDateTime zdt = ZonedDateTime.now();
        System.out.println(zdt);
    }

}
