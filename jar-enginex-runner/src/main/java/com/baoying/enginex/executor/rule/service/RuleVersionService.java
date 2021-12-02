package com.baoying.enginex.executor.rule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baoying.enginex.executor.rule.model.RuleVersion;
import com.baoying.enginex.executor.rule.model.vo.RuleVersionVo;

import java.util.List;

public interface RuleVersionService extends IService<RuleVersion> {

    RuleVersionVo queryById(Long id);

    List<RuleVersionVo> queryVersionListByRuleId(Long ruleId);
}
