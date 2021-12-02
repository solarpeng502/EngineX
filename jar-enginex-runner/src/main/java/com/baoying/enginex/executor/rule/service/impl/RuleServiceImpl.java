package com.baoying.enginex.executor.rule.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baoying.enginex.executor.canal.TableEnum;
import com.baoying.enginex.executor.common.constants.Constants;
import com.baoying.enginex.executor.config.ConfigHolder;
import com.baoying.enginex.executor.redis.RedisManager;
import com.baoying.enginex.executor.redis.RedisUtils;
import com.baoying.enginex.executor.rule.mapper.RuleInfoMapper;

import com.baoying.enginex.executor.rule.model.RuleFieldInfo;
import com.baoying.enginex.executor.rule.model.RuleInfo;
import com.baoying.enginex.executor.rule.model.RuleScriptVersion;
import com.baoying.enginex.executor.rule.model.vo.RuleVersionVo;
import com.baoying.enginex.executor.rule.model.vo.RuleVo;
import com.baoying.enginex.executor.rule.service.RuleFieldInfoService;
import com.baoying.enginex.executor.rule.service.RuleScriptVersionService;
import com.baoying.enginex.executor.rule.service.RuleService;
import com.baoying.enginex.executor.rule.service.RuleVersionService;
import com.baoying.enginex.executor.tactics.consts.TacticsType;
import com.baoying.enginex.executor.tactics.model.TacticsOutput;
import com.baoying.enginex.executor.tactics.service.TacticsOutputService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


@Service("ruleService2")
public class RuleServiceImpl extends ServiceImpl<RuleInfoMapper, RuleInfo> implements RuleService {
    @Resource
    private RuleInfoMapper ruleInfoMapper;
    @Autowired
    private RuleVersionService versionService;
    @Resource
    private RuleFieldInfoService ruleFieldInfoService;
    @Resource
    private TacticsOutputService outputService;
    @Autowired
    private ConfigHolder configHolder;
    @Autowired
    private RedisManager redisManager;
    @Resource
    private RuleScriptVersionService ruleScriptVersionService;

    @Override
    public RuleVo queryById(Long id) {
        //查询规则
        RuleInfo ruleInfo = ruleInfoMapper.selectById(id);
        if (ruleInfo==null){
            return null;
        }
        RuleVo ruleVo = new RuleVo();
        BeanUtils.copyProperties(ruleInfo, ruleVo);
        Integer difficulty = ruleInfo.getDifficulty();
        switch (difficulty) {
            case 1:
                List<RuleFieldInfo> list = ruleFieldInfoService.queryByRuleId(id);
                List<TacticsOutput>  tacticsOutputList = outputService.queryByTactics(new TacticsOutput(id, TacticsType.BASE_RULE));
                ruleVo.setTacticsOutputList(tacticsOutputList);
                ruleVo.setRuleFieldList(list);
                break;
            case 2:
                //查询版本
                List<RuleVersionVo> ruleVersionList = versionService.queryVersionListByRuleId(id);
                ruleVo.setRuleVersionList(ruleVersionList);
                break;
            case 3:
                //脚本规则集
                List<RuleScriptVersion> ruleScriptVersionList = ruleScriptVersionService.queryVersionListByRuleId(id);
                ruleVo.setRuleScriptVersionList(ruleScriptVersionList);
                break;
        }
        return ruleVo;
    }

    @Override
    public List<JSONObject> setComplexRuleOutput(Long versionId, Map<String,Object> temp, Map<String, Object> input, String outType) {
        List<JSONObject> jsonObjectList = outputService.setOutput(new TacticsOutput(versionId, TacticsType.COMPLEX_RULE,outType), temp);
        for (JSONObject jsonObject : jsonObjectList) {
            input.putAll(jsonObject);
        }
        return jsonObjectList;
    }

    @Override
    public List<JSONObject> setBaseRuleOutput(Long ruleId, Map<String, Object> input) {
        List<JSONObject> jsonObjectList = outputService.setOutput(new TacticsOutput(ruleId, TacticsType.BASE_RULE), input);
        return jsonObjectList;
    }

    @Override
    public List<RuleInfo> getRuleList(List<Long> ruleIds) {
        List<RuleInfo> ruleInfoList = null;
        if(Constants.switchFlag.ON.equals(configHolder.getCacheSwitch())){
            List<String> keys = RedisUtils.getPrimaryKey(TableEnum.T_RULE, ruleIds);
            ruleInfoList = redisManager.hgetAllBatchByPrimaryKeys(keys, RuleInfo.class);
        } else {
            ruleInfoList = ruleInfoMapper.getRuleList(ruleIds);
        }
        return ruleInfoList;
    }
}
