package com.baoying.enginex.executor.engine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baoying.enginex.executor.engine.model.EngineNode;

import java.util.List;

public interface EngineNodeService extends IService<EngineNode> {

    /**
     * 根据版本id获取版本下的所有节点
     * @param versionId
     * @return
     */
    List<EngineNode> getEngineNodeListByVersionId(Long versionId);
}
