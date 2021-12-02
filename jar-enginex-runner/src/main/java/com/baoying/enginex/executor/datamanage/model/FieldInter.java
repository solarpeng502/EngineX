package com.baoying.enginex.executor.datamanage.model;


import com.baoying.enginex.executor.common.model.BasePage;

import java.io.Serializable;

public class FieldInter extends BasePage implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 * */
	private Integer id;
	
	/**
	 * 衍生字段编号
	 * */
	private Integer fieldRelId;
	
	/**
	 * 公式引用的字段编号
	 * */
	private Integer interFieldId;
	
	/**
	 * 公式引用的字段用户关系编号
	 * */
	private Integer interFieldRelId;
	
	/**
	 * 同名字段的顺序
	 * */
	private Integer seq;
	
	/**
	 * 字段值区间划分
	 * */
	private String interval;
	
	/**
	 * 对应区间的值定义
	 * */
	private String value;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFieldRelId() {
		return fieldRelId;
	}

	public void setFieldRelId(Integer fieldRelId) {
		this.fieldRelId = fieldRelId;
	}

	public Integer getInterFieldId() {
		return interFieldId;
	}

	public void setInterFieldId(Integer interFieldId) {
		this.interFieldId = interFieldId;
	}

	public Integer getInterFieldRelId() {
		return interFieldRelId;
	}

	public void setInterFieldRelId(Integer interFieldRelId) {
		this.interFieldRelId = interFieldRelId;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}

