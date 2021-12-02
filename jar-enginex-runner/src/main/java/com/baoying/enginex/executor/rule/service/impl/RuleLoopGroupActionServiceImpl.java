package com.baoying.enginex.executor.rule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baoying.enginex.executor.canal.TableEnum;
import com.baoying.enginex.executor.common.constants.Constants;
import com.baoying.enginex.executor.config.ConfigHolder;
import com.baoying.enginex.executor.redis.RedisManager;
import com.baoying.enginex.executor.redis.RedisUtils;
import com.baoying.enginex.executor.rule.mapper.RuleLoopGroupActionMapper;
import com.baoying.enginex.executor.rule.model.RuleLoopGroupAction;
import com.baoying.enginex.executor.rule.service.RuleLoopGroupActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service("ruleLoopGroupActionService")
public class RuleLoopGroupActionServiceImpl extends ServiceImpl<RuleLoopGroupActionMapper, RuleLoopGroupAction> implements RuleLoopGroupActionService {
    @Resource
    private RuleLoopGroupActionMapper ruleLoopGroupActionMapper;
    @Autowired
    private ConfigHolder configHolder;
    @Autowired
    private RedisManager redisManager;

    @Override
    public List<RuleLoopGroupAction> getRuleLoopList(Long forId, Long conditionId) {
        List<RuleLoopGroupAction> loopList = null;
        if(Constants.switchFlag.ON.equals(configHolder.getCacheSwitch())){
            String key = RedisUtils.getForeignKey(TableEnum.T_RULE_LOOP_GROUP_ACTION, forId);
            loopList = redisManager.getByForeignKey(key, RuleLoopGroupAction.class);
            loopList = loopList.stream().filter(item -> item.getConditionGroupId().equals(conditionId)).collect(Collectors.toList());
        } else {
            RuleLoopGroupAction ruleLoopGroupAction = new RuleLoopGroupAction();
            ruleLoopGroupAction.setConditionForId(forId);
            ruleLoopGroupAction.setConditionGroupId(conditionId);
            loopList = ruleLoopGroupActionMapper.selectList(new QueryWrapper<>(ruleLoopGroupAction));
        }

        if (loopList==null){
            loopList = new ArrayList<>();
        }
        return loopList;
    }
}
