package com.baoying.enginex.executor.rule.model.vo;


import com.baoying.enginex.executor.rule.model.RuleFieldInfo;
import com.baoying.enginex.executor.rule.model.RuleInfo;
import com.baoying.enginex.executor.rule.model.RuleScriptVersion;
import com.baoying.enginex.executor.tactics.model.TacticsOutput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors
public class RuleVo extends RuleInfo {

    private RuleConditionVo ruleConditionVo;//规则对应的结点树


    private List<RuleFieldInfo> ruleFieldList;//简单规则条件列表

    private List<TacticsOutput> tacticsOutputList;//输出字段

    private List<RuleVersionVo> ruleVersionList;//版本列表
    private List<RuleScriptVersion> ruleScriptVersionList;
}
