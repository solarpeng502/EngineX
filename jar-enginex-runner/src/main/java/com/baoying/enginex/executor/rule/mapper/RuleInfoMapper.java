package com.baoying.enginex.executor.rule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baoying.enginex.executor.rule.model.RuleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface RuleInfoMapper extends BaseMapper<RuleInfo> {

    List<RuleInfo> getRuleList(@Param("ruleIds")List<Long> ruleIds);
}

