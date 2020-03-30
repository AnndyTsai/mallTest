package com.Demo.Listeners.Assert;
/**
 * 重构TestNG断言方法
 * */

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

public class Assertion {

    public static boolean flag = true;

    public static List<Error> errors = new ArrayList<Error>();
    /**
     * 断言实际与预期是否相等
     * */
    public static void verifyEquals(Object actual, Object expected){
        try{
            Assert.assertEquals(actual, expected);
        }catch(Error e){

            errors.add(e);
            flag = false;
        }
    }
    /**
     * 断言实际与预期是否相等 断言失败并输出message信息
     * */
    public static void verifyEquals(Object actual, Object expected, String message){
        try{
            Assert.assertEquals(actual, expected, message);

        }catch(Error e){
           errors.add(e);
            flag = false;
        }
    }
    /**
     * 断言实际与预期是否为null
     * */
    public static void verifyNulls(boolean actual, boolean expected){

        try{

            Assert.assertEquals(actual, expected);

        }catch(Error e){
           errors.add(e);
            flag = false;
        }
    }
    /**
     * 断言实际与预期是否为null，断言失败 输出message信息
     * */
    public static void verifyNulls(boolean actual, boolean expected , String msg){

        try{

            Assert.assertEquals(actual, expected, msg);

        }catch(Error e){

           errors.add(e);
            flag = false;
        }
    }

    /**
     * 断言实际String是否包含与预期String
     * */
    public static void verifyActualCotainers(String actual, String expected){

        boolean tag = false;

        if(expected.contains(actual)){

            tag = true;
        }

        try{

            Assert.assertEquals(tag, true);

        }catch(Error e){

           errors.add(e);
            flag = false;
        }
    }
    /**
     * 断言实际String是否包含与预期String
     * */
    public static void verifyExpectedCotainers(String actual, String expected){

        boolean tag = false;

        if(actual.contains(expected)){

            tag = true;
        }
        try{

            Assert.assertEquals(true, tag);

        }catch(Error e){

           errors.add(e);
            flag = false;
        }
    }
}
