package com.baoying.enginex.executor.engine.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baoying.enginex.executor.engine.model.EngineVersion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface EngineVersionMapper extends BaseMapper<EngineVersion> {

	/**
	 * 获取引擎正在运行中的版本
	 * @param engineId
	 * @return
	 */
	EngineVersion getRunningVersion(@Param("engineId") Long engineId);

	/**
	 * 获取指定版本信息
	 * @param paramMap
	 * @return
	 */
	EngineVersion getTargetEngineVersion(Map<String, Object> paramMap);
	
}