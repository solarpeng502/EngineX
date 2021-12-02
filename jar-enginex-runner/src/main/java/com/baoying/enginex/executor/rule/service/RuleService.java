package com.baoying.enginex.executor.rule.service;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baoying.enginex.executor.rule.model.RuleInfo;
import com.baoying.enginex.executor.rule.model.vo.RuleVo;


import java.util.List;
import java.util.Map;

public interface RuleService extends IService<RuleInfo> {

    /**
     * 通过ID查询单条数据
     * @param id 主键
     * @return 实例对象
     */
    RuleVo queryById(Long id);

    List<JSONObject> setComplexRuleOutput(Long versionId, Map<String,Object> temp, Map<String,Object> input, String outType);

    List<JSONObject> setBaseRuleOutput(Long ruleId,  Map<String,Object> input);

    List<RuleInfo> getRuleList(List<Long> ruleIds);

}
