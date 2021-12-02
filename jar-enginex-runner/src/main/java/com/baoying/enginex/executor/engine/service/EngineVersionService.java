package com.baoying.enginex.executor.engine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baoying.enginex.executor.engine.model.EngineVersion;

public interface EngineVersionService extends IService<EngineVersion> {

    EngineVersion getEngineVersionById(Long versionId);

    /**
     * 获取引擎正在运行中的版本
     * @param engineId
     * @return
     */
    EngineVersion getRunningVersion(Long engineId);

}
