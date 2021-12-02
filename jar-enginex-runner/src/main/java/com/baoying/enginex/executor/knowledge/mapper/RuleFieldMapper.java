package com.baoying.enginex.executor.knowledge.mapper;


import com.baoying.enginex.executor.common.mapper.BaseMapper;
import com.baoying.enginex.executor.engine.model.NodeKnowledge;
import com.baoying.enginex.executor.knowledge.model.Rule;
import com.baoying.enginex.executor.knowledge.model.RuleField;

import java.util.List;
import java.util.Map;


public interface RuleFieldMapper extends BaseMapper<RuleField> {
	
	/**
	 * getFieldList : (根据规则id,，获取规则下的所有字段)
	 * @author keke
	 * @param  ruleId 规则id
	 * @return 规则下的所有字段
	 * */
	public  List<RuleField> getFieldList(Long ruleId); 
	
	/**
	 * insertField : (批量新增字段记录)
	 * @author keke
	 * @param  rlist 字段信息集合
	 * @return 
	 * */
	public int insertField(List<RuleField> ruleFieldlist);
	
	/**
	 * updateField : (批量修改字段记录)
	 * @author keke
	 * @param rlist 字段信息集合
	 * @return 
	 * */
	public boolean updateField(List<RuleField> rlist);
	
	/**
	 * deleteField : (批量删除字段记录)
	 * @author keke
	 * @param  rlist 字段信息集合
	 * @return 
	 * */
	public boolean deleteField(List<RuleField> rlist);
	
	
	/**
	 * getNodeByList : (根据引擎节点得到所用字段)
	 * @author wenyu.cao
	 * @param  nodeid 节点编号
	 * @return  返回字段list
	 * */
	public  List<RuleField> getNodeByList(NodeKnowledge knowledge);
	public  List<RuleField> getNodeByListNew(NodeKnowledge knowledge);
	/**
	 * 
	 * 根据规则得到规则引用字段
	 * @param nodeKnowledge
	 * @return 
	 * @see
	 */
	public List<RuleField> selectNodeByRuleList(NodeKnowledge nodeKnowledge);
	public List<RuleField> selectNodeByRuleListNew(NodeKnowledge nodeKnowledge);
	/**
	 *
	 * 根据规则id得到规则引用字段
	 * @param paramMap 规则id集合
	 * @return
	 * @see
	 */
	public  List<RuleField> selectByRuleList(Map<String, Object> paramMap);
	public  List<RuleField> selectByRuleListNew(Map<String, Object> paramMap);
}
