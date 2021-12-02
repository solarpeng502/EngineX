package com.baoying.enginex.executor.engine.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baoying.enginex.executor.canal.TableEnum;
import com.baoying.enginex.executor.common.constants.Constants;
import com.baoying.enginex.executor.config.ConfigHolder;
import com.baoying.enginex.executor.engine.mapper.EngineNodeMapper;
import com.baoying.enginex.executor.engine.model.EngineNode;
import com.baoying.enginex.executor.engine.service.EngineNodeService;
import com.baoying.enginex.executor.redis.RedisManager;
import com.baoying.enginex.executor.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EngineNodeServiceImpl extends ServiceImpl<EngineNodeMapper, EngineNode> implements EngineNodeService {

    @Autowired
    private ConfigHolder configHolder;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private EngineNodeMapper engineNodeMapper;

    @Override
    public List<EngineNode> getEngineNodeListByVersionId(Long versionId) {
        List<EngineNode> engineNodeList = null;
        if(Constants.switchFlag.ON.equals(configHolder.getCacheSwitch())){
            String key = RedisUtils.getForeignKey(TableEnum.T_ENGINE_NODE, versionId);
            engineNodeList = redisManager.getByForeignKey(key, EngineNode.class);
            if(engineNodeList != null){
                // 按node_order升序排序
                engineNodeList = engineNodeList.stream().sorted(Comparator.comparing(EngineNode::getNodeOrder)).collect(Collectors.toList());
            }
        } else {
            engineNodeList = engineNodeMapper.getEngineNodeListByVersionId(versionId);
        }

        return engineNodeList;
    }
}
