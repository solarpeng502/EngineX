package com.baoying.enginex.executor.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ListOpUtils {
    public static Map<String,List<JSONObject>> recursionGroup(List<JSONObject> param, List<String> keys){
        return param.stream().collect(Collectors.groupingBy(item->{
            String cond = "";
            for (String key : keys) {
                if (StringUtils.isNotBlank(cond)){
                    cond+="_";
                }
                cond += getObjByKeyAndJson(item,key);
//                String[] split = key.split("\\.");
//                if (split.length>1){
//                    JSONObject jsonObject = item;
//                    for (int i = 0; i < split.length; i++) {
//                        if (i<split.length-1){
//                            jsonObject = handlerToJson(jsonObject,split[i]);
//                        }else {
//                            cond += handlerToObj(jsonObject,split[i]).toString();
//                        }
//                    }
//                }else {
//                    cond += handlerToObj(item,split[0]).toString();
//
//                }
            }
            return cond;
        }));
    }
    public static JSONObject handlerToJson(JSONObject preObject, String key){
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
    public static Object handlerToObj(JSONObject preObject, String key){
        Pattern pattern = Pattern.compile("(\\[(0|([1-9]([0-9])*))\\])+$");
        Matcher matcher = pattern.matcher(key);
        Object result = null;
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
                    result = jsonArray.get(Integer.valueOf(indexStr));
                }
            }
        }else {
            //不是数组
            result = preObject.get(key);
        }
        return result;
    }
    public static Object getObjByKeyAndJson(JSONObject preObject, String key){
        if (StringUtils.isBlank(key)){
            return preObject;
        }
        Object o = "";
        String[] split = key.split("\\.");
        int length = split.length;
        JSONObject temp = preObject;
        for (int i = 0; i <length ; i++) {
            if (i<length-1){
                temp = ListOpUtils.handlerToJson(temp,split[i]);
            }else {
                o = ListOpUtils.handlerToObj(temp,split[i]);
            }
        }
        return o;
    }
}
