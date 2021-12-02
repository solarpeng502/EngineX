package com.baoying.enginex.executor.engine.model;

import java.util.Map;

public class ComplexRule {
	
	private Map<String,Object> result;
	
	private String out;
	
	private Map<String, Object> returnResult;
	
	
	
	public Map<String, Object> getReturnResult() {
		return returnResult;
	}

	public void setReturnResult(Map<String, Object> returnResult) {
		this.returnResult = returnResult;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	public String getOut() {
		return out;
	}

	public void setOut(String out) {
		this.out = out;
	}
	
	
	

}
