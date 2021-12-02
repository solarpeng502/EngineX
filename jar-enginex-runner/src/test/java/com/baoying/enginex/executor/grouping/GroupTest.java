package com.baoying.enginex.executor.grouping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.platform.commons.util.StringUtils;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GroupTest {
    public static void main(String[] args) {
//        test2();
//        test3();
//        test1();
        test4();

    }
    public static void test1(){
        List<User> list = new ArrayList();
        for (int i = 0; i < 100; i++) {
            list.add(new User(i,"用户 ： "+i,i%10+18,new Role((int)(Math.random()*10+1),"角色 ： "+i%10,new Auth((int)(Math.random()*10+1),"权限 ： "+i%20))));
        }
        Map<String, List<User>> result = new HashMap<>();

        Map<Integer, List<User>> collect = list.stream().collect(Collectors.groupingBy(user -> user.getAge()));
        for (Map.Entry<Integer, List<User>> entry : collect.entrySet()) {
            Integer key = entry.getKey();
            Map<Integer, List<User>> collect1 = entry.getValue().stream().collect(Collectors.groupingByConcurrent(user -> user.getRole().getId()));
            for (Map.Entry<Integer, List<User>> childEntry : collect1.entrySet()) {
                result.put(""+key+":"+childEntry.getKey(),childEntry.getValue());
            }
        }
//        System.out.println(collect);
    }
//    public static void handlerGroup(Map<String, List<JSONObject>> param,List<String> keys){
//        Map<String, List<JSONObject>> result = new HashMap<>();
//        for (Map.Entry<String, List<JSONObject>> listEntry : param.entrySet()) {
//            //前一次分组时的key
//            String parentKey = listEntry.getKey();
//            //第一次分组完成的一个数组
//            List<JSONObject> list = listEntry.getValue();
//            //默认key为父级key
//            String resultKey = parentKey;
//
//            for (String key : keys) {
//                Map<String, List<JSONObject>> collect = list.stream().collect(Collectors.groupingBy(json -> json.get(key).toString()));
//                for (Map.Entry<String, List<JSONObject>> childEntry : collect.entrySet()) {
//                    resultKey += (":"+ childEntry.getKey());
//                    result.put(resultKey,childEntry.getValue());
//                }
//
//            }
//        }
//    }


    public static void test2(){
        List<Map<String,Object>> list = new ArrayList();
        Map map = null;
        for (int i = 0; i < 100; i++) {
            map = new HashMap();
            map.put("id",i);
            map.put("name","用户 ： "+i);
            map.put("age",i%10);
            list.add(map);
        }
        Map<Object, List<Map<String, Object>>> collect = list.stream().collect(Collectors.groupingBy(item -> item.get("age")));

        for (Map.Entry<Object, List<Map<String, Object>>> entry : collect.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
            System.out.println(entry.getValue().size());
        }
    }

    public static void test3(){
        List<Map<String,Object>> list = new ArrayList();
        JSONObject map = null;
        for (int i = 0; i < 100; i++) {
            map = new JSONObject();
            map.put("id",i);
            map.put("name","用户 ： "+i);
            map.put("age",i%10);
            list.add(map);
        }
        Map<Object, List<Map<String, Object>>> collect = list.stream().collect(Collectors.groupingBy(item -> item.get("age")));

        for (Map.Entry<Object, List<Map<String, Object>>> entry : collect.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
            System.out.println(entry.getValue().size());
        }

    }

    public static void test4(){
        List<JSONObject> list = new ArrayList();
        JSONObject map = null;
        List<Auth> authList;
        for (int i = 0; i < 10000; i++) {
            map = new JSONObject();
            map.put("id",i%5);
            map.put("name","用户"+i%3);
            map.put("age",i%13);
            authList = new ArrayList<>();
            authList.add(new Auth(i%7,"权限"+i%7));
            map.put("role",new Role(i%11,"角色"+i%7,null,authList));

            list.add(map);
        }

//        Map<String, List<JSONObject>> stringListMap = handlerGroup(list, Arrays.asList("age", "name", "id","role.id"));
        Map<String, List<JSONObject>> stringListMap = recursionGroup(list, Arrays.asList("age", "name", "role.name","role.authList[0].name"));
        System.out.println(stringListMap);
    }

//    public static Map<String,List<JSONObject>> handlerGroup(List<JSONObject> param, List<String> keys ){
//        //没有给分组的key则直接返回
//        if (keys==null||keys.isEmpty()){
//            return null;
//        }
//        Map<String,List<JSONObject>> result = recursionGroup(param, keys,0,"");
//        return result;
//    }
//
//    public static Map<String,List<JSONObject>> recursionGroup(List<JSONObject> param,List<String> keys,int index,String parentKey){
//
//        if (StringUtils.isNotBlank(parentKey)){
//            parentKey+=":";
//        }
//        final String preKey = parentKey;
//        Map<String, List<JSONObject>> result = new HashMap<>();
//
//        Map<String, List<JSONObject>> temp = new HashMap<>();
//        if (keys.get(index)!=null){
//            //做本次分组
//            Map<String, List<JSONObject>> collect = param.stream().collect(Collectors.groupingBy(json -> {
//                String[] split = keys.get(index).split("\\.");
//                if (split.length>1){
//                    JSONObject jsonObject = json;
//                    for (int i = 0; i < split.length; i++) {
//                        if (i<split.length-1){
//                            jsonObject = jsonObject.getJSONObject(split[i]);
//                        }else {
//                            return jsonObject.get(split[i]).toString();
//                        }
//                    }
//                }
//                return json.get(split[0]).toString();
//
//            }));
//            //分组完成还需要分组
//            if (keys.size()>index+1){
//                //执行分组
//                for (Map.Entry<String, List<JSONObject>> entry : collect.entrySet()) {
//                    temp.putAll(recursionGroup(entry.getValue(),keys,index+1,entry.getKey()));
//                }
//            }else {
//                temp = collect;
//            }
//        }else {
//            //没有分组的key无法执行
//        }
//        result = temp.entrySet().stream().collect(Collectors.toMap(item->preKey+item.getKey(),item->item.getValue()));
//        return result;
//    }

    public static Map<String,List<JSONObject>> recursionGroup(List<JSONObject> param,List<String> keys){
        return param.stream().collect(Collectors.groupingBy(item->{
            String cond = "";
            for (String key : keys) {
                if (StringUtils.isNotBlank(cond)){
                    cond+=":";
                }
                String[] split = key.split("\\.");
                if (split.length>1){
                    JSONObject jsonObject = item;
                    for (int i = 0; i < split.length; i++) {
                        if (i<split.length-1){
//                            jsonObject = jsonObject.getJSONObject(split[i]);
                            jsonObject = handlerToObject(jsonObject,split[i]);
                        }else {
//                            cond += jsonObject.get(split[i]).toString();
                            cond += handlerToString(jsonObject,split[i]);
                        }
                    }
                }else {
//                    cond += item.get(split[0]).toString();
                    cond += handlerToString(item,split[0]);
                }
            }
            return cond;
        }));
//        return null;
    }
    private static JSONObject handlerToObject(JSONObject preObject,String key){
        Pattern pattern = Pattern.compile("(\\[(0|([1-9]([0-9])*))\\])+$");
        Matcher matcher = pattern.matcher(key);
        JSONObject result = null;
        if (matcher.find()){
            //是数组
            String group = matcher.group();
            JSONArray jsonArray = preObject.getJSONArray(key.replace(group, ""));
            String[] split = group.replace("[", "").split("]");
            int length = split.length;
            for (int i = 0; i <length ; i++) {
                String indexStr = split[i];
                if (i<length-1){
                    jsonArray = jsonArray.getJSONArray(Integer.valueOf(indexStr));
                }else {
                    result = jsonArray.getJSONObject(Integer.valueOf(indexStr));
                }
            }
        }else {
            //不是数组
            result = preObject.getJSONObject(key);
        }

        return result;
    }
    private static String handlerToString(JSONObject preObject,String key){
        Pattern pattern = Pattern.compile("(\\[(0|([1-9]([0-9])*))\\])+$");
        Matcher matcher = pattern.matcher(key);
        String result = null;
        if (matcher.find()){
            //是数组
            String group = matcher.group();
            JSONArray jsonArray = preObject.getJSONArray(key.replace(group, ""));
            String[] split = group.replace("[", "").split("]");
            int length = split.length;
            for (int i = 0; i <length ; i++) {
                String indexStr = split[i];
                if (i<length-1){
                    jsonArray = jsonArray.getJSONArray(Integer.valueOf(indexStr));
                }else {
                    result = jsonArray.get(Integer.valueOf(indexStr)).toString();
                }
            }
        }else {
            //不是数组
            result = preObject.get(key).toString();
        }
        return result;
    }

}
