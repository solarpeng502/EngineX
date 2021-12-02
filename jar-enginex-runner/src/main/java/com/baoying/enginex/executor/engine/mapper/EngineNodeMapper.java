package com.baoying.enginex.executor.engine.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baoying.enginex.executor.engine.model.EngineNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EngineNodeMapper extends BaseMapper<EngineNode> {

	/**
	 * 根据版本id获取版本下的所有节点
	 * @param engineVersionId
	 * @return
	 */
	List<EngineNode> getEngineNodeListByVersionId(@Param("engineVersionId") Long engineVersionId);

}