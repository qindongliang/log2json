package net.test;

import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;

/**
 * Created by QinDongLiang on 2017/9/13.
 */
public class JsonTest {


    static Logger log =Logger.getLogger(JsonTest.class);


    @Test
    public void testBasicLog(){


        log.info("测试信息");


    }

    @Test
    public void testCustomFiled(){

        HashMap<String,Object> hm=new HashMap<String, Object>();
        hm.put("cost",124);
        hm.put("description","测试自定义的字段");

        HashMap<String,Object> hm2=new HashMap<String, Object>();

        hm2.put("data",hm);

        log.info(hm2);
    }


    @Test
    public void exceptionLogTest(){

        try {
            String check = null;
            System.out.println(check.length());
        }catch (Exception e){
            HashMap<String,Object> emp=new HashMap<String, Object>();
            emp.put("message","发生异常");
            emp.put("trace_id",100025);

            log.error(emp,e);
        }

    }






}
