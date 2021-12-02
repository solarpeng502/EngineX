package com.baoying.enginex.executor.canal;

/**
 * 缓存数据同步表
 */
public enum TableEnum {

    /**
     * 引擎
     */
    T_ENGINE("t_engine", "id", ""),
    T_ENGINE_VERSION("t_engine_version", "version_id", "engine_id"),
    T_ENGINE_NODE("t_engine_node", "node_id", "version_id"),

    /**
     * 指标
     */
    T_FIELD("t_field", "id", ""),
    T_FIELD_INTERFACE("t_field_interface", "id", ""),
    T_FIELD_DATA_SOURCE("t_field_data_source", "id", ""),

    /**
     * 规则
     */
    T_RULE("t_rule", "id", ""),
    T_RULE_VERSION("t_rule_version", "id", "rule_id"),
    T_RULE_CONDITION("t_rule_condition", "id", "version_id"),
    T_RULE_LOOP_GROUP_ACTION("t_rule_loop_group_action", "id", "condition_for_id"),
    T_RULE_FIELD("t_rule_field", "id", "rule_id"),
    /**
     * 策略输出
     */
    T_TACTICS_OUTPUT("t_tactics_output", "id", "tactics_id");

    private String tableName;
    private String primaryId;
    private String foreignId;

    TableEnum(String tableName, String primaryId, String foreignId) {
        this.tableName = tableName;
        this.primaryId = primaryId;
        this.foreignId = foreignId;
    }

    public static TableEnum getByTableName(String tableName) {
        for (TableEnum tableEnum : TableEnum.values()) {
            if (tableName.equals(tableEnum.getTableName())) {
                return tableEnum;
            }
        }
        return null;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(String primaryId) {
        this.primaryId = primaryId;
    }

    public String getForeignId() {
        return foreignId;
    }

    public void setForeignId(String foreignId) {
        this.foreignId = foreignId;
    }
}
