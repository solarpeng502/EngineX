package com.baoying.enginex.executor.engine.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baoying.enginex.executor.canal.TableEnum;
import com.baoying.enginex.executor.common.constants.Constants;
import com.baoying.enginex.executor.config.ConfigHolder;
import com.baoying.enginex.executor.engine.mapper.EngineMapper;
import com.baoying.enginex.executor.engine.model.Engine;
import com.baoying.enginex.executor.engine.service.EngineService;
import com.baoying.enginex.executor.redis.RedisManager;
import com.baoying.enginex.executor.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EngineServiceImpl extends ServiceImpl<EngineMapper, Engine> implements EngineService {

    @Autowired
    private ConfigHolder configHolder;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private EngineMapper engineMapper;

    @Override
    public Engine getEngineById(Long id) {
        Engine engine = null;
        if(Constants.switchFlag.ON.equals(configHolder.getCacheSwitch())){
            String key = RedisUtils.getPrimaryKey(TableEnum.T_ENGINE, id);
            engine = redisManager.getByPrimaryKey(key, Engine.class);
        } else {
            engine = engineMapper.selectById(id);
        }

        return engine;
    }
}
