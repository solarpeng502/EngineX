package com.baoying.enginex.executor.engine.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baoying.enginex.executor.engine.enums.NodeTypeEnum;
import com.baoying.enginex.executor.engine.mapper.EngineResultSetMapper;
import com.baoying.enginex.executor.engine.model.Engine;
import com.baoying.enginex.executor.engine.model.EngineNode;
import com.baoying.enginex.executor.engine.model.EngineResultSet;
import com.baoying.enginex.executor.engine.model.EngineVersion;
import com.baoying.enginex.executor.engine.service.EngineApiService;
import com.baoying.enginex.executor.engine.service.EngineNodeService;
import com.baoying.enginex.executor.engine.service.EngineService;
import com.baoying.enginex.executor.engine.service.EngineVersionService;
import com.baoying.enginex.executor.node.service.impl.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EngineApiServiceImpl implements EngineApiService {

    private static final Logger logger = LoggerFactory.getLogger(EngineApiServiceImpl.class);

    @Autowired
    public EngineService engineService;

    @Resource
    public EngineVersionService engineVersionService;

    @Resource
    public EngineNodeService engineNodeService;

    @Resource
    public EngineResultSetMapper resultSetMapper;

    @Autowired
    private DecisionOptionsNode decisionOptionsNode;

    @Autowired
    private RuleSetNode ruleSetNode;

    @Autowired
    private GroupNode groupNode;

    @Autowired
    private SandboxProportionNode sandboxProportionNode;

    @Autowired
    private AsyncRestTemplate asyncRestTemplate;

    @Override
    public String engineApi(Map<String, Object> paramJson) {
        logger.info("请求参数，paramJson: {}", JSONObject.toJSONString(paramJson));
        JSONObject jsonObject = new JSONObject();
        JSONArray resultJson = new JSONArray();
        Map<String, Map<String,Object>> featureMaps = new ConcurrentHashMap<>();
        //时间差小于等于30分钟并且鉴权成功
        if (true){
            Long organId = Long.valueOf(paramJson.get("organId").toString());
            Long engineId = Long.valueOf(paramJson.get("engineId").toString());
            //获取引擎信息
            Engine engine = engineService.getEngineById(engineId);
            if(engine != null && !engine.getOrganId().equals(organId)){
                // todo 校验引擎是否为该组织所属
            }
            //获取引擎正在运行中的版本
            EngineVersion engineVersion = engineVersionService.getRunningVersion(engineId);
            if (engineVersion != null) {
                //返回引擎下的所有节点集合
                List<EngineNode> engineNodeList = engineNodeService.getEngineNodeListByVersionId(engineVersion.getVersionId());
                Map<String, EngineNode> engineNodeMap = getEngineNodeListByMap(engineNodeList);
                try {
                    //变量池
                    Map<String, Object> inputParam = new ConcurrentHashMap<>();
                    inputParam.putAll(JSONObject.parseObject(JSONObject.toJSONString(paramJson.get("fields")), Map.class));
                    EngineNode engineNode = engineNodeMap.get("ND_START");
                    if (null != engineNode && null != engineNode.getNextNodes()) {
                        //返回输出结果
                        Map<String, Object> outMap = new ConcurrentHashMap<>();
                        // 记录执行前全量指标
                        featureMaps.put("before",inputParam);
                        //节点执行方法
                        recursionEngineNode(inputParam, engineNodeMap.get(engineNode.getNextNodes()), engineNodeMap, outMap);
                        jsonObject.put("status", "0x0000");
                        jsonObject.put("msg", "执行成功");
                        if (outMap.containsKey("centens") && outMap.get("centens").equals("true")) {
                            jsonObject.put("status", "0x0006");
                            jsonObject.put("msg", "获取数据失败");
                            jsonObject.put("data", "");
                            return jsonObject.toString();
                        }
                        //记录执行后的全量指标
                        featureMaps.put("after",inputParam);
                        paramJson.put("versionId",engineNode.getVersionId());
                        String json = JSONObject.toJSONString(inputParam);
                        jsonObject.put("input", JSONObject.parseObject(json));

                        EngineResultSet resultSet = new EngineResultSet();
                        resultSet.setEngine_code(engine.getCode());
                        resultSet.setInput(json);
                        resultSet.setEngine_id(engine.getId());
                        resultSet.setEngine_name(engine.getName());
                        resultSet.setType(2);
                        resultSet.setSubVersion(engineVersion.getSubVersion());
                        resultSet.setUid(String.valueOf(paramJson.get("uid")));
                        resultSet.setPid(String.valueOf(paramJson.get("pid")));

                        //决策表最终结果
                        if (outMap.containsKey("decisionTables")){
                            jsonObject.put("decisionTablesResult", outMap.get("decisionTables").toString());
                            resultSet.setDecisionTablesResult(outMap.get("decisionTables").toString());
                        }
                        //决策树最终结果
                        if (outMap.containsKey("decisionTree")){
                            jsonObject.put("decisionTreeResult", outMap.get("decisionTree").toString());
                            resultSet.setDecisionTreeResult(outMap.get("decisionTree").toString());
                        }
                        // 节点终止输出
                        if (outMap.containsKey("result")) {
                            resultSet.setResult(outMap.get("result").toString());
                            //决策选项最终结果
                            jsonObject.put("result", outMap.get("result").toString());
                        }

                        if (outMap.containsKey("blackJson")) {
                            resultJson.add(new JSONObject().parse(outMap.get("blackJson").toString()));
                        }

                        if (outMap.containsKey("whiteJson")) {
                            resultJson.add(new JSONObject().parse(outMap.get("whiteJson").toString()));
                        }

                        if (outMap.containsKey("ruleJson")) {
                            //规则集节点输出
                            JSONObject ruleJson = new JSONObject();
                            ruleJson.put("resultType", 2);
                            ruleJson.put("resultJson", outMap.get("ruleJson"));
                            resultJson.add(ruleJson);
                        }

                        if (outMap.containsKey("scoreJson")) {
                            //评分卡输出
                            JSONObject ruleJson = new JSONObject();
                            ruleJson.put("resultType", 4);
                            ruleJson.put("resultJson", outMap.get("scoreJson"));
                            resultJson.add(ruleJson);
                        }

                        if (outMap.containsKey("decisionJson")) {
                            //决策选项输出
                            JSONObject ruleJson = new JSONObject();
                            ruleJson.put("resultType", 9);
                            ruleJson.put("resultJson", outMap.get("decisionJson"));
                            resultJson.add(ruleJson);
                        }

                        if (outMap.containsKey("childEngineJson")) {
                            //子引擎节点输出
                            JSONObject ruleJson = new JSONObject();
                            ruleJson.put("resultType", 14);
                            ruleJson.put("resultJson", outMap.get("childEngineJson"));
                            resultJson.add(ruleJson);
                        }

                        if (outMap.containsKey("modelJson")) {
                            //模型节点输出
                            JSONObject ruleJson = new JSONObject();
                            ruleJson.put("resultType", 15);
                            ruleJson.put("resultJson", outMap.get("modelJson"));
                            resultJson.add(ruleJson);
                        }

                        if (outMap.containsKey("decisionTablesJson")) {
                            //决策表输出
                            JSONObject ruleJson = new JSONObject();
                            ruleJson.put("resultType", 16);
                            ruleJson.put("resultJson", outMap.get("decisionTablesJson"));
                            resultJson.add(ruleJson);
                        }

                        if (outMap.containsKey("decisionTreeJson")) {
                            //决策树输出
                            JSONObject ruleJson = new JSONObject();
                            ruleJson.put("resultType", 17);
                            ruleJson.put("resultJson", outMap.get("decisionTreeJson"));
                            resultJson.add(ruleJson);
                        }

                        jsonObject.put("data", resultJson);
                        String result = JSONObject.toJSONString(jsonObject);

                        JSONObject tmpJsonObject = JSONObject.parseObject(result);
                        tmpJsonObject.remove("input");
                        resultSet.setOutput(JSONObject.toJSONString(tmpJsonObject));
                        resultSetMapper.insertResultSet(resultSet);
                        Integer resultId = resultSet.getId();
                        // 正常返回结果回调
                        decisionCallback(engine.getCallbackUrl(), paramJson, result);
                    }
                } catch (Exception e) {
                    logger.error("接口请求异常", e);
                    jsonObject.put("status", "0x0005");
                    jsonObject.put("msg", "执行失败");
                    jsonObject.put("data", "");
                    // 异常回调
                    decisionCallback(engine.getCallbackUrl(), paramJson, "执行失败");
                }
            } else {
                jsonObject.put("status", "0x0004");
                jsonObject.put("msg", "请求引擎不存在或尚未部署运行");
                jsonObject.put("data", "");
            }
        } else {
            jsonObject.put("status", "0x0001");
            jsonObject.put("msg", "鉴权失败，非法调用");
            jsonObject.put("data", "");
        }

        return jsonObject.toString();
    }


    /**
     * 递归执行节点
     * @param inputParam
     * @param engineNode
     * @param engineNodeMap
     * @param outMap
     */
    private EngineNode recursionEngineNode(Map<String, Object> inputParam, EngineNode engineNode, Map<String, EngineNode> engineNodeMap, Map<String, Object> outMap) {
        logger.info("请求参数--" + "inputParam:" + JSONObject.toJSONString(inputParam));

        EngineNode resultNode = null; // 结束时返回节点: 串行流程返回null、并行流程返回聚合节点

        if(engineNode == null){
            return null;
        }

        // 获取节点所需的指标
        getNodeField(engineNode, inputParam);
        // 执行节点逻辑
        runNode(engineNode, inputParam, outMap);

        //用于存储执行过的节点
        List<String> executedNodeList = new ArrayList<>();
        if(outMap.containsKey("executedNodes")){
            executedNodeList =(List<String>) outMap.get("executedNodes");
        }
        executedNodeList.add(engineNode.getNodeId()+"");
        // 更新执行过节点数组
        outMap.put("executedNodes",executedNodeList);
        // 递归执行下一个节点
        if (StringUtils.isNotBlank(engineNode.getNextNodes())) {
                // 串行节点执行
                EngineNode nextEngineNode = engineNodeMap.get(engineNode.getNextNodes());
                //如果输出的map里面有nextNode，则说明有分组，需要走分组下面的节点
                if (outMap.containsKey("nextNode")) {
                    nextEngineNode = engineNodeMap.get(outMap.get("nextNode"));
                    outMap.remove("nextNode");
                }

                if(nextEngineNode!=null&&nextEngineNode.getNodeType() == NodeTypeEnum.AGGREGATION.getValue()){
                    // 并行节点后面的分支为多线程执行，执行到聚合节点则结束
                    resultNode = nextEngineNode;
                } else {
                    resultNode = recursionEngineNode(inputParam, nextEngineNode, engineNodeMap, outMap);
                }
        }

        return resultNode;
    }



    /**
     * 获取节点所需的指标
     * @param engineNode
     * @param inputParam
     */
    private void getNodeField(EngineNode engineNode, Map<String, Object> inputParam) {
        switch (engineNode.getNodeType()) {
            case 2:
                //规则
                ruleSetNode.getNodeField(engineNode, inputParam);
                break;
            case 3:
                //分组
                groupNode.getNodeField(engineNode, inputParam);
                break;
            case 9:
                //决策选项
                decisionOptionsNode.getNodeField(engineNode, inputParam);
                break;
            default:
                break;
        }
    }

    /**
     * 执行节点逻辑
     * @param engineNode
     * @param inputParam
     * @param outMap
     */
    private void runNode(EngineNode engineNode, Map<String, Object> inputParam, Map<String, Object> outMap) {
        switch (engineNode.getNodeType()) {
            case 2:
                //规则
                ruleSetNode.runNode(engineNode, inputParam, outMap);
                break;
            case 3:
                //分组
                groupNode.runNode(engineNode, inputParam, outMap);
                break;
            case 7:
                //沙盒比例
                sandboxProportionNode.runNode(engineNode, inputParam, outMap);
                break;
            case 9:
                //决策选项
                decisionOptionsNode.runNode(engineNode, inputParam, outMap);
                break;
            default:
                break;
        }
    }

    /**
     * 把引擎节点，以序号为key放入map
     *
     * @param nodelist 引擎节点
     * @return map
     * @see
     */
    private Map<String, EngineNode> getEngineNodeListByMap(List<EngineNode> nodelist) {
        Map<String, EngineNode> map = new HashMap<>();
        for (int i = 0; i < nodelist.size(); i++) {
            map.put(nodelist.get(i).getNodeCode(), nodelist.get(i));
        }
        return map;
    }

    /**
     * 决策流执行完回调（包括决策流正常返回结果回调、以及异常回调）
     * @param url
     * @param paramJson
     * @param result
     */
    private void decisionCallback(String url, Map<String, Object> paramJson, String result){
        if(StringUtils.isBlank(url)){
            return;
        }
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("paramJson", JSONObject.toJSONString(paramJson));
        paramMap.put("result", result);
        // 设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        // 封装请求体
        JSONObject body = JSONObject.parseObject(JSONObject.toJSONString(paramMap));
        // 封装参数和头信息
        HttpEntity<JSONObject> httpEntity = new HttpEntity(body, httpHeaders);
        ListenableFuture<ResponseEntity<String>> future = asyncRestTemplate.postForEntity(url, httpEntity, String.class);
        if(future != null){
            future.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    logger.info("引擎回调异步调用失败", throwable);
                }

                @Override
                public void onSuccess(ResponseEntity<String> stringResponseEntity) {
                    String result = stringResponseEntity.getBody();
                    logger.info("引擎回调异步调用成功，result:{}", result);
                }
            });
        }
    }

}
