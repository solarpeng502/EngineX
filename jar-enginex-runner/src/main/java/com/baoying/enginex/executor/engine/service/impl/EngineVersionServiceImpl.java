package com.baoying.enginex.executor.engine.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baoying.enginex.executor.canal.TableEnum;
import com.baoying.enginex.executor.common.constants.Constants;
import com.baoying.enginex.executor.config.ConfigHolder;
import com.baoying.enginex.executor.engine.mapper.EngineVersionMapper;
import com.baoying.enginex.executor.engine.model.EngineVersion;
import com.baoying.enginex.executor.engine.service.EngineVersionService;
import com.baoying.enginex.executor.redis.RedisManager;
import com.baoying.enginex.executor.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EngineVersionServiceImpl extends ServiceImpl<EngineVersionMapper, EngineVersion> implements EngineVersionService {

    @Autowired
    private ConfigHolder configHolder;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private EngineVersionMapper engineVersionMapper;

    @Override
    public EngineVersion getEngineVersionById(Long versionId) {
        EngineVersion engineVersion = null;
        if(Constants.switchFlag.ON.equals(configHolder.getCacheSwitch())){
            String key = RedisUtils.getPrimaryKey(TableEnum.T_ENGINE_VERSION, versionId);
            engineVersion = redisManager.getByPrimaryKey(key, EngineVersion.class);
        } else {
            engineVersion = engineVersionMapper.selectById(versionId);
        }
        return engineVersion;
    }

    @Override
    public EngineVersion getRunningVersion(Long engineId) {
        EngineVersion engineVersion = null;
        if(Constants.switchFlag.ON.equals(configHolder.getCacheSwitch())){
            String key = RedisUtils.getForeignKey(TableEnum.T_ENGINE_VERSION, engineId);
            List<EngineVersion> list = redisManager.getByForeignKey(key, EngineVersion.class);
            Optional<EngineVersion> optional = list.stream().filter(item -> item.getBootState() == 1).findFirst();
            if(optional.isPresent()){
                engineVersion = optional.get();
            }
        } else {
            engineVersion = engineVersionMapper.getRunningVersion(engineId);
        }

        return engineVersion;
    }
}
