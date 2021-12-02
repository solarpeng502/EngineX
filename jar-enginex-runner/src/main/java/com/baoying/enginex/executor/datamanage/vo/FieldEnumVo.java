package com.baoying.enginex.executor.datamanage.vo;

import com.baoying.enginex.executor.datamanage.model.Field;

import java.util.List;


public class FieldEnumVo {

	private Field field;
	
	private List<String> enums;

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public List<String> getEnums() {
		return enums;
	}

	public void setEnums(List<String> enums) {
		this.enums = enums;
	}
}
