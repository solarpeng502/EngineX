package com.baoying.enginex.executor.rule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baoying.enginex.executor.rule.model.RuleConditionInfo;
import com.baoying.enginex.executor.rule.model.vo.RuleConditionVo;

import java.util.List;


public interface RuleConditionService extends IService<RuleConditionInfo> {

    RuleConditionVo queryByVersionId(Long versionId);

    List<String> queryFieldEnByVersionIds(List<Long> versionIds);
}
