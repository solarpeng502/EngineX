package com.baoying.enginex.executor.knowledge.model;


import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;


public class ScorecardRuleContent implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	  
	/**
	 * 主键
	 * */
	private Long id;

   /**
    * 评分卡Id
    * */
	private Long scorecardId;
	
   /**
    * 字段
    * */
	private String field;
	
   /**
    * 字段英文名称
    * */
	private String fieldEn;
	
	/**
	 * 字段值
	 * */
	private String fieldValue; 
	
	/**
	 * 字段id
	 * */
	private Long fieldId; 
	
	  /**
     * 关联的字段的值类型
     * */
    private Integer valueType;
	
	/**
     * 关联的字段的取值范围
     * */
    private String valueScope;
    
    /**
     * 关联的字段的值拆解后的数组
     * */
    private String[] values;
    
    private ScoreCardJson sCardJson;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getScorecardId() {
		return scorecardId;
	}
	
	public void setScorecardId(Long scorecardId) {
		this.scorecardId = scorecardId;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public Long getFieldId() {
		return fieldId;
	}

	public void setFieldId(Long fieldId) {
		this.fieldId = fieldId;
	}
	
	public Integer getValueType() {
		return valueType;
	}

	public void setValueType(Integer valueType) {
		this.valueType = valueType;
	}

	public String getValueScope() {
		return valueScope;
	}

	public void setValueScope(String valueScope) {
		this.valueScope = valueScope;
	}
	
	public String[] getValues() {
		if(valueType == 3){
			values = valueScope.split(",");
		}else{
			values =	new String[]{valueScope}; 
		}
		return values;
	}

	public ScoreCardJson getsCardJson() {
		sCardJson = JSONObject.parseObject(fieldValue, ScoreCardJson.class);
		return sCardJson;
	}

	public String getFieldEn() {
		return fieldEn;
	}

	public void setFieldEn(String fieldEn) {
		this.fieldEn = fieldEn;
	}
	
	
}
