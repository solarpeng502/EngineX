

package com.baoying.enginex.executor.engine.model;

import java.util.Map;

public class ScoreCardEngine {
	private String code;//评分卡编号
	private String name;//评分卡名称
	private String scoreCardName;//评分卡名称
	private Map<String , Object> inFields;//评分可用到的字段
	private Map<String, Object> outFields;//评分卡
	
	
	public String getScoreCardName() {
		return scoreCardName;
	}
	public void setScoreCardName(String scoreCardName) {
		this.scoreCardName = scoreCardName;
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
