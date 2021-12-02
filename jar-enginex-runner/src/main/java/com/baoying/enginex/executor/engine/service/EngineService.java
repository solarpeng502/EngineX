package com.baoying.enginex.executor.engine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baoying.enginex.executor.engine.model.Engine;

public interface EngineService extends IService<Engine> {

    /**
     * 根据id查询引擎
     * @param id
     * @return
     */
    Engine getEngineById(Long id);
}
