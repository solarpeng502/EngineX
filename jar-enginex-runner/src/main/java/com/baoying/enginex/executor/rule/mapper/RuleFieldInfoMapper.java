package com.baoying.enginex.executor.rule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baoying.enginex.executor.rule.model.RuleFieldInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RuleFieldInfoMapper extends BaseMapper<RuleFieldInfo> {

    List<String> getFieldEnList(@Param("ruleIds") List<Long> ruleIds);
}
