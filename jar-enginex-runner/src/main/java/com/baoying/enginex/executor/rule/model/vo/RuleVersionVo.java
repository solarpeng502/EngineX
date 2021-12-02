package com.baoying.enginex.executor.rule.model.vo;


import com.baoying.enginex.executor.rule.model.RuleVersion;
import com.baoying.enginex.executor.tactics.model.TacticsOutput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleVersionVo extends RuleVersion {
    private RuleConditionVo ruleConditionVo;//规则对应的结点树

    private List<TacticsOutput> tacticsOutputList;//成功输出字段
    private List<TacticsOutput> failOutputList;//失败输出字段
}
