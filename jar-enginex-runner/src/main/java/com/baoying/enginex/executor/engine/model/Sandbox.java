

package com.baoying.enginex.executor.engine.model;

public class Sandbox {
	private Integer sandbox;//沙盒组编号
	private Integer proportion;//沙盒占用比例
	private String nextNode;//下个节点序号
	private Integer sum;//分母
	private Integer startNumber;//起始值
	private Integer endNumberl;//终止值
	
	
	
	public Integer getSum() {
		return sum;
	}
	public void setSum(Integer sum) {
		this.sum = sum;
	}
	public Integer getStartNumber() {
		return startNumber;
	}
	public void setStartNumber(Integer startNumber) {
		this.startNumber = startNumber;
	}
	public Integer getEndNumberl() {
		return endNumberl;
	}
	public void setEndNumberl(Integer endNumberl) {
		this.endNumberl = endNumberl;
	}
	public Integer getSandbox() {
		return sandbox;
	}
	public void setSandbox(Integer sandbox) {
		this.sandbox = sandbox;
	}
	public Integer getProportion() {
		return proportion;
	}
	public void setProportion(Integer proportion) {
		this.proportion = proportion;
	}
	public String getNextNode() {
		return nextNode;
	}
	public void setNextNode(String nextNode) {
		this.nextNode = nextNode;
	}
	
	
}
