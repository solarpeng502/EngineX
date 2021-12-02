package com.baoying.enginex.executor;

import com.alibaba.fastjson.JSON;
import com.baoying.enginex.executor.util.ExecuteUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
    public static void main(String[] args) {
        Pattern pattern$ = Pattern.compile("\\$\\{[a-zA-Z0-9_\u4e00-\u9fa5()（）-]+\\}");
        Matcher matcher$ = pattern$.matcher("${aa}");

        while (matcher$.find()){
            System.out.println(matcher$.group());
        }
//        System.out.println("".matches("大abcd"));
//        Pattern pattern = Pattern.compile("(abcd)+");
//        Pattern pattern = Pattern.compile("(\\[(0|([1-9]([0-9])*))\\])+$");
//        Matcher matcher = pattern.matcher("addd[0][1][2][3][4][5]");
//
//        System.out.println(matcher.find());
//        System.out.println(matcher.group(0));
//        String fieldEnStr = "a.s.d.1.2.3.";
//        System.out.println(JSON.toJSONString(fieldEnStr.split("\\.")));
    }
    private static void testRegex(){
        String formula = "@1@2@3@4@5@6@7@";
        Pattern pattern = Pattern.compile("@[a-zA-Z0-9_\u4e00-\u9fa5()（）-]+@");
        Matcher matcher = pattern.matcher(formula);
        while (matcher.find()) {
            String fieldEn = matcher.group().replace("@", "");
            System.out.println(matcher.group());
        }
    }
}
