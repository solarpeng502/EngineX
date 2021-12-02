package com.baoying.enginex.executor.knowledge.mapper;


import com.baoying.enginex.executor.common.mapper.BaseMapper;
import com.baoying.enginex.executor.knowledge.model.KnowledgeTree;

import java.util.List;
import java.util.Map;


public interface KnowledgeTreeMapper  extends BaseMapper<KnowledgeTree> {
	
	/**
	 * getTreeList：(根据父节点id和组织id,查询其下的所有子节点)
	 * @author keke
	 * @param paramMap 参数集合
	 * @return 父节点下的所有子节点
	 * */
	public List<KnowledgeTree> getTreeList(Map<String, Object> paramMap);

	/**
	 * batchInsert:(批量新增节点)
	 * @author keke
	 * @param k 节点信息集合
	 * @return
	 * */
	public int batchInsert(List<KnowledgeTree> k);

	/**
	 * getTreeList：(根据父节点id和组织id,查询其下的所有子节点,若节点下规则，则过滤掉)
	 * @author keke
	 * @param paramMap 参数集合
	 * @return 父节点下的所有子节点
	 * */
	public List<KnowledgeTree> getTreeDataForEngine(Map<String, Object> paramMap);
}
