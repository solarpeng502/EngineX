package com.baoying.enginex.executor.tactics.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baoying.enginex.executor.canal.TableEnum;
import com.baoying.enginex.executor.common.constants.Constants;
import com.baoying.enginex.executor.common.model.ExpressionParam;
import com.baoying.enginex.executor.config.ConfigHolder;
import com.baoying.enginex.executor.redis.RedisManager;
import com.baoying.enginex.executor.redis.RedisUtils;
import com.baoying.enginex.executor.tactics.consts.TacticsType;
import com.baoying.enginex.executor.tactics.mapper.TacticsOutputMapper;
import com.baoying.enginex.executor.tactics.model.OutCondition;
import com.baoying.enginex.executor.tactics.model.TacticsOutput;
import com.baoying.enginex.executor.tactics.service.TacticsOutputService;

import com.baoying.enginex.executor.util.ExecuteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TacticsOutputServiceImpl extends ServiceImpl<TacticsOutputMapper, TacticsOutput> implements TacticsOutputService {

    @Autowired
    private ConfigHolder configHolder;
    @Autowired
    private RedisManager redisManager;

    @Override
    public List<TacticsOutput> queryByTactics(TacticsOutput entity) {
        List<TacticsOutput> tacticsOutputList = null;
        if(Constants.switchFlag.ON.equals(configHolder.getCacheSwitch())){
            String key = RedisUtils.getForeignKey(TableEnum.T_TACTICS_OUTPUT, entity.getTacticsId());
            tacticsOutputList = redisManager.getByForeignKey(key, TacticsOutput.class);
            if(tacticsOutputList != null){
                tacticsOutputList = tacticsOutputList.stream().filter(item -> item.getTacticsType().equals(entity.getTacticsType()))
                        .collect(Collectors.toList());
            }
            if (tacticsOutputList!=null&&!tacticsOutputList.isEmpty()&& TacticsType.COMPLEX_RULE.equals(entity.getVariableType())&&entity.getOutType()!=null){
                //复杂规则需要过滤出类型
                tacticsOutputList = tacticsOutputList.stream().filter(item ->entity.getOutType().equals( item.getOutType())).collect(Collectors.toList());
            }
        } else {
            tacticsOutputList = this.list(new QueryWrapper<>(entity));
        }

        return tacticsOutputList;
    }

    @Transactional

    //设置输出，传入map向map中放入输出并且返回输出列表
    @Override
    public List<JSONObject> setOutput(TacticsOutput entity, Map<String, Object> input) {
        List<TacticsOutput> tacticsOutputList = this.queryByTactics(entity);
        List<JSONObject> jsonList = new ArrayList<>();
        if (tacticsOutputList != null && tacticsOutputList.size() > 0) {
            for (TacticsOutput tacticsOutput : tacticsOutputList) {
                if (!this.judgeOutCondition(tacticsOutput.getOutCondition(),input)){
                    continue;
                }
                JSONObject json = new JSONObject();
                String fieldEn = tacticsOutput.getFieldEn();
                String fieldValue = tacticsOutput.getFieldValue();
                Object value = fieldValue;
                Integer variableType = tacticsOutput.getVariableType();
                if (variableType != null) {
                    switch (variableType) {
                        case 2:
                            value = ExecuteUtils.getObjFromMap(input, fieldValue);
                            break;
                        case 3:
                            value = ExecuteUtils.getObjFromScript(input,fieldValue);
                            break;
                    }
                }
                if (value != null ) {
                    if (!"".equals(value)&&!"'".equals(value)&&value.toString().startsWith("'")&&value.toString().endsWith("'")){
                        value = value.toString().substring(1,value.toString().length()-1);
                    }
                    json.put(fieldEn, value);
                    input.put(fieldEn, value);
                    jsonList.add(json);
                }
            }
        }
        return jsonList;
    }

    //判断是否符合输出条件
    @Override
    public boolean judgeOutCondition(String condition, Map<String, Object> input) {
        //条件为空则符合输出
        if (null == condition || "".equals(condition)) {
            return true;
        }
        OutCondition outCondition;
        try {
            outCondition = JSON.parseObject(condition, OutCondition.class);
        }catch (Exception e){
            //字符串转json失败
            return true;
        }
        String logical = outCondition.getLogical();
        List<ExpressionParam> conditionList = outCondition.getConditionList();
        if (null == logical || null == conditionList||conditionList.size()<1){
            return true;
        }
        boolean result=false;
        switch (logical) {
            case "||":
                result = false;
                for (ExpressionParam expression : conditionList) {
                    if (ExecuteUtils.getExpressionResult(expression, input)){
                        return true;
                    }
                }
                break;
            case "&&":
                result = true;
                for (ExpressionParam expression : conditionList) {
                    if (!ExecuteUtils.getExpressionResult(expression, input)){
                        return false;
                    }
                }
                break;
        }
        return result;
    }


}
