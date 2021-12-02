package com.baoying.enginex.executor.rule.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.baoying.enginex.executor.rule.model.RuleLoopGroupAction;

import java.util.List;


public interface RuleLoopGroupActionService extends IService<RuleLoopGroupAction> {

    List<RuleLoopGroupAction> getRuleLoopList(Long forId, Long conditionId);

}
