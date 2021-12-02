package com.baoying.enginex.executor.rule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baoying.enginex.executor.canal.TableEnum;
import com.baoying.enginex.executor.common.constants.Constants;
import com.baoying.enginex.executor.config.ConfigHolder;
import com.baoying.enginex.executor.redis.RedisManager;
import com.baoying.enginex.executor.redis.RedisUtils;
import com.baoying.enginex.executor.rule.mapper.RuleVersionMapper;
import com.baoying.enginex.executor.rule.model.RuleVersion;
import com.baoying.enginex.executor.rule.model.vo.RuleConditionVo;
import com.baoying.enginex.executor.rule.model.vo.RuleVersionVo;
import com.baoying.enginex.executor.rule.service.RuleConditionService;
import com.baoying.enginex.executor.rule.service.RuleVersionService;
import com.baoying.enginex.executor.tactics.consts.TacticsType;
import com.baoying.enginex.executor.tactics.model.TacticsOutput;
import com.baoying.enginex.executor.tactics.service.TacticsOutputService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RuleVersionServiceImpl extends ServiceImpl<RuleVersionMapper, RuleVersion> implements RuleVersionService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RuleVersionMapper versionMapper;
    @Autowired
    private RuleConditionService conditionService;
    @Autowired
    private TacticsOutputService outputService;
    @Autowired
    private ConfigHolder configHolder;
    @Autowired
    private RedisManager redisManager;

    @Override
    public RuleVersionVo queryById(Long id) {
        RuleVersion ruleVersion = null;
        if (Constants.switchFlag.ON.equals(configHolder.getCacheSwitch())) {
            String key = RedisUtils.getPrimaryKey(TableEnum.T_RULE_VERSION, id);
            ruleVersion = redisManager.getByPrimaryKey(key, RuleVersion.class);
        } else {
            ruleVersion = versionMapper.selectById(id);
        }

        RuleVersionVo result = new RuleVersionVo();
        if (ruleVersion == null) {
            return result;
        }
        BeanUtils.copyProperties(ruleVersion, result);
        //查询ruleCondition组装成树形结构
        RuleConditionVo ruleConditionVo = conditionService.queryByVersionId(id);
        List<TacticsOutput>  tacticsOutputList = outputService.queryByTactics(new TacticsOutput(id, TacticsType.COMPLEX_RULE,TacticsType.OutType.SUCCESS_OUT));
        List<TacticsOutput>  failOutputList = outputService.queryByTactics(new TacticsOutput(id, TacticsType.COMPLEX_RULE,TacticsType.OutType.FAIL_OUT));
        result.setRuleConditionVo(ruleConditionVo);
        result.setTacticsOutputList(tacticsOutputList);
        result.setFailOutputList(failOutputList);
        return result;
    }

    @Override
    public List<RuleVersionVo> queryVersionListByRuleId(Long ruleId) {
        LambdaQueryWrapper<RuleVersion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RuleVersion::getRuleId,ruleId);
        queryWrapper.eq(RuleVersion::getStatus,1);
        queryWrapper.orderByDesc(RuleVersion::getUpdateTime);
        List<RuleVersion> ruleVersionList = versionMapper.selectList(queryWrapper);
        List<RuleVersionVo> ruleVersionVoList = new ArrayList<>();
        for (RuleVersion ruleVersion :  ruleVersionList) {
            RuleVersionVo versionVo = new RuleVersionVo();
            BeanUtils.copyProperties(ruleVersion,versionVo);
            ruleVersionVoList.add(versionVo);
        }
        return ruleVersionVoList;
    }

}
