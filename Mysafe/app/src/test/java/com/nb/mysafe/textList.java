package com.nb.mysafe;

import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * @author 侯紫睿
 * @time 2016/5/3 0003  下午 5:05
 * @desc ${TODD}
 */
public class textList extends TestCase {
   
    public void test(){
        ArrayList<String> list = new  ArrayList<String>();
        list.add("hello1");
        list.add("hello2");
        list.add(0,"hello3");
        for (String l : list) {
            System.out.print(l);
        }
    }
}
