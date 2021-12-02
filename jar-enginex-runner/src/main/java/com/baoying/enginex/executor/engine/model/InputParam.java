package com.baoying.enginex.executor.engine.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class InputParam {
	private Map<String ,Object> inputParam;
	private List<Result>  result;
	// 数组中 符合条件的对象属性
	private Map<String, Set<String>> outputParam;

	public Map<String, Object> getInputParam() {
		return inputParam;
	}

	public void setInputParam(Map<String, Object> inputParam) {
		this.inputParam = inputParam;
	}

	public List<Result> getResult() {
		return result;
	}

	public void setResult(List<Result> result) {
		this.result = result;
	}

	public Map<String, Set<String>> getOutputParam() {
		return outputParam;
	}

	public void setOutputParam(Map<String, Set<String>> outputParam) {
		this.outputParam = outputParam;
	}
}
