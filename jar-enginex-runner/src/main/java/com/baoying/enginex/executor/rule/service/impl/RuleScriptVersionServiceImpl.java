package com.baoying.enginex.executor.rule.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baoying.enginex.executor.datamanage.model.Field;
import com.baoying.enginex.executor.rule.mapper.RuleScriptVersionMapper;
import com.baoying.enginex.executor.rule.model.RuleScriptVersion;
import com.baoying.enginex.executor.rule.service.RuleScriptVersionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service("ruleScriptVersionService")
public class RuleScriptVersionServiceImpl extends ServiceImpl<RuleScriptVersionMapper, RuleScriptVersion> implements RuleScriptVersionService {
    @Resource
    private RuleScriptVersionMapper ruleScriptVersionMapper;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public RuleScriptVersion queryById(Long id) {
        if (id!=null){
            return this.getById(id);
        }
        return null;
    }

    @Override
    public List<RuleScriptVersion> queryVersionListByRuleId(Long ruleId) {
        LambdaQueryWrapper<RuleScriptVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RuleScriptVersion::getRuleId,ruleId);
        wrapper.eq(RuleScriptVersion::getStatus,1);
        wrapper.orderByDesc(RuleScriptVersion::getId);
        List<RuleScriptVersion> list = this.list(wrapper);
        return list;
    }

    @Override
    public List<String> queryFieldEnByVersionId(Long versionId) {

        RuleScriptVersion ruleScriptVersion = this.queryById(versionId);
        Set<String> fieldEnSet = new HashSet<>();
        if (ruleScriptVersion==null){
            return new ArrayList<>();
        }
        collectFieldEn(ruleScriptVersion,fieldEnSet);
        return new ArrayList<>(fieldEnSet);
    }

    @Override
    public List<String> queryFieldEnByVersionIds(List<Long> versionIds) {
        LambdaQueryWrapper<RuleScriptVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(RuleScriptVersion::getId,versionIds);
        List<RuleScriptVersion> list = this.list(wrapper);
        Set<String> fieldEnSet = new HashSet<>();
        if (list!=null&&!list.isEmpty()){
            for (RuleScriptVersion ruleScriptVersion : list) {
                collectFieldEn(ruleScriptVersion,fieldEnSet);
            }
        }
        return new ArrayList<>(fieldEnSet);
    }
    private void collectFieldEn(RuleScriptVersion ruleScriptVersion,Set<String> fieldEnSet){
        String scriptContent = ruleScriptVersion.getScriptContent();
        if (StringUtils.isNotBlank(scriptContent)){
            JSONObject jsonObject = JSON.parseObject(scriptContent);
            Object farr = jsonObject.get("farr");
            if (farr!=null&&!"".equals(farr)){
                List<Field> fieldList = JSONArray.parseArray(JSON.toJSONString(farr), Field.class);
                fieldEnSet.addAll(fieldList.stream().map(item->{return item.getFieldEn();}).collect(Collectors.toSet()));
            }
        }
    }

}
