

package com.baoying.enginex.executor.engine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors
public class EngineResultSet {
	private Integer id;
	private String uid;
	private String pid;
	private String input;
	private String output;
	private Date create_datetime;
	
	private String result;
	
	private Long engine_id;
	
	private Integer engine_version;
	
	private String uuid;
	
	private String engine_name;
	
	private String engine_code;
	
	private Date startDate;
	
	private Date endDate;
	
	private Integer type;
	
	private Integer subVersion;
	
	private String scorecardscore;
	
	private String datilResult;
	/**
	 *决策表结果
	 */
	private String decisionTablesResult;

	/**
	 *决策树结果
	 */
	private String decisionTreeResult;

	/**
	 * 批量测试批次号
	 */
	private String batchNo;
	
	/**
	 * 批量测试每批测试开始时间
	 */
	private Date startTime;
	
	/**
	 * 批量测试每批次花费时间
	 */
	private String costTime;


	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getScorecardscore() {
		return scorecardscore;
	}

	public void setScorecardscore(String scorecardscore) {
		this.scorecardscore = scorecardscore;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSubVersion() {
		return subVersion;
	}

	public void setSubVersion(Integer subVersion) {
		this.subVersion = subVersion;
	}

	private List<ResultSetList> resultSetList;
	
	
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

	public String getEngine_name() {
		return engine_name;
	}

	public void setEngine_name(String engine_name) {
		this.engine_name = engine_name;
	}

	public String getEngine_code() {
		return engine_code;
	}

	public void setEngine_code(String engine_code) {
		this.engine_code = engine_code;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public Date getCreate_datetime() {
		return create_datetime;
	}

	public void setCreate_datetime(Date create_datetime) {
		this.create_datetime = create_datetime;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Long getEngine_id() {
		return engine_id;
	}

	public void setEngine_id(Long engine_id) {
		this.engine_id = engine_id;
	}

	public Integer getEngine_version() {
		return engine_version;
	}

	public void setEngine_version(Integer engine_version) {
		this.engine_version = engine_version;
	}

	public List<ResultSetList> getResultSetList() {
		return resultSetList;
	}

	public void setResultSetList(List<ResultSetList> resultSetList) {
		this.resultSetList = resultSetList;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getCostTime() {
		return costTime;
	}

	public void setCostTime(String costTime) {
		this.costTime = costTime;
	}

	public String getDatilResult() {
		return datilResult;
	}

	public void setDatilResult(String datilResult) {
		this.datilResult = datilResult;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}
}
