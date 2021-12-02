

package com.baoying.enginex.executor.engine.model;

import java.util.Date;

public class ResultSetList {
  private Long id;
  private Integer type;//1.黑名单。2.白名单。3.拒绝规则。4.加减分规则
  private String code;
  private String name;
  private String description;
  private String resultsetId;
  private String expression;
  private Date startDate;
  private Date endDate;
  
  
public Date getStartDate() {
	return startDate;
}
public void setStartDate(Date startDate) {
	this.startDate = startDate;
}
public Date getEndDate() {
	return endDate;
}
public void setEndDate(Date endDate) {
	this.endDate = endDate;
}
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public Integer getType() {
	return type;
}
public void setType(Integer type) {
	this.type = type;
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
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public String getResultsetId() {
	return resultsetId;
}
public void setResultsetId(String resultsetId) {
	this.resultsetId = resultsetId;
}
public String getExpression() {
	return expression;
}
public void setExpression(String expression) {
	this.expression = expression;
}
  
  
  
}
