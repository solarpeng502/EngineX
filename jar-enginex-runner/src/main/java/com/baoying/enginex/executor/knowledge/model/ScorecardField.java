package com.baoying.enginex.executor.knowledge.model;

import java.io.Serializable;


public class ScorecardField implements Serializable{
	

	private static final long serialVersionUID = 1L;

    /**
     * 主键
     * */
    private Long id;
   
    /**
     * 关联的评分卡的id
     * */
    private Long scorecardId;
   
    /**
     * 关联的字段id
     * */
    private Long fieldId;
    

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

	public Long getFieldId() {
		return fieldId;
	}

	public void setFieldId(Long fieldId) {
		this.fieldId = fieldId;
	}
}
