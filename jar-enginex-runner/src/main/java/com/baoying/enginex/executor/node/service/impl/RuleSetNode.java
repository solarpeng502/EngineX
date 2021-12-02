package com.baoying.enginex.executor.node.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baoying.enginex.executor.common.constants.CommonConst;
import com.baoying.enginex.executor.common.constants.Constants;
import com.baoying.enginex.executor.common.ksession.KSessionPool;
import com.baoying.enginex.executor.common.model.ExpressionParam;
import com.baoying.enginex.executor.datamanage.model.Field;
import com.baoying.enginex.executor.datamanage.service.FieldService;
import com.baoying.enginex.executor.engine.model.EngineNode;
import com.baoying.enginex.executor.engine.model.InputParam;
import com.baoying.enginex.executor.engine.model.Result;
import com.baoying.enginex.executor.node.service.CommonService;
import com.baoying.enginex.executor.node.service.EngineNodeService;
import com.baoying.enginex.executor.redis.RedisManager;
import com.baoying.enginex.executor.rule.consts.RuleConst;
import com.baoying.enginex.executor.rule.model.RuleInfo;
import com.baoying.enginex.executor.rule.model.RuleLoopGroupAction;
import com.baoying.enginex.executor.rule.model.RuleScriptVersion;
import com.baoying.enginex.executor.rule.model.vo.RuleConditionVo;
import com.baoying.enginex.executor.rule.model.vo.RuleVersionVo;
import com.baoying.enginex.executor.rule.service.*;
import com.baoying.enginex.executor.tactics.consts.TacticsType;
import com.baoying.enginex.executor.util.ExecuteUtils;
import com.baoying.enginex.executor.util.JevalUtil;
import com.baoying.enginex.executor.util.MD5;
import com.baoying.enginex.executor.util.jeval.EvaluationException;
import org.apache.commons.lang3.StringUtils;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class RuleSetNode implements EngineNodeService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommonService commonService;
    @Autowired
    private RuleFieldInfoService ruleFieldInfoService;
    @Resource
    private RuleService ruleService;
    @Autowired
    private RuleConditionService conditionService;
    @Resource
    private RuleScriptVersionService ruleScriptVersionService;
    @Resource
    private FieldService fieldService;
    @Autowired
    private RuleVersionService versionService;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private KSessionPool kSessionPool;
    @Autowired
    private RedisManager redisManager;

    @Override
    public void getNodeField(EngineNode engineNode, Map<String, Object> inputParam) {
        logger.info("start【获取规则集节点指标】RuleSetNode.getNodeField engineNode:{},inputParam:{}", JSONObject.toJSONString(engineNode), JSONObject.toJSONString(inputParam));
        JSONObject nodeJson = JSONObject.parseObject(engineNode.getNodeJson());
        List<Long> ids = new ArrayList<>();
        List<Long> ruleIds = new ArrayList<>(); // 基础规则集
        List<Long> versionIds = new ArrayList<>(); // 复杂规则集
        List<Long> scriptVersionIds = new ArrayList<>(); // 脚本规则集

        JSONArray jsonArray = null;
        int groupType = nodeJson.getInteger("groupType");
        if (groupType == Constants.ruleNode.MUTEXGROUP) {
            jsonArray = nodeJson.getJSONObject("mutexGroup").getJSONArray("rules");
        } else {
            jsonArray = nodeJson.getJSONObject("executeGroup").getJSONArray("rules");
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject ruleObj = jsonArray.getJSONObject(i);
            Long versionId = ruleObj.getLong("ruleVersionId");
            Long ruleId = ruleObj.getLong("id");
            Long difficulty = ruleObj.getLong("difficulty");
            if (difficulty != null && difficulty == 3) {
                scriptVersionIds.add(versionId); // 脚本式规则
            } else if (versionId != null) {
                versionIds.add(versionId); // 复杂规则
            } else if (ruleId != null) {
                ruleIds.add(ruleId); // 简单规则
            }
        }

        //获取字段en
        List<String> fieldEnList = new ArrayList<>();
        if (!ruleIds.isEmpty()) {
            fieldEnList.addAll(ruleFieldInfoService.getFieldEnList(ruleIds));
        }
        if (!versionIds.isEmpty()) {
            fieldEnList.addAll(conditionService.queryFieldEnByVersionIds(versionIds));
        }
        if (!scriptVersionIds.isEmpty()) {
            fieldEnList.addAll(ruleScriptVersionService.queryFieldEnByVersionIds(scriptVersionIds));
        }

        //筛选调那些循环或者嵌套中的字段
        fieldEnList = fieldEnList.stream().distinct().filter(f -> f != null && !f.contains(".") && !f.contains("%")).collect(Collectors.toList());
        if (fieldEnList != null && !fieldEnList.isEmpty()) {
            List<Field> fieldList = fieldService.selectFieldListByEns(fieldEnList);
            for (Field field : fieldList) {
                ids.add(field.getId());
            }
        }

        if (!ids.isEmpty()) {
            commonService.getFieldByIds(ids, inputParam);
        }
    }

    @Override
    public void runNode(EngineNode engineNode, Map<String, Object> inputParam, Map<String, Object> outMap) {
        JSONObject nodeJson = JSONObject.parseObject(engineNode.getNodeJson());
        //监控中心--记录节点快照信息
        if (engineNode != null && engineNode.getSnapshot() != null) {
            outMap.put("nodeSnapshot", engineNode.getSnapshot());
        }
        JSONObject nodeInfo = new JSONObject();
        nodeInfo.put("engineNode", engineNode);
        nodeInfo.put("nodeId", engineNode.getNodeId());
        nodeInfo.put("nodeName", engineNode.getNodeName());
        nodeInfo.put("nodeType", engineNode.getNodeType());
        outMap.put("nodeInfo", nodeInfo);
        int groupType = nodeJson.getInteger("groupType") == null ? Constants.ruleNode.EXECUTEGROUP : nodeJson.getInteger("groupType");
        CopyOnWriteArrayList<Map> ruleResultList = new CopyOnWriteArrayList<>();// 规则执行结果集合
        List<RuleInfo> ruleHitList = new ArrayList<>(); // 命中的规则集合

        // 互斥组(串行)
        if (groupType == Constants.ruleNode.MUTEXGROUP) {
            JSONArray jsonArray = nodeJson.getJSONObject("mutexGroup").getJSONArray("rules");
            List<RuleInfo> ruleInfoList = getRuleFromJsonArray(jsonArray);
            //监控中心--循环获取策略层面的快照信息
            recordStrategySnopshot(ruleInfoList, outMap);
            ruleHitList = serialRule(inputParam, outMap, ruleInfoList, ruleResultList);
        }
        // 执行组(并行)
        else if (groupType == Constants.ruleNode.EXECUTEGROUP) {
            JSONArray jsonArray = nodeJson.getJSONObject("executeGroup").getJSONArray("rules");
            List<RuleInfo> ruleInfoList = getRuleFromJsonArray(jsonArray);
            //监控中心--循环获取策略层面的快照信息
            recordStrategySnopshot(ruleInfoList, outMap);
            ruleHitList = parallelRule(inputParam, outMap, ruleInfoList, ruleResultList);
        }

        // 终止条件处理
        terminalCondition(engineNode, nodeJson, outMap, ruleHitList);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nodeId", engineNode.getNodeId());
        jsonObject.put("nodeName", engineNode.getNodeName());
        jsonObject.put("ruleResultList", ruleResultList);

        if (outMap.containsKey("ruleJson")) {
            JSONArray resultJson = (JSONArray) outMap.get("ruleJson");
            resultJson.add(jsonObject);
        } else {
            JSONArray resultJson = new JSONArray();
            resultJson.add(jsonObject);
            outMap.put("ruleJson", resultJson);
        }
        int hitSize = 0;
        double scoreSum = 0d;
        for (Map map : ruleResultList) {
            Object ruleScore = map.get("ruleScore");
            Object ruleResult = map.get("ruleResult");
            if (null != ruleResult && "命中".equals(ruleResult)) {
                hitSize++;
                if (null != ruleScore) {
                    try {
                        scoreSum += Double.valueOf(ruleScore.toString());
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        }
        String hitKey = "" + engineNode.getNodeType() + "_" + engineNode.getNodeId() + "_size";
        String scoreKey = "" + engineNode.getNodeType() + "_" + engineNode.getNodeId() + "_score";
        inputParam.put(hitKey, hitSize);
        inputParam.put(scoreKey, scoreSum);
        //监控中心==》记录节点输出结果
        //记录整个规则集中的所有规则的命中情况,以及总的统计次数 放到输出变量池
        JSONObject nodeResult = new JSONObject();
        nodeResult.put("ruleResultList", ruleResultList);
        nodeResult.put("hitNum", hitSize);
        nodeResult.put("scoreTotal", scoreSum);
        outMap.put("nodeResult", nodeResult);
    }

    /**
     * 监控中心--获取策略层面快照信息
     *
     * @param ruleInfoList
     * @param outMap
     */
    private void recordStrategySnopshot(List<RuleInfo> ruleInfoList, Map<String, Object> outMap) {
        JSONArray jsonObject = new JSONArray();
        ruleInfoList.stream().forEach(ruleInfo -> {
            if (ruleInfo.getVersion().getSnapshot() != null) {
                jsonObject.add(ruleInfo.getVersion().getSnapshot());

            }
        });
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("snopshot", jsonObject);
        outMap.put("strategySnopshot", jsonObject1);
    }

    /**
     * 串行执行规则
     *
     * @param inputParam
     * @param outMap
     * @param ruleInfoList
     * @param ruleResultList
     * @return
     */
    private List<RuleInfo> serialRule(Map<String, Object> inputParam, Map<String, Object> outMap, List<RuleInfo> ruleInfoList, CopyOnWriteArrayList<Map> ruleResultList) {
        logger.info("请求参数--串行执行规则" + "map:" + JSONObject.toJSONString(inputParam));
        List<RuleInfo> resultList = new ArrayList<>();
        for (int i = 0; i < ruleInfoList.size(); i++) {
            RuleInfo rule = ruleInfoList.get(i);
            boolean hitFlag = executeByDifficulty(inputParam, outMap, rule, ruleResultList);
            if (hitFlag) {
                resultList.add(rule);
                break;
            }
        }
        return resultList;
    }


    /**
     * 并行执行规则
     *
     * @param inputParam
     * @param outMap
     * @param ruleInfoList
     * @param ruleResultList
     * @return
     */
    private List<RuleInfo> parallelRule(Map<String, Object> inputParam, Map<String, Object> outMap, List<RuleInfo> ruleInfoList, CopyOnWriteArrayList<Map> ruleResultList) {
        logger.info("请求参数--并行执行规则" + "map:" + JSONObject.toJSONString(inputParam));
        List<RuleInfo> resultList = new ArrayList<>();
        List<CompletableFuture<RuleInfo>> futureList = new ArrayList<>();
        for (int i = 0; i < ruleInfoList.size(); i++) {
            final int index = i;
            CompletableFuture<RuleInfo> future = CompletableFuture.supplyAsync(() -> {
                RuleInfo rule = ruleInfoList.get(index);
                boolean hitFlag = executeByDifficulty(inputParam, outMap, rule, ruleResultList);
                if (hitFlag) {
                    return rule;
                } else {
                    return null;
                }
            }, threadPoolTaskExecutor);

            futureList.add(future);
        }

        for (CompletableFuture<RuleInfo> future : futureList) {
            try {
                RuleInfo rule = future.get();
                if (rule != null) {
                    resultList.add(rule);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        return resultList;
    }

    /**
     * 根究规则类型选择执行
     *
     * @param inputParam
     * @param outMap
     * @param rule
     * @param ruleResultList
     * @return
     */
    private boolean executeByDifficulty(Map<String, Object> inputParam, Map<String, Object> outMap, RuleInfo rule, CopyOnWriteArrayList<Map> ruleResultList) {
        boolean hitFlag = false;
        if (rule.getDifficulty() == 1) {
            hitFlag = executeBasicRule(inputParam, outMap, rule, ruleResultList);
        } else if (rule.getDifficulty() == 2) {
            hitFlag = executeComplexRule(inputParam, outMap, rule, ruleResultList);
        } else if (rule.getDifficulty() == 3) {
            hitFlag = executeScriptRule(inputParam, outMap, rule, ruleResultList);
        }
        return hitFlag;
    }

    /**
     * 执行复杂规则
     *
     * @param input
     * @param output
     * @param rule
     * @param ruleResultList
     * @return
     */
    public boolean executeComplexRule(Map<String, Object> input, Map<String, Object> output, RuleInfo rule, CopyOnWriteArrayList<Map> ruleResultList) {
        boolean hitFlag = false;
        //获取需要执行的整个规则。
//        RuleVo rule = ruleService.queryByVersionId(ruleId);
//        Long versionId = rule.getVersionId();
//        if (versionId==null){
//            return false;
//        }
//        RuleVersionVo ruleVersion = versionService.queryByVersionId(versionId);
        RuleVersionVo ruleVersion = rule.getVersion();
        if (ruleVersion == null) {
            return false;
        }

        //取出本规则的条件列表
        Map<String, Object> ruleMap = new HashMap<>();
        ruleMap.put("ruleId", rule.getId());
        ruleMap.put("ruleVersionId",ruleVersion.getId());
        ruleMap.put("ruleCode", rule.getCode());
        ruleMap.put("ruleName", rule.getName());
        ruleMap.put("versionCode", ruleVersion.getVersionCode());
        ruleMap.put("versionDesc", ruleVersion.getDescription());
        ruleMap.put("desc", rule.getDescription());
        ruleMap.put("ruleResult", "未命中");

        //获取规则需要执行的condition逻辑。
        RuleConditionVo ruleCondition = ruleVersion.getRuleConditionVo();
        //传入输入参数、中间变量、输出参数和需要执行的condition逻辑获取执行结果
        Map<String, Object> temp = JSON.parseObject(JSON.toJSONString(input), Map.class);
        boolean result = this.executeRuleCondition(temp, output, ruleCondition);
        String resultFieldEn = ruleVersion.getResultFieldEn();
        if (resultFieldEn == null || "".equals(resultFieldEn)) {
            resultFieldEn = "rule_2_"+rule.getId()+"_"+ruleVersion.getId()+"_hitResult";
        }
        String scoreFieldEn = ruleVersion.getScoreFieldEn();
        if (StringUtils.isNotBlank(scoreFieldEn)){
            scoreFieldEn = "rule_2_"+rule.getId()+"_"+ruleVersion.getId()+"_score";
        }
        input.put(resultFieldEn, "未命中");
        //根据执行的最终结果处理此规则输出内容
        List<JSONObject> fieldList = new ArrayList<>();
        JSONObject resultJson = new JSONObject();
        if (result) {
            ruleMap.put("ruleResult", "命中");
            ruleMap.put("ruleScore", rule.getScore());
            JSONObject scoreJson = new JSONObject();
            resultJson.put(resultFieldEn, "命中");
            fieldList.add(resultJson);
//            if (StringUtils.isNotBlank(ruleVersion.getScoreFieldEn())) {
                scoreJson.put(scoreFieldEn, ruleVersion.getScore());
                fieldList.add(scoreJson);
                input.put(scoreFieldEn, ruleVersion.getScore());
//            }
            input.put(resultFieldEn, "命中");
            //处理此规则需要输出的内容
            fieldList.addAll(ruleService.setComplexRuleOutput(ruleVersion.getId(), temp, input, TacticsType.OutType.SUCCESS_OUT));
            ruleMap.put("fieldList", fieldList);
            hitFlag = true;
        } else {
            resultJson.put(resultFieldEn, "未命中");
            ruleMap.put("ruleScore", 0);
            input.put(scoreFieldEn,0);
            fieldList.add(resultJson);
            fieldList.addAll(ruleService.setComplexRuleOutput(ruleVersion.getId(), temp, input, TacticsType.OutType.FAIL_OUT));
            ruleMap.put("fieldList", fieldList);
        }
        ruleResultList.add(ruleMap);
        return hitFlag;
    }

    //执行规则的条件
    private boolean executeRuleCondition(Map<String, Object> input, Map<String, Object> output, RuleConditionVo ruleCondition) {
        Integer conditionType = ruleCondition.getConditionType();
        boolean result = false;
        switch (conditionType) {
            //关系条件节点 &&和||
            case RuleConst.RELATION_CONDITION:
                //循环结果的条件
            case RuleConst.LOOP_RESULT_CONDITION:
            case RuleConst.CONDITION_RESULT_CONDITION:
                result = executeRelation(input, output, ruleCondition);
                break;
            //表达式条件节点
            case RuleConst.EXPRESSION_CONDITION:
                result = executeExpression(input, output, ruleCondition);
                break;
            //循环条件根节点
            case RuleConst.LOOP_CONDITION:
                result = executeLoop(input, output, ruleCondition);
                break;
            //条件组根节点
            case RuleConst.CONDITION_GROUP_CONDITION:
                result = executeCondGroup(input, output, ruleCondition);
                break;
        }
        return result;
    }

    //执行条件组
    private boolean executeCondGroup(Map<String, Object> input, Map<String, Object> output, RuleConditionVo ruleCondition) {
        //取出子条件
        List<RuleConditionVo> children = ruleCondition.getChildren();
        //存储命中条数
        int hitNum = 0;
        if (children == null) {
            return false;
        }
        //执行条件组中条件列表，命中则添加命中条数
        for (RuleConditionVo child : children) {
            boolean childResult = executeRuleCondition(input, output, child);
            if (childResult) {
                hitNum++;
            }
        }
        //获取条件组命中条件，为null直接不命中
        RuleConditionVo condGroup = ruleCondition.getCondGroupResultCondition();
        if (condGroup == null) {
            return false;
        }
        //传入命中条件和组内命中条数执行并返回
        Map<String, Object> map = new HashMap<>();
        //将命中条数存入map然后判断执行结果
        map.put("hitNum", hitNum);
        return executeRuleCondition(map, output, condGroup);
    }

    //关系条件节点 &&和||
    private boolean executeRelation(Map<String, Object> input, Map<String, Object> output, RuleConditionVo ruleCondition) {
        //获取关系逻辑
        String logical = ruleCondition.getLogical();
        //处理子逻辑
        List<RuleConditionVo> children = ruleCondition.getChildren();

        boolean result = false;
        switch (logical) {
            case "||":
                result = false;
                for (RuleConditionVo child : children) {
                    boolean childResult = executeRuleCondition(input, output, child);
                    if (childResult) {
                        return true;
                    }
                }
                break;
            case "&&":
                result = true;
                for (RuleConditionVo child : children) {
                    boolean childResult = executeRuleCondition(input, output, child);
                    if (!childResult) {
                        return false;
                    }
                }
                break;
        }
        return result;
    }

    //表达式条件节点
    private boolean executeExpression(Map<String, Object> input, Map<String, Object> output, RuleConditionVo ruleCondition) {
        String executionLogic = ruleCondition.getExecutionLogic();
        boolean result = false;
        ExpressionParam expressionParam = new ExpressionParam();
        //复制执行的关键参数到统一入参
        BeanUtils.copyProperties(ruleCondition, expressionParam);
        result = ExecuteUtils.getExpressionResult(expressionParam, input);
        return result;
    }

    //循环条件根节点
    private boolean executeLoop(Map<String, Object> input, Map<String, Object> output, RuleConditionVo ruleCondition) {
        List<RuleConditionVo> children = ruleCondition.getChildren();
        String fieldEn = ruleCondition.getFieldEn();

        //对循环中每个条件进行处理
        String[] split = fieldEn.split("\\.");
        //从map中取元素返回最终取到的对象
        Object obj = ExecuteUtils.getObjFromMap(input, fieldEn);
        List arrayList = new ArrayList();
        if (obj != null) {
            arrayList.addAll(JSON.parseObject(JSON.toJSONString(obj), ArrayList.class));
        }
        //取不到这个数组
        if (arrayList.isEmpty()) {
            return false;
        }
        //拼接当前对象的key
        String currentKey = "%" + split[split.length - 1] + "%";
        for (RuleConditionVo child : children) {
            List<RuleLoopGroupAction> loopGroupActions = child.getLoopGroupActions();
            // 调用for循环条件下的操作,并且将其存入input中
            for (RuleLoopGroupAction loopGroupAction : loopGroupActions) {
                this.initLoopGroupAction(loopGroupAction, input);
            }
        }
        for (Object currentObj : arrayList) {
            //将循环时的当前对象存入input
            input.put(currentKey, currentObj);
            //循环执行当前for中的每个判断单元
            for (RuleConditionVo child : children) {
                if (executeRuleCondition(input, output, child)) {
                    List<RuleLoopGroupAction> loopGroupActions = child.getLoopGroupActions();
                    // 调用for循环条件下的操作,并且将其存入input中
                    for (RuleLoopGroupAction loopGroupAction : loopGroupActions) {
                        this.saveLoopGroupAction(loopGroupAction, input);
                    }
                }
            }
        }
        //计算for的返回结果
        RuleConditionVo loopResultCondition = ruleCondition.getLoopResultCondition();
        boolean result = executeRuleCondition(input, output, loopResultCondition);
        return result;
    }

    //保存循环规则的动作
    private void saveLoopGroupAction(RuleLoopGroupAction loopGroupAction, Map<String, Object> input) {
        Integer actionType = loopGroupAction.getActionType();
        String actionKey = loopGroupAction.getActionKey();
        String actionValue = loopGroupAction.getActionValue();
        if (actionType == null) {
            return;
        }
        switch (actionType) {
            case RuleConst.LOOP_GROUP_ACTION_TYPE_SUM:
                Integer count = 1;
                if (input.containsKey(actionKey) && StringUtils.isNumeric(ExecuteUtils.getObjFromMap(input, actionKey).toString())) {
                    count = count + Integer.parseInt(ExecuteUtils.getObjFromMap(input, actionKey).toString());
                }
                input.put(actionKey, count);
                break;
            case RuleConst.LOOP_GROUP_ACTION_TYPE_ASSIGNMENT:
                //赋值待添加
                break;
            case RuleConst.LOOP_GROUP_ACTION_TYPE_OUT_CONST:
                input.put(actionKey, actionValue);
                break;
            case RuleConst.LOOP_GROUP_ACTION_TYPE_OUT_VARIABLE:
                input.put(actionKey, ExecuteUtils.getObjFromMap(input, actionValue));
                break;
        }
    }


    private void initLoopGroupAction(RuleLoopGroupAction loopGroupAction, Map<String, Object> input){
        Integer actionType = loopGroupAction.getActionType();
        String actionKey = loopGroupAction.getActionKey();
        String actionValue = loopGroupAction.getActionValue();
        if (actionType == null) {
            return;
        }
        switch (actionType) {
            case RuleConst.LOOP_GROUP_ACTION_TYPE_SUM:
                input.put(actionKey, 0);
                break;
            case RuleConst.LOOP_GROUP_ACTION_TYPE_ASSIGNMENT:
                //赋值待添加
                break;
            case RuleConst.LOOP_GROUP_ACTION_TYPE_OUT_CONST:
                input.put(actionKey, "");
                break;
            case RuleConst.LOOP_GROUP_ACTION_TYPE_OUT_VARIABLE:
                input.put(actionKey,new HashSet<>());
                break;
        }
    }
    /**
     * 执行基础规则（drools）
     *
     * @param map
     * @param outMap
     * @param rule
     * @param ruleResultList
     * @return
     */
    private boolean executeBasicRule(Map<String, Object> map, Map<String, Object> outMap, RuleInfo rule, CopyOnWriteArrayList<Map> ruleResultList) {
        boolean hitFlag = false;
        StatefulKnowledgeSession kSession = null;
        String keyMd5 = null;
        try {
            String ruleString = rule.getContent().replace("\\r\\n", "\r\n");
            ruleString = ruleString.replace("\\t", "\t");
            keyMd5 = CommonConst.DROOLS_KSESSION_KEY_PREFIX + MD5.GetMD5Code(ruleString);
            redisManager.set(keyMd5, ruleString, 120);
            kSession = kSessionPool.borrowObject(keyMd5);

            List<Result> resultList = new ArrayList<>();
            InputParam inputParam = new InputParam();
            inputParam.setInputParam(map);
            inputParam.setResult(resultList);
            FactHandle fact = kSession.insert(inputParam);
            kSession.fireAllRules();
            kSession.retract(fact);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ruleId", rule.getId());
            jsonObject.put("ruleCode", rule.getCode());
            jsonObject.put("ruleName", rule.getName());
            jsonObject.put("desc", rule.getDescription());
            String resultEn = rule.getResultFieldEn();
            if (StringUtils.isBlank(resultEn)){
                    resultEn = "rule_1_"+rule.getId()+"_hitResult";
            }
            String scoreEn = rule.getScoreFieldEn();
            if (StringUtils.isBlank(resultEn)){
                scoreEn = "rule_1_"+rule.getId()+"_score";
            }
            if (resultList.size() > 0) {
                jsonObject.put("ruleResult", "命中");
                jsonObject.put("ruleScore", rule.getScore());
                map.put(scoreEn, rule.getScore());
                map.put(resultEn, "命中");

                List<JSONObject> fieldList = new ArrayList<>();
                JSONObject resultJson = new JSONObject();
                JSONObject scoreJson = new JSONObject();
                resultJson.put(rule.getResultFieldEn(), "命中");
                scoreJson.put(rule.getScoreFieldEn(), rule.getScore());
                fieldList.add(resultJson);
                fieldList.add(scoreJson);
                if (null != rule && null != rule.getId()) {
                    fieldList.addAll(ruleService.setBaseRuleOutput(rule.getId(), map));
                    jsonObject.put("fieldList", fieldList);
                }
                ruleResultList.add(jsonObject);
                hitFlag = true;

            } else {
                // 未命中返回信息
                jsonObject.put("ruleResult", "未命中");
                map.put(resultEn, "未命中");
                map.put(scoreEn, 0);
                ruleResultList.add(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("请求异常", e);
        } finally {
            if (keyMd5 != null && kSession != null) {
                kSessionPool.returnObject(keyMd5, kSession);
            }
        }
        return hitFlag;
    }

    /**
     * 终止条件判断
     *
     * @param engineNode
     * @param inputParam
     * @param outMap
     * @param ruleHitList
     */
    private void terminalCondition(EngineNode engineNode, Map<String, Object> inputParam, Map<String, Object> outMap, List<RuleInfo> ruleHitList) {
        if (StringUtils.isBlank(engineNode.getNodeScript())) {
            return;
        }
        JSONObject nodeScript = JSONObject.parseObject(engineNode.getNodeScript());
        JSONObject terminationInfo = nodeScript.getJSONObject("terminationInfo");
        JSONArray selectedRule = terminationInfo.getJSONArray("selectedRule");
        String conditions = terminationInfo.getString("conditions");
        if (!selectedRule.isEmpty()) {
            if (!selectedRule.isEmpty()) {
                List<JSONObject> selectedRuleList = JSONObject.parseArray(JSONObject.toJSONString(selectedRule), JSONObject.class);
                // 查找已选规则中命名的规则集合
                List<RuleInfo> selectedHitRules = new ArrayList<>();
                for (JSONObject jsonObject : selectedRuleList) {
                    Optional<RuleInfo> rule = ruleHitList.stream().filter(item -> item.getId().equals(jsonObject.getLong("id"))).findFirst();
                    if (rule.isPresent()) {
                        selectedHitRules.add(rule.get());
                    }
                }

                int totalSize = selectedHitRules.size(); // 规则命名个数
                double totalScore = selectedHitRules.stream().mapToDouble(RuleInfo::getScore).sum(); // 规则总得分
                String sizeKey = engineNode.getNodeType() + "_" + engineNode.getNodeId() + "_terminal_size";
                String scoreKey = engineNode.getNodeType() + "_" + engineNode.getNodeId() + "_terminal_score";
                Map<String, Object> variablesMap = new HashMap<>();
                variablesMap.put(sizeKey, totalSize);
                variablesMap.put(scoreKey, totalScore);

                ExecuteUtils.terminalCondition(engineNode,inputParam,outMap,variablesMap);
            }
        }
    }

    private List<RuleInfo> getRuleFromJsonArray(JSONArray jsonArray) {
        List<Long> ruleIds = new ArrayList<>();
        Map<Long, Long> map = new HashMap<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject ruleObj = jsonArray.getJSONObject(i);
            Long versionId = ruleObj.getLong("ruleVersionId");
            Long ruleId = ruleObj.getLong("id");
            if (ruleId != null) {
                ruleIds.add(ruleId);
                if (versionId != null) {
                    map.put(ruleId, versionId);
                }
            }
        }

        List<RuleInfo> ruleInfoList = ruleService.getRuleList(ruleIds);
        for (RuleInfo ruleInfo : ruleInfoList) {
            if (ruleInfo.getDifficulty() == 2 || ruleInfo.getDifficulty() == 3) {
                Long versionId = map.get(ruleInfo.getId());
                ruleInfo.setVersionId(versionId);
                if (versionId != null) {
                    switch (ruleInfo.getDifficulty()) {
                        case 2:
                            RuleVersionVo ruleVersionVo = versionService.queryById(versionId);
                            ruleInfo.setVersion(ruleVersionVo);
                            ruleInfo.setScore(ruleVersionVo.getScore());
                            break;
                        case 3:
                            RuleScriptVersion ruleScriptVersion = ruleScriptVersionService.queryById(versionId);
                            ruleInfo.setScriptVersion(ruleScriptVersion);
                            ruleInfo.setVersion(JSON.parseObject(JSON.toJSONString(ruleScriptVersion), RuleVersionVo.class));
                            ruleInfo.setScore(0);
                            break;
                    }
                } else {
                    ruleInfo.setScore(0);
                }
            }
        }

        return ruleInfoList;
    }

    /**
     * 执行脚本规则
     *
     * @param inputParam
     * @param outMap
     * @param rule
     * @param ruleResultList
     * @return
     */
    private boolean executeScriptRule(Map<String, Object> inputParam, Map<String, Object> outMap, RuleInfo rule, CopyOnWriteArrayList<Map> ruleResultList) {
        boolean hitFlag = false;
        RuleScriptVersion scriptVersion = rule.getScriptVersion();
        if (RuleConst.ScriptType.GROOVY.equals(rule.getScriptType())&&RuleConst.ScriptType.GROOVY.equals( scriptVersion.getScriptType())) {
            //groovy脚本执行
            //取出需要执行的版本
            if (scriptVersion == null || StringUtils.isBlank(scriptVersion.getScriptContent())) {
                return false;
            }
            //取出脚本内容
            String scriptContent = scriptVersion.getScriptContent();
            //取出本规则集的规则列表
            Map<String, Object> ruleMap = new HashMap<>();
            ruleMap.put("ruleId", rule.getId());
            ruleMap.put("ruleCode", rule.getCode());
            ruleMap.put("ruleName", rule.getName());
            ruleMap.put("versionCode", scriptVersion.getVersionCode());
            ruleMap.put("versionDesc", scriptVersion.getDescription());
            ruleMap.put("desc", rule.getDescription());
            ruleMap.put("ruleResult", "未命中");


            String resultFieldEn = "hitResult";
            String resultEn = "rule_"+rule.getDifficulty()+"_"+rule.getId()+"_"+scriptVersion.getId()+"_hitResult";
            String scoreEn = "rule_"+rule.getDifficulty()+"_"+rule.getId()+"_"+scriptVersion.getId()+"_score";
            inputParam.put(resultEn, "未命中");
            inputParam.put(scoreEn,0);
            //根据执行的最终结果处理此规则输出内容
            List fieldList = new ArrayList<>();
            JSONObject resultJson = new JSONObject();
            try {
                Object resp = ExecuteUtils.getObjFromScript(inputParam, scriptContent);
                String result = "未命中";
                JSONObject executeResult = null;
                int ruleScore = 0;
                if (resp instanceof HashMap) {
                    Map resultMap = (HashMap) resp;
                    executeResult = JSON.parseObject(JSON.toJSONString(resultMap));
                    ruleScore = executeResult.getIntValue("ruleScore");
                    result = executeResult.getString(resultFieldEn);
                    JSONArray fieldListJson = executeResult.getJSONArray("fieldList");
                    JSONObject updateInputMap = executeResult.getJSONObject("updateInputMap");
                    if (fieldListJson != null) {
                        fieldList = fieldListJson.toJavaList(Object.class);
                        List list = new ArrayList();
                        for (Object o : fieldList) {
                            if (o!=null&& o instanceof Map){
                                Map map = ExecuteUtils.handleGroovyResult((Map) o);
                                list.add(map);
                            }
                        }
                        fieldList = list;
                    }

                    if (executeResult != null) {
                        ruleMap.put("ruleResult", result);
                        ruleMap.put("ruleScore", ruleScore);
                        resultJson.put(resultFieldEn, result);
                        fieldList.add(resultJson);
                        inputParam.put(resultFieldEn, result);
                        //处理此规则需要输出的内容
                        ruleMap.put("fieldList", fieldList);
                    }
                    if ("命中".equals(result)) {
                        hitFlag = true;
                        inputParam.put(resultEn,"命中");
                        inputParam.put(scoreEn,ruleScore);
                    }
                    //更新入参
                    if (updateInputMap!=null&&!updateInputMap.isEmpty()){
                        Set<Map.Entry<String, Object>> entries =  ExecuteUtils.handleGroovyResult(updateInputMap).entrySet();
                        for (Map.Entry<String, Object> entry : entries) {
                            inputParam.put(entry.getKey(),entry.getValue());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("脚本规则集执行错误：{}" + e);
            }
            ruleResultList.add(ruleMap);
        }
        return hitFlag;
    }

//    public static void main(String[] args) {
////        HashMap<String, Object> result = new HashMap<>();                   //规则执行的返回值
////        int ruleScore = 0;                                                  //规则命中时得分
////        String hitResult = "未命中";                                        //命中结果，可选类型为：命中、未命中
////        HashMap<String, Object> updateInputMap = new HashMap<>();           //用于更新入参的map，此map中的所有内容将被更新到入参中,key重复的将被覆盖。
////        ArrayList<HashMap<String, Object>> fieldList = new ArrayList<>();   //用于存放输出字段的值
////        //自定义代码区域，根据需要书写逻辑代码
////
////
////
////        //返回固定格式的结果用于后续执行
////        result.put("hitResult",hitResult);
////        result.put("ruleScore",ruleScore);
////        result.put("fieldList",fieldList);
////        result.put("updateInputMap",updateInputMap);
////        return result;
//    }

}
