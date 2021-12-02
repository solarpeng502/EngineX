package com.baoying.enginex.executor.knowledge.model;

public class ScoreCardJson {
	
	private String ouput;
	private Integer index;
	private String formula;
	private String formula_show;
	private String fields;
	
	public String getOuput() {
		return ouput;
	}
	public void setOuput(String ouput) {
		this.ouput = ouput;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public String getFormula_show() {
		return formula_show;
	}
	public void setFormula_show(String formula_show) {
		this.formula_show = formula_show;
	}
	public String getFields() {
		if(fields!=null){
			fields = fields.substring(1,fields.length()-1).trim();
		}
		return fields;
	}
	public void setFields(String fields) {
		this.fields = fields;
	}
}
