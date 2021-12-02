package com.baoying.enginex.executor.engine.model;

import java.io.Serializable;

public class IndexEngineReportVo implements Serializable {

    private static final long serialVersionUID = -1274492726714567316L;
    private String dayTime;
    private String monthTime;
    private Integer engineId;
    private String engineName;
    private Integer useNum;

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }

    public String getMonthTime() {
        return monthTime;
    }

    public void setMonthTime(String monthTime) {
        this.monthTime = monthTime;
    }

    public Integer getEngineId() {
        return engineId;
    }

    public void setEngineId(Integer engineId) {
        this.engineId = engineId;
    }

    public String getEngineName() {
        return engineName;
    }

    public void setEngineName(String engineName) {
        this.engineName = engineName;
    }

    public Integer getUseNum() {
        return useNum;
    }

    public void setUseNum(Integer useNum) {
        this.useNum = useNum;
    }
}
