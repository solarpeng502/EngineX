package com.baoying.enginex.executor.rule.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.baoying.enginex.executor.rule.model.RuleScriptVersion;

import java.util.List;


public interface RuleScriptVersionService extends IService<RuleScriptVersion> {
    RuleScriptVersion queryById(Long id);

    List<RuleScriptVersion> queryVersionListByRuleId(Long ruleId);

    List<String> queryFieldEnByVersionId(Long versionId);
    List<String> queryFieldEnByVersionIds(List<Long> versionId);

}
