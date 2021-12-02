package com.baoying.enginex.executor.engine.controller;

import com.alibaba.fastjson.JSONObject;
import com.baoying.enginex.executor.engine.model.DecisionReqModel;
import com.baoying.enginex.executor.engine.service.EngineApiService;
import com.baoying.enginex.executor.engine.thread.EngineCallable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/QueryString")
public class ApiController  {

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    public EngineApiService engineApiService;

    @RequestMapping(value = "/decision", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String decision(String ts, String nonce, String act, String pid, String uid, String sign, String token, String paramJson, String fields) {
        logger.info("请求参数--" + "ts:" + ts + ",nonce:" + nonce + ",act:" + act + ",pid:" + pid + ",uid:" + uid + ", sign:" + sign + ",token:" + token + ",paramJson" + paramJson);
        Map<String, Object> map = new HashMap<>();
        map.put("ts", ts);
        map.put("nonce", nonce);
        map.put("act", act);
        map.put("pid", pid);
        map.put("uid", uid);
        map.put("token", token);
        JSONObject jsonObject = JSONObject.parseObject(paramJson);
        if (jsonObject.getInteger("reqType") == 2) {
            map.put("version", jsonObject.getInteger("version"));
            map.put("subversion", jsonObject.getInteger("subversion"));
        }
        map.put("reqType", jsonObject.getInteger("reqType"));
        map.put("engineId", jsonObject.getLong("engineId"));
        map.put("organId", jsonObject.getLong("organId"));
        map.put("sign", jsonObject.getString("sign"));

        Map<String, Object> requestFields = new HashMap<>();
        if(StringUtils.isNotBlank(fields)){
            requestFields = JSONObject.parseObject(fields, Map.class);
        }
        map.put("fields", requestFields);
        String result = engineApiService.engineApi(map);
        logger.info("uid:" + uid + " 响应参数--" + "result:" + result);
        return result;
    }

    @RequestMapping(value = "/batchDecision", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String batchDecision(HttpServletResponse response, String ts, String nonce, String act, String sign, String token, int reqType, Long engineId, Long organId, String paramJson) {
        List<JSONObject> resultList = new ArrayList<>();
        Map<String, Object> resultMap = new HashMap<>();

        List<Map<String, Object>> list = new ArrayList<>();
        List<DecisionReqModel> reqModelList = JSONObject.parseArray(paramJson, DecisionReqModel.class);
        for (DecisionReqModel reqModel : reqModelList) {
            Map<String, Object> map = new HashMap<>();
            map.put("ts", ts);
            map.put("nonce", nonce);
            map.put("act", act);
            map.put("token", token);
            map.put("reqType", reqType);
            map.put("engineId", engineId);
            map.put("organId", organId);
            map.put("sign", sign);
            map.put("pid", reqModel.getPid());
            map.put("uid", reqModel.getUid());

            Map<String, Object> requestFields = new HashMap<>();
            if(reqModel.getFields() != null){
                requestFields = JSONObject.parseObject(JSONObject.toJSONString(reqModel.getFields()), Map.class);
            }
            map.put("fields", requestFields);
            list.add(map);
        }

        List<Future<String>> futureList = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for(Map<String, Object> paramMap : list){
            futureList.add(executorService.submit(new EngineCallable(paramMap)));
        }

        // 获取线程执行结果
        for (final Future<String> future : futureList) {
            try {
                final String str = future.get(5, TimeUnit.MINUTES);
                resultList.add(JSONObject.parseObject(str));
            } catch (Exception e) {
                boolean cancelResult = future.cancel(true);
                logger.error("取消结果(" + cancelResult + ")" + e.getMessage(), e);
            }
        }

        String result = JSONObject.toJSONString(resultList);
        resultMap.put("result", resultList);
        logger.info(" 响应参数--" + "result:" + result);

        return result;
    }

}
