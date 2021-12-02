package com.baoying.enginex.executor.tactics.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baoying.enginex.executor.tactics.model.TacticsOutput;

import java.util.List;
import java.util.Map;


public interface TacticsOutputService extends IService<TacticsOutput> {

    List<TacticsOutput> queryByTactics(TacticsOutput entity);

    List<JSONObject> setOutput(TacticsOutput entity,Map<String,Object> input);

    boolean judgeOutCondition(String condition,Map<String,Object> input);
}
