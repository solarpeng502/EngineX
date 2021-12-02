package com.baoying.enginex.executor.node.service;

import com.baoying.enginex.executor.engine.model.EngineNode;

import java.util.Map;

/**
 * 引擎节点执行
 */
public interface EngineNodeService {

    /**
     * 获取节点所需的指标
     * @param engineNode
     * @param inputParam
     */
    void getNodeField(EngineNode engineNode, Map<String, Object> inputParam);

    /**
     * 执行节点逻辑
     * @param engineNode
     * @param inputParam
     * @param outMap
     */
    void runNode(EngineNode engineNode, Map<String, Object> inputParam, Map<String, Object> outMap);
}
