
package com.baoying.enginex.executor.engine.model;

import java.util.Map;

public class DecisionOptions {
	private String code;//决策选项code
	private String name;//决策选项名称
	private Map<String , Object> inFields;//输入字段
	private Map<String, Object> outFields;//输出字段
	private Integer fType;//输出字段类型
	private Long nodId;//节点id
	private String fieldScope;
	
	
	
	public String getFieldScope() {
		return fieldScope;
	}
	public void setFieldScope(String fieldScope) {
		this.fieldScope = fieldScope;
	}
	public Long getNodId() {
		return nodId;
	}
	public void setNodId(Long nodId) {
		this.nodId = nodId;
	}
	public Integer getfType() {
		return fType;
	}
	public void setfType(Integer fType) {
		this.fType = fType;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, Object> getInFields() {
		return inFields;
	}
	public void setInFields(Map<String, Object> inFields) {
		this.inFields = inFields;
	}
	public Map<String, Object> getOutFields() {
		return outFields;
	}
	public void setOutFields(Map<String, Object> outFields) {
		this.outFields = outFields;
	}
	
	
	
}
