package com.baoying.enginex.executor.rule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baoying.enginex.executor.rule.model.RuleFieldInfo;

import java.util.List;

public interface RuleFieldInfoService extends IService<RuleFieldInfo> {

    List<RuleFieldInfo> queryByRuleId(Long ruleId);

    List<String> getFieldEnList(List<Long> ruleIds);
}
