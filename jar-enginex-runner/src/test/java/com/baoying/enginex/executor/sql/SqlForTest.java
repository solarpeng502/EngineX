package com.baoying.enginex.executor.sql;

import com.alibaba.fastjson.JSON;
import com.baoying.enginex.executor.datamanage.mapper.SimpleMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@SpringBootTest
//@RunWith(SpringRunner.class)
public class SqlForTest {

    @Autowired
    private SimpleMapper simpleMapper;
    @Test
    public void testForSql(){

        String sqlStr = " select * from  t_list_db " +
                " where  id in " +
                " <foreach collection=\"list\" item=\"item\" separator=\",\" open=\"(\" close=\")\">\n" +
                " #{item}\n" +
                " </foreach>";
        Map<String,Object>  param = new HashMap<>();
        param.put("sqlStr",sqlStr);
        List<Long> ids = new ArrayList<>();
        ids.add(112L);
        ids.add(114L);
        ids.add(115L);
        param.put("list",ids);
        List<LinkedHashMap<String, Object>> test = simpleMapper.test(param);

        System.out.println(JSON.toJSONString(test));

    }

    public static void main(String[] args) {
        String sqlStr = " select * from  t_list_db " +
                " where  id in    ( #{aaaa}   )";
        Pattern sqlnPattern = Pattern.compile("\\s*in\\s*\\(\\s*#\\{([a-zA-Z0-9_\u4e00-\u9fa5()（）-]+)\\}\\s*\\)");
        Matcher matcher = sqlnPattern.matcher(sqlStr);
       if ( matcher.find()){
           System.out.println(matcher.group(0));
       }
    }
}
