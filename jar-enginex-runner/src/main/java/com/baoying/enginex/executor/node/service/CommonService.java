package com.baoying.enginex.executor.node.service;

import com.baoying.enginex.executor.datamanage.model.Field;

import java.util.List;
import java.util.Map;

public interface CommonService {

    boolean getFieldByIds(List<Long> ids, Map<String, Object> inputParam);

    /**
     * 获取引擎节点所需的指标
     * @param fields
     * @param engineNode
     * @param inputParam
     * @return
     */
    boolean getEngineField(List<Field> fields, Map<String, Object> inputParam);

    /**
     * 获取衍生指标
     * @param inputParam
     */
    void getFieldResult (Map<String, Object> inputParam);
}
