package com.baoying.enginex.executor.rule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baoying.enginex.executor.canal.TableEnum;
import com.baoying.enginex.executor.common.constants.Constants;
import com.baoying.enginex.executor.config.ConfigHolder;
import com.baoying.enginex.executor.redis.RedisManager;
import com.baoying.enginex.executor.redis.RedisUtils;
import com.baoying.enginex.executor.rule.mapper.RuleFieldInfoMapper;
import com.baoying.enginex.executor.rule.model.RuleFieldInfo;
import com.baoying.enginex.executor.rule.service.RuleFieldInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RuleFieldInfoServiceImpl extends ServiceImpl<RuleFieldInfoMapper, RuleFieldInfo> implements RuleFieldInfoService {

    @Resource
    private RuleFieldInfoMapper ruleFieldInfoMapper;
    @Autowired
    private ConfigHolder configHolder;
    @Autowired
    private RedisManager redisManager;

    @Override
    public List<RuleFieldInfo> queryByRuleId(Long ruleId) {
        RuleFieldInfo ruleFieldInfo = new RuleFieldInfo();
        ruleFieldInfo.setRuleId(ruleId);
        List<RuleFieldInfo> ruleFieldInfoList = ruleFieldInfoMapper.selectList(new QueryWrapper<>(ruleFieldInfo));
        return ruleFieldInfoList;
    }

    @Override
    public List<String> getFieldEnList(List<Long> ruleIds) {
        List<String> list = null;
        if(Constants.switchFlag.ON.equals(configHolder.getCacheSwitch())){
            List<String> keys = RedisUtils.getForeignKey(TableEnum.T_RULE_FIELD, ruleIds);
            List<RuleFieldInfo> ruleFieldInfos = redisManager.hgetAllBatchByForeignKeys(keys, RuleFieldInfo.class);
            Set<String> set = ruleFieldInfos.stream().map(item -> {
                String[] fieldIdArr = item.getFieldId().split("\\|"); // 587|f_hr_age
                return fieldIdArr[1];
            }).collect(Collectors.toSet());

            list = new ArrayList<>(set);
        } else {
            list = ruleFieldInfoMapper.getFieldEnList(ruleIds);
        }
        return list;
    }

}
