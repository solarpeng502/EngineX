package com.baoying.enginex.executor.knowledge.model;

public class ScorecardExcel {
	
	/**
	 * 评分卡名称
	 * */
	private String name;
	
	/**
	 * 评分卡代码
	 * */
	private String code;
	
	/**
	 * 评分卡描述
	 * */
	private String description;
	
	/**
	 * 版本号
	 * */
	private String version;
	
	/**
	 * 评分卡字段内容
	 * */
	private String  fieldContent;
	
	/**
	 * 评分卡内容
	 * */
	private String content;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getFieldContent() {
		return fieldContent;
	}
	public void setFieldContent(String fieldContent) {
		this.fieldContent = fieldContent;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
